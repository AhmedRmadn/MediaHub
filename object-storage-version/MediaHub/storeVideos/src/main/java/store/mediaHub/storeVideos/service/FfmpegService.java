package store.mediaHub.storeVideos.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import store.mediaHub.storeVideos.Factory.ResolutionFactory;
import store.mediaHub.storeVideos.configuration.ServiceConfiguration;
import store.mediaHub.storeVideos.model.VideoInfo;

@Service
public class FfmpegService {

	@Autowired
	ResolutionFactory resolutionFactory;

	@Autowired
	ServiceConfiguration configuration;

//	private final Path scriptHLSPath = Paths.get("D:/media/scripts/generate-hls.bat");
//	private final Path scriptMasterPath = Paths.get("D:/media/scripts/generate-master-playlist.bat");
//	private final Path scriptVideoResolutionPath = Paths.get("D:/media/scripts/ffmpeg-info.bat");
//	private final Path HLSPath = Paths.get("D:/media/HLS/");
	private String scriptHLSPath;
	private String scriptMasterPath;
	private String scriptVideoResolutionPath;

	@PostConstruct
	public void init() {
		System.out.println(configuration.getScriptHLS());
		System.out.println(configuration.getScriptMaster());
		System.out.println(configuration.getScriptVideoResolution());

		this.scriptHLSPath = Paths.get(configuration.getScriptHLS()).toAbsolutePath().toString();
		this.scriptMasterPath = Paths.get(configuration.getScriptMaster()).toAbsolutePath().toString();
		this.scriptVideoResolutionPath = Paths.get(configuration.getScriptVideoResolution()).toAbsolutePath()
				.toString();
		System.out.println(scriptHLSPath);
		System.out.println(scriptMasterPath);
		System.out.println(scriptVideoResolutionPath);
	}

	public void processVideo(String inputPath, String outputPath, List<Integer> resolutions)
			throws IOException, InterruptedException {
//		Files.createDirectories(HLSPath.resolve(videoID));
		for (int resolution : resolutions)
			runFfmpegScriptOnWindows(outputPath, inputPath, resolutionFactory.getTime(resolution),
					resolutionFactory.getHeight(resolution), resolutionFactory.getWidth(resolution),
					resolutionFactory.getVideoBitrate(resolution), resolutionFactory.getAudioBitrate(resolution));
		generateMasterPlaylist(outputPath);
	}

//	public void processVideo(String videoID, String videoPath, String fileName, List<Integer> resolutions)
//			throws IOException, InterruptedException {
////		Files.createDirectories(HLSPath.resolve(videoID));
//		for (int resolution : resolutions)
//			runFfmpegScriptOnWindows(HLSPath.resolve(videoID), videoPath, resolutionFactory.getTime(resolution),
//					resolutionFactory.getHeight(resolution), resolutionFactory.getWidth(resolution),
//					resolutionFactory.getVideoBitrate(resolution), resolutionFactory.getAudioBitrate(resolution));
//		generateMasterPlaylist(HLSPath.resolve(videoID));
//	}

	public VideoInfo getVideoInfo(String vidPath) throws InterruptedException, IOException {

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", scriptVideoResolutionPath, vidPath);
		System.out.println(vidPath);
		System.out.println(scriptVideoResolutionPath);
		pb.redirectErrorStream(true);
		Process process = pb.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		Integer width = null, height = null;
		Double duration = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
			if (line.startsWith("width=")) {
				width = Integer.parseInt(line.substring(6));
			} else if (line.startsWith("height=")) {
				height = Integer.parseInt(line.substring(7));
			} else if (line.startsWith("duration=")) {
				duration = Double.parseDouble(line.substring(9));
			}
		}

		process.waitFor();
		reader.close();
		if (width != null && height != null && duration != null) {
			VideoInfo videoInfo = new VideoInfo();
			videoInfo.setDuration(duration);
			int minDiff = Integer.MAX_VALUE;
			for (int h : resolutionFactory.getResolutionList()) {
				int diff = Math.abs(h - height);
				if (diff <= minDiff) {
					minDiff = diff;
					videoInfo.setHeight(h);
				}
			}
			videoInfo.setWidth(resolutionFactory.getWidth(videoInfo.getHeight()));
			return videoInfo;
		} else {
			throw new RuntimeException("Could not parse all values from ffprobe output.");
		}

	}

	private void runFfmpegScriptOnWindows(String outputPath, String inputPath, String segmentDuration,
			int resolutionHeight, int resolutionWidth, String videoBitrate, String audioBitrate)
			throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", scriptHLSPath, inputPath.toString(),
				outputPath.toString(), segmentDuration, String.valueOf(resolutionHeight),
				String.valueOf(resolutionWidth), videoBitrate, audioBitrate);
		pb.inheritIO();
		Process process = pb.start();
		int exitCode = process.waitFor();
		if (exitCode != 0) {
			throw new RuntimeException("FFmpeg process failed with exit code " + exitCode);
		}
	}

	private void generateMasterPlaylist(String hlsRootPath) throws IOException, InterruptedException {

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", scriptMasterPath.toString(), hlsRootPath);

		pb.redirectErrorStream(true);
		Process process = pb.start();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("[Script] " + line);
			}
		}

		int exitCode = process.waitFor();
		if (exitCode != 0) {
			throw new RuntimeException("Failed to generate master playlist. Exit code: " + exitCode);
		}
		System.out.println("Master playlist generated successfully at: " + hlsRootPath + ("master.m3u8"));
	}

}

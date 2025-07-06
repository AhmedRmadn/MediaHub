package store.mediaHub.storeVideos.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import store.mediaHub.storeVideos.Factory.ResolutionFactory;
import store.mediaHub.storeVideos.model.Video;
import store.mediaHub.storeVideos.model.VideoInfo;
import store.mediaHub.storeVideos.repository.VideoRepository;

import mediahub.dto.ProcessVideoDTO;;

@Service
public class StoreService {

	@Autowired
	private VideoRepository videoRepository;

	@Autowired
	private FfmpegService ffmpegService;

	@Autowired
	ResolutionFactory resolutionFactory;

	@RabbitListener(queues = "video-queue")
	@Transactional
	public void store(ProcessVideoDTO processVideoDTO) throws IOException, InterruptedException {

		System.out.println(processVideoDTO.getChunkDir());
		System.out.println(processVideoDTO.getFilePath());
		Video video = videoRepository.createVideoRecord(processVideoDTO.getTitle(), processVideoDTO.getDescription(),
				processVideoDTO.getFileName(), processVideoDTO.getUploadTime(), processVideoDTO.getUploaderId());

		mergeChunks(Paths.get(processVideoDTO.getChunkDir()), processVideoDTO.getTotalChunks(),
				Paths.get(processVideoDTO.getFilePath()).resolve(processVideoDTO.getFileName()));
//		FileSystemUtils.deleteRecursively(Paths.get(processVideoDTO.getChunkDir()));
		System.out.println(" video path "
				+ Paths.get(processVideoDTO.getFilePath()).resolve(processVideoDTO.getFileName()).toString());
		VideoInfo videoInfo = ffmpegService.getVideoInfo(
				Paths.get(processVideoDTO.getFilePath()).resolve(processVideoDTO.getFileName()).toString());

		video = videoRepository.updateVideo(video, videoInfo.getDuration(), videoInfo.getWidth(),
				videoInfo.getHeight());
		finalizeUpload(video, Paths.get(processVideoDTO.getFilePath()).resolve(processVideoDTO.getFileName()));

	}

	private void mergeChunks(Path chunkDir, int totalChunks, Path finalFile) throws IOException {
		try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(finalFile))) {
			for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
				Path chunkFile = chunkDir.resolve("chunk_" + chunkIndex);

				Files.copy(chunkFile, outputStream);
			}
		}
//		FileSystemUtils.deleteRecursively(chunkDir);
	}

	private void finalizeUpload(Video video, Path inputVideoPath) throws IOException, InterruptedException {
		List<Integer> resList = new ArrayList<Integer>();
		for (int res : resolutionFactory.getResolutionList()) {
			if (res <= video.getHeight()) {
				resList.add(res);
			}
		}
		Collections.sort(resList);
//		resList = resList.subList(0, 2);
		ffmpegService.processVideo(video.getVideoId(), inputVideoPath, video.getFileName(), resList);
		System.out.println("Processing started");
	}

}

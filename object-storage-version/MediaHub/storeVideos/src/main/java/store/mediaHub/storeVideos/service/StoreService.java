package store.mediaHub.storeVideos.service;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import io.minio.MinioClient;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import store.mediaHub.storeVideos.Factory.ResolutionFactory;
import store.mediaHub.storeVideos.configuration.ServiceConfiguration;
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
	ServiceConfiguration configuration;

	@Autowired
	private MinioClient minioClient;

	@Autowired
	ResolutionFactory resolutionFactory;

	@Value("${minio.bucket}")
	private String bucket;

	private String localStoragePath;

	private String streamFolderStorage;

	@PostConstruct
	public void init() {
		this.localStoragePath = configuration.getLocalStoragePath();
		this.streamFolderStorage = configuration.getStreamFolderStorage();
	}

	@RabbitListener(queues = "video-queue")
	@Transactional
	public void store(ProcessVideoDTO processVideoDTO) throws Exception {

		System.out.println(processVideoDTO.getStorageUploadKey());
		try {
			Video video = videoRepository.createVideoRecord(processVideoDTO.getTitle(),
					processVideoDTO.getDescription(), processVideoDTO.getFileName(), processVideoDTO.getUploadTime(),
					processVideoDTO.getUploaderId());
			Path storageMergeVideoPath = Paths.get(localStoragePath).resolve(video.getVideoId()).toAbsolutePath();
			Files.createDirectories(storageMergeVideoPath);
			mergeChunks(processVideoDTO.getStorageUploadKey(), processVideoDTO.getTotalChunks(),
					processVideoDTO.getFileName(), storageMergeVideoPath);

//		FileSystemUtils.deleteRecursively(Paths.get(processVideoDTO.getChunkDir()));

			System.out
					.println(" video path " + storageMergeVideoPath.resolve(processVideoDTO.getFileName()).toString());
			VideoInfo videoInfo = ffmpegService
					.getVideoInfo(storageMergeVideoPath.resolve(processVideoDTO.getFileName()).toString());

			video = videoRepository.updateVideo(video, videoInfo.getDuration(), videoInfo.getWidth(),
					videoInfo.getHeight());

			finalizeUpload(video, storageMergeVideoPath, processVideoDTO.getFileName());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	private void mergeChunks(String uploadStorageKey, int totalChunks, String fileName, Path storageMergeVideoPath)
			throws Exception {
		// Create a temporary file to merge into
		storageMergeVideoPath = storageMergeVideoPath.resolve(fileName);

		try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(storageMergeVideoPath))) {
			for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
				String chunkKey = uploadStorageKey + chunkIndex;

				try (InputStream inputStream = minioClient
						.getObject(GetObjectArgs.builder().bucket(bucket).object(chunkKey).build())) {
					inputStream.transferTo(outputStream);
				}
			}
		}

		// Upload the final merged file to MinIO
//		minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(finalObjectKey)
//				.filename(storeMergeVideoPath.toString()).contentType("video/mp4").build());

		// Optionally delete the temp file
//	    Files.deleteIfExists(tempFile);
	}

//	private void mergeChunks(Path chunkDir, int totalChunks, Path finalFile) throws IOException {
//		try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(finalFile))) {
//			for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
//				Path chunkFile = chunkDir.resolve("chunk_" + chunkIndex);
//
//				Files.copy(chunkFile, outputStream);
//			}
//		}
////		FileSystemUtils.deleteRecursively(chunkDir);
//	}

	private void finalizeUpload(Video video, Path basePath, String fileName) throws Exception {
		List<Integer> resList = new ArrayList<Integer>();
		for (int res : resolutionFactory.getResolutionList()) {
			if (res <= video.getHeight()) {
				resList.add(res);
			}
		}
		Collections.sort(resList);
		String inputPath = basePath.resolve(fileName).toString();
		String outputPath = basePath.toString();

		resList = resList.subList(0, 1);
		ffmpegService.processVideo(inputPath, outputPath, resList);
		System.out.println("uploading started");
		publishOnObjectStorage(video, basePath, resList);
		System.out.println("uploading finished");
	}

	private void publishOnObjectStorage(Video video, Path HLSPath, List<Integer> resList) throws Exception {
		String streamVideoKey = generateStreamKey(video);

		Path masterPath = HLSPath.resolve("master.m3u8");
		uploadToMinio(masterPath, streamVideoKey + "/master.m3u8");

		for (int res : resList) {
			Path resDir = HLSPath.resolve(String.valueOf(res));
			if (Files.isDirectory(resDir)) {
				try (Stream<Path> files = Files.walk(resDir)) {
					files.filter(Files::isRegularFile).forEach(path -> {
						String relativePath = HLSPath.relativize(path).toString().replace("\\", "/"); // safety
						String objectKey = streamVideoKey + "/" + relativePath;
						uploadToMinio(path, objectKey);
					});
				}
			}
		}

	}

	private void uploadToMinio(Path filePath, String objectKey) {
		String contentType = filePath.getFileName().toString().toLowerCase().endsWith(".m3u8")
				? "application/vnd.apple.mpegurl"
				: "video/MP2T";
		try {
			minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(objectKey)
					.filename(filePath.toString()).contentType(contentType) // uses file extension
																			// to guess type
					.build());
		} catch (Exception e) {
			System.err.println("Failed to upload " + objectKey + e.getMessage());
		}
	}

	private String generateStreamKey(Video video) {
		return streamFolderStorage + "/" + video.getVideoId();
	}

}

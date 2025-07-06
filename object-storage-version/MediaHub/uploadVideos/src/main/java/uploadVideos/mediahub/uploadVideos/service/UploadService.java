package uploadVideos.mediahub.uploadVideos.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import mediahub.dto.InitUploadRequest;
import mediahub.dto.ProcessVideoDTO;
import uploadVideos.mediahub.uploadVideos.configuration.RabbitMQConfig;
import uploadVideos.mediahub.uploadVideos.configuration.ServiceConfiguration;
import uploadVideos.mediahub.uploadVideos.model.UploadSession;
import uploadVideos.mediahub.uploadVideos.model.UploadStatus;
import uploadVideos.mediahub.uploadVideos.repository.UploadSessionRepository;

@Service
public class UploadService {

	@Autowired
	private UploadSessionRepository uploadSessionRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ServiceConfiguration configuration;

	@Autowired
	private MinioClient minioClient;

	@Value("${minio.bucket}")
	private String storageBucket;

	private String uploadFolderStorage;

	@PostConstruct
	public void init() {
		uploadFolderStorage = configuration.getUploadFolderStorage();
	}

	public UploadSession initUpload(InitUploadRequest initUpload, String userId) throws IOException {
		UploadSession uploadSession = uploadSessionRepository.createUploadSession(initUpload.getFileName(), userId,
				initUpload.getTotalChunks(), UploadStatus.uploading, initUpload.getTitle(),
				initUpload.getDescription());
		String storageUploadKey = generateUploadStorageKey(uploadSession);// This will act as the 'folder'
		uploadSession = uploadSessionRepository.setUploadPath(uploadSession.getUploadId(), storageUploadKey);
		return uploadSession;
	}

	private String generateUploadStorageKey(UploadSession uploadSession) {
		return uploadFolderStorage + "/" + uploadSession.getUploadId() + "/";
	}

	public UploadSession uploadChunk(String uploadSessionId, int chunkIndex, int totalChunks, MultipartFile chunk)
			throws Exception {
		UploadSession uploadSession = uploadSessionRepository.getUploadSessionById(uploadSessionId);
		String objectStorage = uploadSession.getStorageUploadKey() + chunkIndex;
		minioClient.putObject(PutObjectArgs.builder().bucket(storageBucket).object(objectStorage)
				.stream(chunk.getInputStream(), chunk.getSize(), -1).contentType(chunk.getContentType()).build());

		uploadSession = uploadSessionRepository.updateChuncks(uploadSessionId, chunkIndex);

		if (chunkIndex == totalChunks - 1) {
			sendToStoreService(uploadSession);
		}
		return uploadSession;

	}

	private void sendToStoreService(UploadSession uploadSession) {
		ProcessVideoDTO processVideoDTO = new ProcessVideoDTO();
		processVideoDTO.setFileName(uploadSession.getFileName());
		processVideoDTO.setTotalChunks(uploadSession.getTotalChunks());
		processVideoDTO.setUploadTime(uploadSession.getUploadTime());
		processVideoDTO.setStorageUploadKey(uploadSession.getStorageUploadKey());
		processVideoDTO.setTitle(uploadSession.getTitle());
		processVideoDTO.setUploaderId(uploadSession.getUploaderId());
		processVideoDTO.setDescription(uploadSession.getDescription());

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, processVideoDTO);

	}

}

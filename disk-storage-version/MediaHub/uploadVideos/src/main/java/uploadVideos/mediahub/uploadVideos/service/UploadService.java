package uploadVideos.mediahub.uploadVideos.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

//	private final Path uploadPath = Paths.get("D:/media/uploads/");
//	private final String chuckPrefix = "tmp";

	private Path uploadPath;
	private String chuckPrefix;

	@PostConstruct
	public void init() {
//		System.out.println(configuration.getUploadPath());
//		System.out.println(configuration.getChuckPrefix());
		uploadPath = Paths.get(configuration.getUploadPath());
		chuckPrefix = configuration.getChuckPrefix();
	}

	public UploadSession initUpload(InitUploadRequest initUpload, String userId) throws IOException {
		UploadSession uploadSession = uploadSessionRepository.createUploadSession(initUpload.getFileName(), userId,
				initUpload.getTotalChunks(), UploadStatus.uploading, initUpload.getTitle(),
				initUpload.getDescription());
		Path uploadPath = generateUploadPath(uploadSession);
		uploadSession = uploadSessionRepository.setUploadPath(uploadSession.getUploadId(), uploadPath.toString(),
				uploadPath.resolve(chuckPrefix).toString());
		Files.createDirectories(Paths.get(uploadSession.getChunkDir()));
		return uploadSession;
	}

	private Path generateUploadPath(UploadSession uploadSession) {
		Path path = uploadPath.resolve(uploadSession.getUploadId());
		return path;
	}

	public UploadSession uploadChunk(String uploadSessionId, int chunkIndex, int totalChunks, MultipartFile chunk)
			throws IllegalStateException, IOException {
		UploadSession uploadSession = uploadSessionRepository.getUploadSessionById(uploadSessionId);
		Path chunkFile = Paths.get(uploadSession.getChunkDir()).resolve("chunk_" + chunkIndex);
		chunk.transferTo(chunkFile);

		uploadSession = uploadSessionRepository.updateChuncks(uploadSessionId, chunkIndex);

//		 Merge if this is the last chunk
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
		processVideoDTO.setFilePath(uploadSession.getFilePath());
		processVideoDTO.setChunkDir(uploadSession.getChunkDir());
		processVideoDTO.setTitle(uploadSession.getTitle());
		processVideoDTO.setUploaderId(uploadSession.getUploaderId());
		processVideoDTO.setDescription(uploadSession.getDescription());

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, processVideoDTO);

	}

}

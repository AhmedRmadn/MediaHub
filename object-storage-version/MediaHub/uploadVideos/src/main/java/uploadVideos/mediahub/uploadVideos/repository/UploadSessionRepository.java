package uploadVideos.mediahub.uploadVideos.repository;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import uploadVideos.mediahub.uploadVideos.model.UploadSession;
import uploadVideos.mediahub.uploadVideos.model.UploadStatus;

@Repository
public class UploadSessionRepository {

	@Autowired
	private RedisTemplate redisTemplate;

	private final String tableName = "UploadSession";

	public UploadSession createUploadSession(String fileName, String userId, int totalChunks, UploadStatus uploadStatus,
			String title, String description) {
		String uploadId = UUID.randomUUID().toString();
		UploadSession session = new UploadSession();
		session.setUploadId(uploadId);
		session.setFileName(fileName);
		session.setReceivedChunks(0);
		session.setTotalChunks(totalChunks);
		session.setUploaderId(userId);
		session.setUploadTime(new Timestamp(System.currentTimeMillis()));
		session.setStatus(uploadStatus);
		session.setDescription(description);
		session.setTitle(title);
		save(session);
		return session;
	}

	private UploadSession save(UploadSession session) {
		redisTemplate.opsForHash().put(tableName, session.getUploadId(), session);
		return session;
	}

	public UploadSession getUploadSessionById(String uploadId) {
		return (UploadSession) redisTemplate.opsForHash().get(tableName, uploadId);

	}

	public UploadSession updateChuncks(String uploadId, int chunkIndex) {
		UploadSession uploadSession = getUploadSessionById(uploadId);
		uploadSession.setReceivedChunks(uploadSession.getReceivedChunks() + 1);
		save(uploadSession);
		return uploadSession;
	}

	public UploadSession setUploadPath(String uploadId, String storageUploadKey) {
		UploadSession uploadSession = getUploadSessionById(uploadId);
		uploadSession.setStorageUploadKey(storageUploadKey);;
		save(uploadSession);
		return uploadSession;
	}

}

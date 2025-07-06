package uploadVideos.mediahub.uploadVideos.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("UploadSession")
public class UploadSession implements Serializable {

	@Id
	private String uploadId;
	private String fileName;
	private String uploaderId; // Optional: for multi-user support
	private int totalChunks;
	private int receivedChunks;
	private Timestamp uploadTime;
	private UploadStatus status;
	private String title;
	private String description;

	private String storageUploadKey;

	public UploadSession() {
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTotalChunks() {
		return totalChunks;
	}

	public void setTotalChunks(int totalChunks) {
		this.totalChunks = totalChunks;
	}

	public int getReceivedChunks() {
		return receivedChunks;
	}

	public void setReceivedChunks(int receivedChunks) {
		this.receivedChunks = receivedChunks;
	}

	public UploadStatus getStatus() {
		return status;
	}

	public void setStatus(UploadStatus status) {
		this.status = status;
	}

	public String getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
	}

	public Timestamp getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStorageUploadKey() {
		return storageUploadKey;
	}

	public void setStorageUploadKey(String storageUploadKey) {
		this.storageUploadKey = storageUploadKey;
	}

}

package store.mediaHub.storeVideos.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.sql.Timestamp;

public class UploadSession implements Serializable{

	private String uploadId;
	private String fileName;
	private String uploaderId; // Optional: for multi-user support
	private int totalChunks;
	private int receivedChunks;
	private Timestamp uploadTime;
	private UploadStatus status;
	private String filePath;

	private String chunkDir;
	private String title;
	private String description;
	
	

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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getChunkDir() {
		return chunkDir;
	}

	public void setChunkDir(String chunkDir) {
		this.chunkDir = chunkDir;
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
	
	

}

package mediahub.dto;

import java.sql.Timestamp;


public class ProcessVideoDTO {
	
	
	
	public ProcessVideoDTO() {
		
	}
	private String fileName;
	private String uploaderId; // Optional: for multi-user support
	private int totalChunks;
	private Timestamp uploadTime;
	private String filePath;
	private String chunkDir;
	private String title;
	private String description;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUploaderId() {
		return uploaderId;
	}
	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
	}
	public int getTotalChunks() {
		return totalChunks;
	}
	public void setTotalChunks(int totalChunks) {
		this.totalChunks = totalChunks;
	}
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

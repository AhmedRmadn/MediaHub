package store.mediaHub.storeVideos.model;

import java.sql.Timestamp;

public class Video {
	private String videoId;
	private String title;
	private String description;
	private String fileName;
	private double duration;
	private int width;
	private int height;
	private Timestamp uploadTime;
	private String uploaderId;

	// Constructors
	public Video() {
	}

	public Video(String videoId, String title, String description, String fileName, double duration, int width,
			int height, Timestamp uploadTime, String uploaderId) {
		this.videoId = videoId;
		this.title = title;
		this.description = description;
		this.fileName = fileName;
		this.duration = duration;
		this.width = width;
		this.height = height;
		this.uploadTime = uploadTime;
		this.uploaderId = uploaderId;
	}

	// Getters and Setters
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Timestamp getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
	}
}
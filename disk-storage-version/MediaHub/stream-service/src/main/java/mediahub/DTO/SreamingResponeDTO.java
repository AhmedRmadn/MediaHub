package mediahub.DTO;

import java.sql.Timestamp;
import java.util.List;

public class SreamingResponeDTO {
	private String videoId;
	private String title;
	private String description;
	private String url;
	private double duration;
	private List<Integer> resolutions;
	private Timestamp uploadTime;
	private String uploaderId;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public List<Integer> getResolutions() {
		return resolutions;
	}

	public void setResolutions(List<Integer> resolutions) {
		this.resolutions = resolutions;
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

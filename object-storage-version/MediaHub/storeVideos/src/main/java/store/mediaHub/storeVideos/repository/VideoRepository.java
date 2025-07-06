package store.mediaHub.storeVideos.repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import store.mediaHub.storeVideos.model.Video;

@Repository
public class VideoRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public Video createVideoRecord(String title, String description, String fileName, Timestamp uploadTime,
			String uploaderId) {
		String videoId = UUID.randomUUID().toString();
		Video video = new Video();
		video.setVideoId(videoId);
		video.setTitle(title);
		video.setDescription(description);
		video.setFileName(fileName);

		video.setUploadTime(uploadTime);
		video.setUploaderId(uploaderId);

		String query = """
				INSERT INTO videos
				(video_id, title, description, file_name, upload_time, uploader_id)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		int rowsAffected = jdbcTemplate.update(query, videoId.toString(), title, description, fileName, uploadTime,
				uploaderId.toString());

		return rowsAffected > 0 ? video : null;
	}

	public Video updateVideo(Video video, double duration, int width, int height) {
	    video.setDuration(duration);
	    video.setWidth(width);
	    video.setHeight(height);

	    String query = """
	            UPDATE videos SET
	            duration = ?, width = ?, height = ?
	            WHERE video_id = ?
	            """;

	    int rowsAffected = jdbcTemplate.update(query, duration, width, height, video.getVideoId());

	    return rowsAffected > 0 ? video : null;
	}
}

package mediahub.stream_service.repository.row_mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import mediahub.stream_service.model.Video;

@Component
public class VideoRowMapper implements RowMapper<Video> {

	@Override
	public Video mapRow(ResultSet rs, int rowNum) throws SQLException {
		Video video = new Video();
		video.setVideoId(rs.getString("video_id"));
		video.setTitle(rs.getString("title"));
		video.setDescription(rs.getString("description"));
		video.setFileName(rs.getString("file_name"));
		video.setDuration(rs.getDouble("duration"));
		video.setWidth(rs.getInt("width"));
		video.setHeight(rs.getInt("height"));
		video.setUploadTime(rs.getTimestamp("upload_time"));
		video.setUploaderId(rs.getString("uploader_id"));
		return video;
	}

}

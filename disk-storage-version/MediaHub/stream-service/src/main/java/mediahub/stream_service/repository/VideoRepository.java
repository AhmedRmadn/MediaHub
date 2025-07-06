package mediahub.stream_service.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mediahub.stream_service.model.Video;
import mediahub.stream_service.repository.row_mapper.VideoRowMapper;

@Repository
public class VideoRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	VideoRowMapper videoRowMapper;

	public Video getVideoById(String videoId) {
		String query = "select * from videos where video_id = ?";
		List<Video> results = jdbcTemplate.query(query, videoRowMapper, videoId);
		return results.isEmpty() ? null : results.get(0);
	}

}

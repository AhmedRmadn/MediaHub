package mediahub.stream_service.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import mediahub.DTO.SreamingResponeDTO;
import mediahub.stream_service.Factory.ResolutionFactory;
import mediahub.stream_service.configuration.ServiceConfiguration;
import mediahub.stream_service.model.Video;
import mediahub.stream_service.repository.VideoRepository;

@Service
public class StreamService {

	@Autowired
	VideoRepository videoRepository;

	@Autowired
	ResolutionFactory resolutionFactory;

	@Autowired
	ServiceConfiguration configuration;

	/*
	 * http://localhost:8080/hls/75939af5-31a8-4699-9cf2-d908b552ca45/master.m3u8
	 * 
	 * 
	 */
//	private final String HLSPath = "http://localhost:8080/MEDIA-SERVICE/hls/";
	private String mediaUrl;

	@PostConstruct
	public void init() {
		mediaUrl = configuration.getMediaUrl();
	}

	public SreamingResponeDTO getStreamingResponse(String videoId) {
		Video video = videoRepository.getVideoById(videoId);
		SreamingResponeDTO sreamingResponeDTO = new SreamingResponeDTO();
		sreamingResponeDTO.setVideoId(video.getVideoId());
		sreamingResponeDTO.setTitle(video.getTitle());
		sreamingResponeDTO.setDescription(video.getDescription());
		sreamingResponeDTO.setDuration(video.getDuration());
		sreamingResponeDTO.setUploadTime(video.getUploadTime());
		sreamingResponeDTO.setUploaderId(video.getUploaderId());
		sreamingResponeDTO.setUrl(generateUrl(video));
		sreamingResponeDTO.setResolutions(getResolutions(video));
		return sreamingResponeDTO;
	}

	private String generateUrl(Video video) {
		return mediaUrl + video.getVideoId() + "/master.m3u8";
	}

	private List<Integer> getResolutions(Video video) {
		List<Integer> resList = new ArrayList<Integer>();
		for (int res : resolutionFactory.getResolutionList()) {
			if (res <= video.getHeight()) {
				resList.add(res);
			}
		}
		Collections.sort(resList);
		return resList;
	}

}

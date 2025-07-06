package mediahub.stream_service.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
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
	
	@Autowired
	private MinioClient minioClient;

	@Value("${minio.bucket}")
	private String bucket;

	/*
	 * http://localhost:8080/hls/75939af5-31a8-4699-9cf2-d908b552ca45/master.m3u8
	 * 
	 * 
	 */
//	private final String HLSPath = "http://localhost:8080/MEDIA-SERVICE/hls/";
	private String streamFolderStorage;

	@PostConstruct
	public void init() {
		streamFolderStorage = configuration.getStreamFolderStorage();
	}

	public SreamingResponeDTO getStreamingResponse(String videoId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException {
		Video video = videoRepository.getVideoById(videoId);
		SreamingResponeDTO sreamingResponeDTO = new SreamingResponeDTO();
		sreamingResponeDTO.setVideoId(video.getVideoId());
		sreamingResponeDTO.setTitle(video.getTitle());
		sreamingResponeDTO.setDescription(video.getDescription());
		sreamingResponeDTO.setDuration(video.getDuration());
		sreamingResponeDTO.setUploadTime(video.getUploadTime());
		sreamingResponeDTO.setUploaderId(video.getUploaderId());
		sreamingResponeDTO.setUrl(getVideoUrl(generateKey(video)));
		sreamingResponeDTO.setResolutions(getResolutions(video));
		return sreamingResponeDTO;
	}

	private String generateKey(Video video) {
		return streamFolderStorage + "/" + video.getVideoId() + "/master.m3u8";
	}
	
    private String getVideoUrl(String fileName) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(fileName)
                .build());
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

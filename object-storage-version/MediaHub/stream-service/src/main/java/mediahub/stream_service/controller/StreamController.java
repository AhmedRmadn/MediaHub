package mediahub.stream_service.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import mediahub.stream_service.service.StreamService;

import mediahub.DTO.SreamingResponeDTO;

@RestController
public class StreamController {

	@Autowired
	StreamService streamService;

	@GetMapping("stream")
	public ResponseEntity<SreamingResponeDTO> getVideoStream(@RequestParam("videoId") String videoId) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException {
		SreamingResponeDTO sreamingResponeDTO = streamService.getStreamingResponse(videoId);
		return ResponseEntity.ok(sreamingResponeDTO);
	}
}

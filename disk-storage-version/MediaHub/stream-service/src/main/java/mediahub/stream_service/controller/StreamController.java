package mediahub.stream_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mediahub.stream_service.service.StreamService;

import mediahub.DTO.SreamingResponeDTO;

@RestController
public class StreamController {

	@Autowired
	StreamService streamService;

	@GetMapping("stream")
	public ResponseEntity<SreamingResponeDTO> getVideoStream(@RequestParam("videoId") String videoId) {
		SreamingResponeDTO sreamingResponeDTO = streamService.getStreamingResponse(videoId);
		return ResponseEntity.ok(sreamingResponeDTO);
	}
}

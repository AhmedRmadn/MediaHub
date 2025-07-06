package uploadVideos.mediahub.uploadVideos.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mediahub.dto.InitUploadRequest;
import uploadVideos.mediahub.uploadVideos.model.UploadSession;
import uploadVideos.mediahub.uploadVideos.service.UploadService;

@RestController
@RequestMapping("/mediahub")
public class UploadController {

	@Autowired
	UploadService uploadService;

	@PostMapping("/init-upload")
	public ResponseEntity<UploadSession> initUpload(@RequestBody InitUploadRequest initUpload) throws IOException {
		String userId = UUID.randomUUID().toString();
		UploadSession uploadSession = uploadService.initUpload(initUpload, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(uploadSession);
	}

	@PostMapping("/upload-chunk")
	public ResponseEntity<UploadSession> uploadChunk(@RequestParam("uploadSessionId") String uploadSessionId,
			@RequestParam("chunkIndex") int chunkIndex, @RequestParam("totalChunks") int totalChunks,
			@RequestParam("chunk") MultipartFile chunk) throws IOException, InterruptedException {
		UploadSession uploadSession = uploadService.uploadChunk(uploadSessionId, chunkIndex, totalChunks, chunk);
		return ResponseEntity.ok(uploadSession);
	}

}

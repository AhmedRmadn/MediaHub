package uploadVideos.mediahub.uploadVideos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mediahub")
public class TestController {

	@GetMapping("test")
	public String test() {
		return "hello world";
	}

}

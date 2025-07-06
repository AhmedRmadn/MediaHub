package store.mediaHub.storeVideos;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import store.mediaHub.storeVideos.service.FfmpegService;
import store.mediaHub.storeVideos.service.StoreService;

@SpringBootApplication
public class StoreVideosApplication {

	public static void main(String[] args) {
		// SpringApplication.run(StoreVideosApplication.class, args);
		// Get the bean from the Spring context
		SpringApplication.run(StoreVideosApplication.class, args);

	}

//	public static void main(String[] args) throws InterruptedException, IOException {
//		// SpringApplication.run(StoreVideosApplication.class, args);
//		// Get the bean from the Spring context
//		var context = SpringApplication.run(StoreVideosApplication.class, args);
//		FfmpegService ffmpegService = context.getBean(FfmpegService.class);
//
//		// Manually call your test method
//		System.out.println(ffmpegService.getVideoInfo("D:/Engneering/spring/TubeLearn/python/test.mp4"));
//
//		System.out.println("done");
//	}

}

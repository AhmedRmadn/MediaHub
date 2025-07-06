package uploadVideos.mediahub.uploadVideos.configuration;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("upload-service")
public class ServiceConfiguration {
	private String uploadPath;
	private String chuckPrefix;



	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getChuckPrefix() {
		return chuckPrefix;
	}

	public void setChuckPrefix(String chuckPrefix) {
		this.chuckPrefix = chuckPrefix;
	}

}

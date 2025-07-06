package uploadVideos.mediahub.uploadVideos.configuration;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("upload-service")
public class ServiceConfiguration {
	private String uploadFolderStorage;
	public String getUploadFolderStorage() {
		return uploadFolderStorage;
	}

	public void setUploadFolderStorage(String uploadFolderStorage) {
		this.uploadFolderStorage = uploadFolderStorage;
	}
	
	


	
	




}

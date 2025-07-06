package mediahub.stream_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("stream-service")
public class ServiceConfiguration {
	public String getStreamFolderStorage() {
		return streamFolderStorage;
	}

	public void setStreamFolderStorage(String streamFolderStorage) {
		this.streamFolderStorage = streamFolderStorage;
	}

	private String streamFolderStorage;
	
	
	


	
	


	
	

}

package store.mediaHub.storeVideos.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("store-service")
public class ServiceConfiguration {

	private String scriptHLS;
	private String scriptMaster;
	private String scriptVideoResolution;
	private String localStoragePath;
	private String streamFolderStorage;

	public String getScriptHLS() {
		return scriptHLS;
	}

	public void setScriptHLS(String scriptHLS) {
		this.scriptHLS = scriptHLS;
	}

	public String getScriptMaster() {
		return scriptMaster;
	}

	public void setScriptMaster(String scriptMaster) {
		this.scriptMaster = scriptMaster;
	}

	public String getScriptVideoResolution() {
		return scriptVideoResolution;
	}

	public void setScriptVideoResolution(String scriptVideoResolution) {
		this.scriptVideoResolution = scriptVideoResolution;
	}

	

	public String getLocalStoragePath() {
		return localStoragePath;
	}

	public void setLocalStoragePath(String localStoragePath) {
		this.localStoragePath = localStoragePath;
	}

	public String getStreamFolderStorage() {
		return streamFolderStorage;
	}

	public void setStreamFolderStorage(String streamFolderStorage) {
		this.streamFolderStorage = streamFolderStorage;
	}
	
	

}

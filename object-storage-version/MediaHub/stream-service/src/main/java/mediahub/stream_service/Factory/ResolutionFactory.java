package mediahub.stream_service.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ResolutionFactory {

	public static class Resolution {
		private int width;
		private int height;
		private String videoBitrate;
		private String audioBitrate;
		private String time;

		public Resolution(int width, int height, String videoBitrate, String audioBitrate, String time) {
			this.width = width;
			this.height = height;
			this.videoBitrate = videoBitrate;
			this.audioBitrate = audioBitrate;
			this.time = time;
		}

		// All Getters and Setters
		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getVideoBitrate() {
			return videoBitrate;
		}

		public void setVideoBitrate(String videoBitrate) {
			this.videoBitrate = videoBitrate;
		}

		public String getAudioBitrate() {
			return audioBitrate;
		}

		public void setAudioBitrate(String audioBitrate) {
			this.audioBitrate = audioBitrate;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

	private final Map<Integer, Resolution> resolutionDetails;
	private final List<Integer> resolutionList;

	public ResolutionFactory() {
		resolutionDetails = new HashMap<>();
		resolutionList = new ArrayList<Integer>();
		resolutionList.add(144);
		resolutionList.add(240);
		resolutionList.add(360);
		resolutionList.add(480);
		resolutionList.add(720);
		resolutionList.add(1080);
		resolutionList.add(1440);
		resolutionList.add(2160);
		resolutionDetails.put(144, new Resolution(256, 144, "95k", "64k", "8"));
		resolutionDetails.put(240, new Resolution(426, 240, "300k", "64k", "6"));
		resolutionDetails.put(360, new Resolution(640, 360, "800k", "96k", "4"));
		resolutionDetails.put(480, new Resolution(854, 480, "1400k", "128k", "4"));
		resolutionDetails.put(720, new Resolution(1280, 720, "2800k", "128k", "3"));
		resolutionDetails.put(1080, new Resolution(1920, 1080, "5000k", "192k", "2"));
		resolutionDetails.put(1440, new Resolution(2560, 1440, "10000k", "256k", "1.5"));
		resolutionDetails.put(2160, new Resolution(3840, 2160, "25000k", "320k", "1"));
	}

	// Get full Resolution object
	public Resolution getResolution(int resolution) {
		return resolutionDetails.get(resolution);
	}

	public List<Integer> getResolutionList() {
		return resolutionList;
	}

	// Get individual fields (optional convenience methods)
	public int getWidth(int resolution) {
		return getResolution(resolution).getWidth();
	}

	public int getHeight(int resolution) {
		return getResolution(resolution).getHeight();
	}

	public String getVideoBitrate(int resolution) {
		return getResolution(resolution).getVideoBitrate();
	}

	public String getAudioBitrate(int resolution) {
		return getResolution(resolution).getAudioBitrate();
	}

	public String getTime(int resolution) {
		return getResolution(resolution).getTime();
	}

}

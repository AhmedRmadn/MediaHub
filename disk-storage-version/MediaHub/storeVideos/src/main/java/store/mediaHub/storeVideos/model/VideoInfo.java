package store.mediaHub.storeVideos.model;

public class VideoInfo {
	private int width;
	private int height;
	private double duration;

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

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return String.format("Width: %d, Height: %d, Duration: %.2f sec", width, height, duration);
	}
}
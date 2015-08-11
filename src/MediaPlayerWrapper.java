import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaPlayerWrapper {
	
	private final MediaPlayer mediaPlayer;
	
	public MediaPlayerWrapper(Media media) {
		mediaPlayer = new MediaPlayer(media);
	}
	
	protected MediaPlayer getMP() {
		return mediaPlayer;
	}
	
	public void play() { mediaPlayer.play(); }
	public void pause() { mediaPlayer.pause(); }
	public void stop() { mediaPlayer.stop(); }
	public void dispose() { mediaPlayer.dispose(); }
	public void setMute(boolean value) { mediaPlayer.setMute(value); } 
	public void setOnEndOfMedia(Runnable value) { mediaPlayer.setOnEndOfMedia(value); }
}

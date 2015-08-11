import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaMarkerEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class LoopPlayer extends MediaPlayerWrapper {
	
	/* tiempo estimado de delay al que se debe anticipar */
	private static final Duration ANTICIPO = Duration.millis(100.0);
	
	private final MediaPlayer mediaPlayer2;
	
	public LoopPlayer(Media media, double inicioLoop) {
		super(media);
		
		getMP().setOnPlaying(new Runnable() { 
			@Override public void run() {	
				media.getMarkers().put( "marca anticipo", media.getDuration().subtract(ANTICIPO) );
				getMP().setOnPlaying(null); 
			}
		});
		
		mediaPlayer2 = new MediaPlayer(media);
		
		enlazar(getMP(), mediaPlayer2, inicioLoop);
		enlazar(mediaPlayer2, getMP(), inicioLoop);
	}
	
	private void enlazar(MediaPlayer mp1, MediaPlayer mp2, double inicioLoop) {
		mp1.setOnMarker(new EventHandler<MediaMarkerEvent>() {
			@Override public void handle(MediaMarkerEvent event) { 
				mp2.seek(Duration.millis(inicioLoop));
				mp2.play();
			}
		});	
	}
	
	@Override
	public void play() {
		super.play();
		if (mediaPlayer2.getStatus() == MediaPlayer.Status.PAUSED)
			mediaPlayer2.play();
	}
	
	@Override
	public void pause() {
		super.pause();
		mediaPlayer2.pause();
	}
	
	@Override
	public void stop() { 
		super.stop();
		mediaPlayer2.stop(); 
	}
	
	@Override
	public void dispose() { 
		super.dispose();
		mediaPlayer2.dispose();
	}
	
	@Override
	public void setMute(boolean value) {
		super.setMute(value);
		mediaPlayer2.setMute(value);
	} 
	
	@Override 
	public void setOnEndOfMedia(Runnable value) {}
}

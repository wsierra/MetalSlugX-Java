import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;
import java.util.Vector;
import java.nio.file.Path;

public class AdministradorSonido {
	
	static { new JFXPanel(); }		// Inicializa JavaFX runtime
	
	private Vector<MediaPlayerWrapper> reproducciones = new Vector<MediaPlayerWrapper>();
	private boolean encendido = true;
	
	public void setEncendido(boolean encendido) {
		this.encendido = encendido;
		for (MediaPlayerWrapper mp : reproducciones)
			mp.setMute(!encendido);
	}
	
	public boolean isEncendido() {
		return encendido;
	}
	
	public MediaPlayerWrapper reproducir(Media sonido, double inicioLoop) {
		
		MediaPlayerWrapper player = new LoopPlayer(sonido, inicioLoop);
		player.setMute(!encendido);
		player.play();
		
		reproducciones.add(player);
		
		return player;
	}
	
	public MediaPlayerWrapper reproducir(Media sonido) {
		
		MediaPlayerWrapper player = new MediaPlayerWrapper(sonido);
		player.setOnEndOfMedia(new Runnable() { 
			@Override public void run() { 
				finalizar(player); 
			}
		});
		player.setMute(!encendido);
		player.play();
		
		reproducciones.add(player);
		
		return player;
	}
	
	public MediaPlayerWrapper reproducir(Path ruta, double inicioloop) {
		return reproducir(crearSonido(ruta), inicioloop);	
	}
	
	public MediaPlayerWrapper reproducir(Path ruta) {
		return reproducir(crearSonido(ruta));
	}
	
	public void finalizar(MediaPlayerWrapper player) {
		player.dispose();
		reproducciones.remove(player);	
	}
	
	public void liberarRecursos() {
		for (MediaPlayerWrapper mp : reproducciones)
			mp.dispose();
		reproducciones.clear();
	}
	
	static public Media crearSonido(Path ruta) {
		return new Media(ruta.toUri().toString());
	}
	
	@Override
	public String toString() {
		return reproducciones.toString();
	}
}

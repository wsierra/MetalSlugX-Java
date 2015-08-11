import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;
import java.util.Vector;
import java.nio.file.Path;

public class AdministradorSonido {
	
	static { new JFXPanel(); }		// Inicializa JavaFX runtime
	
	private Vector<MediaPlayer> reproducciones = new Vector<MediaPlayer>();
	private boolean encendido = true;
	
	public void setEncendido(boolean encendido) {
		this.encendido = encendido;
		for (MediaPlayer mp : reproducciones)
			mp.setMute(!encendido);
	}
	
	public boolean isEncendido() {
		return encendido;
	}
	
	public MediaPlayer reproducir(Media sonido, boolean loop) {
		
		if (encendido) {
			MediaPlayer player = new MediaPlayer(sonido);
			
			if (loop)
				player.setCycleCount(MediaPlayer.INDEFINITE);
			else {
				player.setOnEndOfMedia(new Runnable() { 
					@Override public void run() { 
						finalizar(player); 
					}
				});
			}
			
			player.play();
			reproducciones.add(player);
			
			return player;
		}
		
		return null;
	}
	
	public MediaPlayer reproducir(Media sonido) {
		return reproducir(sonido, false);
	}
	
	public MediaPlayer reproducir(Path ruta, boolean loop) {
		return reproducir(crearSonido(ruta), loop);	
	}
	
	public MediaPlayer reproducir(Path ruta) {
		return reproducir(ruta, false);
	}
	
	public void finalizar(MediaPlayer player) {
		player.dispose();
		reproducciones.remove(player);	
	}
	
	public void liberarRecursos() {
		for (MediaPlayer mp : reproducciones)
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

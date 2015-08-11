import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.nio.file.Path;
import javax.sound.sampled.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import java.io.IOException;

public class AdministradorSonido {
	
	private Map<Path, List<Clip>> clips = Collections.synchronizedMap(new HashMap<Path, List<Clip>>());
	private Map<Integer, Clip> reproducciones = Collections.synchronizedMap(new HashMap<Integer, Clip>());
	
	private int ultimoID;
	private int ultimoIDLoop;
	
	private volatile boolean mute;
	private volatile boolean pause;
	
	private Executor worker = Executors.newSingleThreadExecutor();
	
	public void cargar(Path ruta, int cantInstancias) {
		worker.execute(new Runnable() {
			@Override public void run() {
				List<Clip> instancias = clips.get(ruta);
				if (instancias == null) {
					instancias = Collections.synchronizedList(new ArrayList<Clip>(cantInstancias));
					clips.put(ruta, instancias);
				}
				
				for (int i = 0; i < cantInstancias; i++)
					instancias.add(crearClip(ruta));
			}
		});
	}
	
	public int reproducir(Path ruta, Integer inicioLoop) {	
		
		int id;
				
		Clip clip = buscarClipDisponible(ruta);
		if (clip != null) {
			id = getProximoID(inicioLoop != null);
			crearReproduccion(id, clip, inicioLoop);
		}
		else
			id = reproducirNuevo(ruta, inicioLoop);
		
		return id;
	}
	
	public int reproducir(Path ruta) {
		return reproducir(ruta, null);
	}
	
	public int reproducirNuevo(Path ruta, Integer inicioLoop) {
		int id = getProximoID(inicioLoop != null);
		
		worker.execute(new Runnable() {
			@Override public void run() {
				Clip clip = crearClip(ruta);
				clip.addLineListener(new LineListener() {
					@Override public void update(LineEvent event) {
						if (event.getType() == LineEvent.Type.STOP)
							clip.close();
					}
				});
				crearReproduccion(id, clip, inicioLoop);
			}
		});	
		
		return id;
	}
	
	public int reproducirNuevo(Path ruta) {
		return reproducirNuevo(ruta, null);
	}
	
	public boolean isMute() {
		return mute;
	}
	
	public void setMute(boolean mute) {
		this.mute = mute;

		synchronized (reproducciones) {
			for (int id : reproducciones.keySet()) {
				Clip clip = reproducciones.get(id);
				((BooleanControl)clip.getControl(BooleanControl.Type.MUTE)).setValue(mute);
				clip.flush();	
			}
		}
	}
	
	public void finalizarTodo() {
		synchronized (reproducciones) {
			for (int id : reproducciones.keySet())
				reproducciones.get(id).stop();
		}
	}
	
	private Clip crearClip(Path ruta) {
		
		Clip clip = null;
		AudioInputStream ais = null;
		try {
			clip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(ruta.toFile());
			clip.open(ais);
		}
		catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				ais.close();
			}
			catch (IOException ex) { 
				ex.printStackTrace(); 
			}
		}
		
		return clip;
	};
		
	private Clip buscarClipDisponible(Path ruta) {
		Clip clip = null;
		List<Clip> instancias = clips.get(ruta);
		if (instancias != null)
			synchronized (instancias) {
				int i;
				for (i = 0; i < instancias.size() && reproducciones.containsValue(instancias.get(i)); i++);
				if (i < instancias.size())
					clip = instancias.get(i);
			}
		return clip;			
	}
	
	private void crearReproduccion(int id, Clip clip, Integer inicioLoop) {

		final LineListener listener = new LineListener() {
			@Override public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {
					reproducciones.remove(id);
					clip.removeLineListener(this);
				}
			}
		};
		
		clip.addLineListener(listener);
		
		reproducciones.put(id, clip);
		((BooleanControl)clip.getControl(BooleanControl.Type.MUTE)).setValue(mute);
		
		clip.setFramePosition(0);
		if (inicioLoop != null) {
			inicioLoop = (int)(inicioLoop / 1000.0 * clip.getFormat().getFrameRate());   //	ms -> frames
			clip.setLoopPoints(inicioLoop, -1);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else
			clip.start();	
	}
	
	private int getProximoID(boolean loop) {
		return loop ? --ultimoIDLoop : ++ultimoID;
	}
	
	private boolean isLoop(int id) {
		return id < 0;		
	}
	
	public static void main(String[] args) {
		AdministradorSonido audio = new AdministradorSonido();
		
		try {
			Thread.sleep(10000);
		}
		catch (InterruptedException ex) {}
		
		audio.cargar(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 5);
		
		
		
		try {
			Thread.sleep(3000);
		}
		catch (InterruptedException ex) {}
		
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"));
		audio.setMute(true);
		
		try {
			Thread.sleep(3000);
		}
		catch (InterruptedException ex) {}
		
		//audio.setMute(false);
		
		try {
			Thread.sleep(20000);
		}
		catch (InterruptedException ex) {}

		int id = audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
		 audio.reproducir(MetalSlugT.DIR_PISTAS.resolve("prueba.wav"), 200);
	
	}
}

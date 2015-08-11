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
	
	private Map<Path, List<ClipWrapper>> clips = Collections.synchronizedMap(new HashMap<Path, List<ClipWrapper>>());
	private Map<Integer, ClipWrapper> reproducciones = Collections.synchronizedMap(new HashMap<Integer, ClipWrapper>());
	
	private int ultimoID;
	private int ultimoIDLoop;
	
	private volatile boolean mute;
	
	private Executor worker = Executors.newSingleThreadExecutor();
	
	public void cargar(Path ruta, int cantInstancias) {
		worker.execute(new Runnable() {
			@Override public void run() {
				List<ClipWrapper> instancias = clips.get(ruta);
				if (instancias == null) {
					instancias = Collections.synchronizedList(new ArrayList<ClipWrapper>(cantInstancias));
					clips.put(ruta, instancias);
				}
				
				for (int i = 0; i < cantInstancias; i++)
					instancias.add(new ClipWrapper(ruta));
			}
		});
	}
	
	public int reproducir(Path ruta, Integer inicioLoop) {	
		
		int id;
				
		ClipWrapper clip = buscarClipDisponible(ruta);
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
				ClipWrapper clip = new ClipWrapper(ruta);
				//~clip.addLineListener(new LineListener() {
					//~@Override public void update(LineEvent event) {
						//~if (event.getType() == LineEvent.Type.STOP)
							//~clip.close();
					//~}
				//~});
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
				ClipWrapper clip = reproducciones.get(id);
				clip.stop();
				clip.setMute(mute);
				int pos = clip.getFramePosition();
				clip.flush();
				clip.setFramePosition(pos);
				if (isLoop(id))
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				else
					clip.start();	
			}
		}		
	}
		
	private ClipWrapper buscarClipDisponible(Path ruta) {
		ClipWrapper clip = null;
		List<ClipWrapper> instancias = clips.get(ruta);
		if (instancias != null)
			synchronized (instancias) {
				int i;
				for (i = 0; i < instancias.size() && reproducciones.containsValue(instancias.get(i)); i++);
				if (i < instancias.size())
					clip = instancias.get(i);
			}
		return clip;			
	}
	
	private void crearReproduccion(int id, ClipWrapper clip, Integer inicioLoop) {

		//~final LineListener listener = new LineListener() {
			//~@Override public void update(LineEvent event) {
				//~if (event.getType() == LineEvent.Type.STOP) {
					//~reproducciones.remove(id);
					//~synchronized (clip) {
						//~clip.removeLineListener(this);
					//~}
				//~}
			//~}
		//~};
		//~
		//~synchronized (clip) {
			//~clip.addLineListener(listener);
		//~}
		
		reproducciones.put(id, clip);
		clip.setMute(mute);
			
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
	
	private static class ClipWrapper {
		
		private final Clip clip;
		private volatile boolean paused;
		
		public ClipWrapper(Path ruta) {
			Clip auxClip = null;
			AudioInputStream ais = null;
			try {
				auxClip = AudioSystem.getClip();
				ais = AudioSystem.getAudioInputStream(ruta.toFile());
				auxClip.open(ais);
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
			clip = auxClip;
		}
		
		public AudioFormat getFormat() 						  { return clip.getFormat(); }
		public int getFramePosition() 				  	      { return clip.getFramePosition(); }
		public void setFramePosition(int frames) 	  		  { clip.setFramePosition(frames); }
		public void setLoopPoints(int start, int end) 		  { clip.setLoopPoints(start, end); }
		public void flush()							  		  { clip.flush(); }
		public void addLineListener(LineListener listener) 	  { clip.addLineListener(listener); }
		public void removeLineListener(LineListener listener) { clip.removeLineListener(listener); }

		public void start() {
			paused = false;
			clip.start();
		}
		
		public void loop(int count) {
			paused = false;
			clip.loop(count);
		}
		
		public void pause() {
			paused = true;
			clip.stop();
		}
		
		public void stop() {
			paused = false;
			clip.stop();
		}
		
		public void setMute(boolean mute) {
			((BooleanControl)clip.getControl(BooleanControl.Type.MUTE)).setValue(mute);
		}
		
		public boolean isPaused() {
			return paused;
		}	
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

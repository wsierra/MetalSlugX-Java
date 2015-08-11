import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.image.BufferStrategy;
import java.nio.file.Path;

public abstract class Juego {
	
	private static final int FPS = 60;

	private EjecucionJuego ejecucion;
	
	private Frame frame;
	private Canvas canvas;
	private Panel panelSecundario;
	private BufferStrategy buffer;
	private GameKeyboard keyboard;
	private AdministradorSonido musica;
	private AdministradorSonido sfx;
	
	private SistemaJuegos sistema;
	private Dimension tamanioLogico;
	private boolean pantallaCompleta;
	private boolean mantRelacionAspecto = true;
	private Color colorFondo = Color.BLACK;
	
	public final void correr(SistemaJuegos sis) throws EjecucionException {

		sistema = sis;
		
		if ( (tamanioLogico = getParamTamanio()) == null ) {
			ejecucionFinalizada();
			throw new EjecucionException("El tamanio del juego devuelto por " + this.getClass().getName() + ".getParamTamanio() debe ser distinto de null");
		}
		if ( (ejecucion = getParamEjecucionJuego()) == null ) {
			ejecucionFinalizada();
			throw new EjecucionException("No se ha especificado un objeto EjecucionJuego valido en " + this.getClass().getName());
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (ClassNotFoundException | InstantiationException | 
			   IllegalAccessException | UnsupportedLookAndFeelException ex) {}
		
		frame = new Frame(getParamTitulo());
		frame.setLayout(new CardLayout());
		frame.add(canvas = new Canvas(), "canvas");	
		frame.add(panelSecundario = new Panel(), "panelSecundario");
		panelSecundario.setBackground(Color.BLACK);
		canvas.setPreferredSize(tamanioLogico);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				ejecucion.parar();
			}	
		});
		
		cambiarModoPantalla(getParamPantallaCompleta());
		
		keyboard = new GameKeyboard();
		musica = new AdministradorSonido();
		sfx = new AdministradorSonido();
		
		canvas.addKeyListener(keyboard);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		canvas.createBufferStrategy(2);
       	buffer = canvas.getBufferStrategy();
		
		canvas.requestFocus();
		ejecucion.start();
	}
	
	public Path getScreenshot() {
		return null;
	}
	
	protected boolean isPantallaCompleta() {
		return pantallaCompleta;
	}
	
	protected boolean isMantenerRelacionAspecto() {
		return mantRelacionAspecto;
	}
	
	protected void setPantallaCompleta(boolean completa) {
		if (pantallaCompleta != completa)	  
			 cambiarModoPantalla(completa);
	}
	
	protected void setMantenerRelacionAspecto(boolean mantener) {
		mantRelacionAspecto = mantener;
		if (mantener)
			limpiarCanvas();
	}
	
	public void setColorFondo(Color color) {
		colorFondo = color != null ? color : Color.BLACK;
	}
	
	public void addKeyListener(KeyListener l) {
		keyboard.addKeyListener(l);
	}
	
	public void removeKeyListener(KeyListener l) {
		keyboard.removeKeyListener(l);
	}
	
	public boolean isKeyPressed(int keyCode) {
		return keyboard.isKeyPressed(keyCode);
	}

	public EjecucionJuego getEjecucion() {
		return ejecucion;
	}
	
	public AdministradorSonido getAdminMusica() {
		return musica;
	}
	
	public AdministradorSonido getAdminSFX() {
		return sfx;
	}
	
	public void setAdminMusica(AdministradorSonido musica) {
		this.musica = musica; 
	}
	
	public void setAdminSFX(AdministradorSonido sfx) {
		this.sfx = sfx; 
	}
	
	protected void retomarBluce() {
		synchronized (ejecucion) {
			ejecucion.setPausa(false);
			ejecucion.notifyAll();
		}
	}
	
	protected void mostrarPanel(Panel panel) {
		
		panelSecundario.add(panel);
		panelSecundario.validate();
		ejecucion.setPausa(true);
		((CardLayout)frame.getLayout()).next(frame);
		
		synchronized (ejecucion) {
			try {
				while (ejecucion.pausado)
					ejecucion.wait();
			}
			catch (InterruptedException e) {}			
		}
		
		panelSecundario.remove(panel);
		panelSecundario.validate();
		keyboard.clear();
		((CardLayout)frame.getLayout()).next(frame);
		canvas.requestFocus();
	}
	
	private void cambiarModoPantalla(boolean completa) {
		frame.dispose();
		frame.setUndecorated(completa);
		frame.setVisible(true);
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(completa ? frame : null);
		pantallaCompleta = completa;
		canvas.requestFocus();
	}
	
	private void limpiarCanvas() {
		Graphics g = buffer.getDrawGraphics();
		canvas.paint(g);
		buffer.show();
		g.dispose();
	}
	
	private void ejecucionFinalizada() {
		if (frame != null)
			frame.dispose();
		if (sistema != null)
			sistema.reanudar();
		ejecucion = null;
		sistema = null;
		frame = null;
		canvas = null;
		buffer = null;
		keyboard = null;
	}
	
	protected abstract String getParamTitulo();
	protected abstract Dimension getParamTamanio();
	protected abstract boolean getParamPantallaCompleta();
	protected abstract EjecucionJuego getParamEjecucionJuego();

	/**  
	 * 	Clase interna				
	 * */
	public abstract class EjecucionJuego extends Thread {
	
		private boolean enBucle;
		private boolean pausado;
		private double delta = 1.0 / FPS;
		private Bloque bloque;
		private Bloque bloqueNuevo;
	
		public EjecucionJuego(Bloque bloqueInicial) {
			bloque = bloqueInicial;
		}
		
		private final synchronized void setPausa(boolean valor) {
			pausado = valor;
			bloque.notificacionPausa(pausado ? Bloque.PAUSADO : Bloque.REANUDADO);	
		}
		
		@Override
		public final void run() {
			inicializar();
			bucle();
			finalizar();
			ejecucionFinalizada();		
		}

		public final void cambiarBloque(Bloque bloqueNuevo) {
			this.bloqueNuevo = bloqueNuevo;
		}
		
		private final void bucle() {
			double nextTime = (double) System.nanoTime() / 1000000000.0;
			double maxTimeDiff = 0.5;
			int skippedFrames = 1;
			int maxSkippedFrames = 5;
			
			bloque.inicializar();
			
			enBucle = true;
			while (enBucle) {
				
				if (bloqueNuevo != null) {
					bloque.finalizar();
					bloque = bloqueNuevo;
					bloque.inicializar();
					bloqueNuevo = null;
				}
				
				double currTime = (double) System.nanoTime() / 1000000000.0;
				if ((currTime - nextTime) > maxTimeDiff) 
					nextTime = currTime;
				if (currTime >= nextTime) {
					nextTime += delta;
					
					keyboard.update();
					bloque.actualizar();

					if ((currTime < nextTime) || (skippedFrames > maxSkippedFrames)) {
						pintar();
						skippedFrames = 1;
					} 
					else {System.out.println("skip");
						skippedFrames++;
					}
				} 
				else {
					int sleepTime = (int)(1000.0 * (nextTime - currTime));
					if (sleepTime > 0) {
						try {
							Thread.sleep(sleepTime);
						} 
						catch (InterruptedException e) {}
					}
				}
			}
			bloque.finalizar();
		}	

		private final void pintar() {
			
			Graphics2D g2d = (Graphics2D) buffer.getDrawGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			double escalaX = canvas.getWidth() / tamanioLogico.getWidth();
			double escalaY = canvas.getHeight() / tamanioLogico.getHeight();
			if (mantRelacionAspecto) 
				if (escalaX < escalaY)
					escalaY = escalaX;
				else
					escalaX = escalaY;	
			g2d.scale(escalaX, escalaY);
			
			if (mantRelacionAspecto) {
				int transX = (int)( canvas.getWidth() / escalaX - tamanioLogico.getWidth() ) / 2;
				int transY = (int)( canvas.getHeight() / escalaY - tamanioLogico.getHeight() ) / 2;
				g2d.translate(transX, transY);	
			}	
			
			g2d.setClip(0, 0, (int)tamanioLogico.getWidth(), (int)tamanioLogico.getHeight());
			g2d.setColor(colorFondo);
			g2d.fillRect(0, 0, (int)tamanioLogico.getWidth(), (int)tamanioLogico.getHeight());
			
			bloque.pintar(g2d);

			buffer.show();
			g2d.dispose();
		}	
		
		protected final void parar() {
        	enBucle = false;
	    }
		
		protected abstract void inicializar();
		protected abstract void finalizar();
	}
	
	protected interface Bloque {
		int PAUSADO = 0;
		int REANUDADO = 1;
		
		void inicializar();
		void actualizar();
		void pintar(Graphics2D g);
		void finalizar();
		void notificacionPausa(int evento);
	}
	
	public static class EjecucionException extends Exception {
		
		private EjecucionException(String message) {
			super(message);
		}	
	}
}

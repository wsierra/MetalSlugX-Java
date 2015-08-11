import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public abstract class Juego {
	
	private EjecucionJuego ejecucion;
	private SistemaJuegos sistema;
	private Frame frame;
	public Canvas canvas;
	private BufferStrategy buffer;
	//private Keyboard keyboard;
	private boolean pantallaCompleta;
	
	public final void correr(SistemaJuegos sis) {
		sistema = sis;
		frame = new Frame(getParamTitulo() != null ? getParamTitulo() : "");
		canvas = new Canvas(){
			public void paint(Graphics g) {
				ejecucion.pintar(g);
			}

		};
		frame.add(canvas, BorderLayout.CENTER);
		canvas.setPreferredSize(getParamTamañoCanvas() != null ? getParamTamañoCanvas() : new Dimension(480, 360));
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				ejecucion.parar();
			}	
		});
		
		//canvas.createBufferStrategy(2);
      // 	buffer = canvas.getBufferStrategy();
       	
		cambiarModoPantalla(getParamPantallaCompleta());
		
		ejecucion = getParamEjecucionJuego();
		ejecucion.start();
	}
	
	private void cambiarModoPantalla(boolean completa) {
		frame.dispose();
		frame.setUndecorated(completa);
		frame.setVisible(true);
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(completa ? frame : null);
		pantallaCompleta = completa;
	}
	
	protected final void setPantallaCompleta(boolean completa) {
		if (pantallaCompleta != completa)	  
			 cambiarModoPantalla(completa);
	}
	
	protected final void ejecucionFinalizada() {
		frame.dispose();
		if (sistema != null)
			sistema.reanudar();
	}
	
	public String getScreenshot() {
		return null;
	}
	
	protected abstract String getParamTitulo();
	protected abstract Dimension getParamTamañoCanvas();
	protected abstract boolean getParamPantallaCompleta();
	protected abstract EjecucionJuego getParamEjecucionJuego();

	/**  
	 * 	Clase interna				
	 * */
	protected abstract class EjecucionJuego extends Thread {
	
		private boolean enBucle;
		private double delta = 1.0/60.0;
	
		public final void run() {
			inicializar();
			bucle();
			finalizar();
			ejecucionFinalizada();		
		}
	
		private void bucle() {
			double nextTime = (double) System.nanoTime() / 1000000000.0;
			double maxTimeDiff = 0.5;
			int skippedFrames = 1;
			int maxSkippedFrames = 5;

			enBucle = true;
			while (enBucle) {
				// convert the time to seconds
				double currTime = (double) System.nanoTime() / 1000000000.0;
				if ((currTime - nextTime) > maxTimeDiff) 
					nextTime = currTime;
				if (currTime >= nextTime) {
					// assign the time for the next update
					nextTime += delta;
					actualizar();

					if ((currTime < nextTime) || (skippedFrames > maxSkippedFrames)) {
						pintar();
						skippedFrames = 1;
					} 
					else {
						skippedFrames++;
					}
				} 
				else {
					// calculate the time to sleep
					int sleepTime = (int)(1000.0 * (nextTime - currTime));
					// sanity checkSystem.out.println("3");
					if (sleepTime > 0) {
						// sleep until the next update
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							// do nothing
						}
					}
				}
			}
		}	

		private void pintar() {
			canvas.repaint();
		}	
		
		protected void parar() {
        	enBucle = false;
	    }

		protected abstract void inicializar();
		protected abstract void actualizar();
		protected abstract void pintar(Graphics g);
		protected abstract void finalizar();
	}		
}

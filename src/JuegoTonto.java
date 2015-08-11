import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JuegoTonto extends Juego {
	
	private static final int ANCHO = 480; 
	private static final int ALTO = 360;
	
	@Override
	public Path getScreenshot() {
		return Paths.get("imagenes","JuegoTontoScreen.png");
	}
	
	@Override
	protected String getParamTitulo() {
		return "Juego Tonto";
	}
	
	@Override
	protected Dimension getParamTamanio() {
		return new Dimension(ANCHO, ALTO);
	}
	
	@Override
	protected boolean getParamPantallaCompleta() {
		return false;
	}
	
	@Override
	protected EjecucionJuego getParamEjecucionJuego() {
		return new EjecucionJuegoTonto();
	}
	
	private class EjecucionJuegoTonto extends EjecucionJuego {
					
		public EjecucionJuegoTonto() {
			super(new Cargar());
		}
		
		@Override
		protected void inicializar() {
			System.out.println("Inicializando juego");
		}
		
 		@Override
 		protected void finalizar() {
			System.out.println("Finalizando juego");
		}	
	}
		
	private class Cargar implements Bloque {
		
		private static final float DURACION_CARGA = 0.7f;
		private int cont = -1;
		
		@Override
		public void inicializar() {
			System.out.println("Inicializando carga");
			setColorFondo(Color.WHITE);
		}
		
		@Override
		public void actualizar() {
			if (cont < 100 * DURACION_CARGA)
				cont++;
			else {
				getEjecucion().cambiarBloque(new Principal());
				System.out.println("Solicitando cambio de bloque");
			}
		}
		
		@Override
		public void pintar(Graphics2D g) {
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(20.0f));
			
			g.fillRect(20, 20, (int)( (ANCHO - 40) / 100.0 * cont / DURACION_CARGA ), 30);
			
			String str = "Cargando " + (int)(cont / DURACION_CARGA)  + "%";
			g.drawString(str, (ANCHO - g.getFontMetrics().stringWidth(str)) / 2, 75);		
		}
		
		@Override
		public void finalizar() {
			System.out.println("Finalizando carga");
			setColorFondo(null);
		}		
	}
	
	
	private class Principal implements Bloque {
		
		private BufferedImage cara;
		private int x;
		private int y;
		private int direccion;
		private int ultimaHorizontal;
		private int proximo;
		
		private KeyAdapter listener;
		
		private final int IZQUIERDA = 0;
		private final int DERECHA = 1;
		private final int ARRIBA = 2;
		private final int ABAJO = 3;
		
		@Override
		public void inicializar() {
			System.out.println("Inicializando principal");
			try {                
				cara = ImageIO.read(new File("imagenes/cara.png"));
			} 
			catch (IOException ex) {}
			
			setColorFondo(Color.WHITE);
			
			listener = new KeyAdapter() {
				
				private String embutido;
				
				@Override
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
						case KeyEvent.VK_E :
							embutido = "e";
							break;
						case KeyEvent.VK_M :
							embutido = "e".equals(embutido) ? embutido + "m" : "";
							break;
						case KeyEvent.VK_B :
							embutido = "em".equals(embutido) ? embutido + "b" : "";
							break;
						case KeyEvent.VK_U :
							embutido = "emb".equals(embutido) ? embutido + "u" : "";
							break;
						case KeyEvent.VK_T :
							embutido = "embu".equals(embutido) ? embutido + "t" : "";
							break;
						case KeyEvent.VK_I :
							embutido = "embut".equals(embutido) ? embutido + "i" : "";
							break;
						case KeyEvent.VK_D :
							embutido = "embuti".equals(embutido) ? embutido + "d" : "";
							break;
						case KeyEvent.VK_O :
							if ("embutid".equals(embutido)) {
								System.out.println("Solicitando cambio de bloque");
								getEjecucion().cambiarBloque(new Embutido());
							}
							else
								embutido = "";
						default :
							embutido = "";
					}		
				}	
			};
			addKeyListener(listener);
			
			x = (ANCHO - cara.getWidth()) / 2;
			y = (ALTO - cara.getHeight()) / 2;
		}
		
		@Override
		public void actualizar() {
			
			if (proximo == 0 || 
				x <= 0 && direccion == IZQUIERDA ||
				y <= 0 && direccion == ARRIBA || 
				x >= ANCHO - cara.getWidth() && direccion == DERECHA || 
				y >= ALTO - cara.getHeight() && direccion == ABAJO) {
					
					int nuevaDir;
					do {
						nuevaDir = (int)(4 * Math.random());
					} while (nuevaDir == direccion);
					direccion = nuevaDir;
					
					proximo = (int)(90 * Math.random() + 10);
			}
								
			switch (direccion) {
				case IZQUIERDA: x-=3; break;
				case DERECHA: x+=3; break;
				case ARRIBA: y-=3; break;
				case ABAJO: y+=3; break;
			}
				
			if ( (direccion == IZQUIERDA || direccion == DERECHA) && direccion != ultimaHorizontal ) {
				ultimaHorizontal = direccion;
				
				AffineTransform af = AffineTransform.getScaleInstance(-1, 1);
				af.translate(-cara.getWidth(), 0);
				AffineTransformOp afo = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				cara = afo.filter(cara, null);
			}
				
			proximo--;
			
			if (isKeyPressed(KeyEvent.VK_R)) {
				System.out.println("Solicitando cambio de bloque (Reinicio)");
				getEjecucion().cambiarBloque(new Cargar());
			}
		}
		
		@Override
		public void pintar(Graphics2D g){
			g.drawImage(cara, x, y, null);
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(3.0f));
			g.drawString("Por favor NO tipee la palabra \"embutido\"", 2, ALTO - 2);
		}
			
		@Override
		public void finalizar() {
			System.out.println("Finalizando principal");
			removeKeyListener(listener);
		}
	}
	
	
	private class Embutido implements Bloque {
		
		private final int IZQUIERDA = 0;
		private final int DERECHA = 1;
		private final int ARRIBA = 2;
		private final int ABAJO = 3;
		
		private BufferedImage salchicha;
		private BufferedImage salchicha1;
		private BufferedImage salchicha2;
		private int secuenciador;
		private int x;
		private int y;
		private int direccion;
		private int ultimaHorizontal = DERECHA;
		private int proximo;
		
		@Override
		public void inicializar() {
			System.out.println("Inicializando embutido");
			
			try {                
				salchicha1 = ImageIO.read(new File("imagenes/emb1.png"));
				salchicha2 = ImageIO.read(new File("imagenes/emb2.png"));
			} 
			catch (IOException ex) {}
			
			salchicha = salchicha1;
			
			setColorFondo(Color.WHITE);
			
			x = (ANCHO - salchicha.getWidth()) / 2;
			y = (ALTO - salchicha.getHeight()) / 2;
		}
		
		@Override
		public void actualizar() {
			
			if (proximo == 0 || 
				x <= 0 && direccion == IZQUIERDA ||
				y <= 0 && direccion == ARRIBA || 
				x >= ANCHO - salchicha.getWidth() && direccion == DERECHA || 
				y >= ALTO - salchicha.getHeight() && direccion == ABAJO) {
					
					int nuevaDir;
					do {
						nuevaDir = (int)(4 * Math.random());
					} while (nuevaDir == direccion);
					direccion = nuevaDir;
					
					proximo = (int)(90 * Math.random() + 10);
			}
								
			switch (direccion) {
				case IZQUIERDA: x-=3; break;
				case DERECHA: x+=3; break;
				case ARRIBA: y-=3; break;
				case ABAJO: y+=3; break;
			}
				
			if ( (direccion == IZQUIERDA || direccion == DERECHA) && direccion != ultimaHorizontal ) {
				ultimaHorizontal = direccion;
				
				AffineTransform af = AffineTransform.getScaleInstance(-1, 1);
				af.translate(-salchicha.getWidth(), 0);
				AffineTransformOp afo = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				salchicha1 = afo.filter(salchicha1, null);
				salchicha2 = afo.filter(salchicha2, null);
			}
				
			proximo--;
			
			secuenciador = (secuenciador + 1) % 10;
			if (secuenciador == 0)
				if (salchicha == salchicha1)
					salchicha = salchicha2;
				else
					salchicha = salchicha1;
			
			if (isKeyPressed(KeyEvent.VK_R)) {
				System.out.println("Solicitando cambio de bloque (Reinicio)");
				getEjecucion().cambiarBloque(new Cargar());
			}
		}
		
		@Override
		public void pintar(Graphics2D g){
			g.drawImage(salchicha, x, y, null);
		}
			
		@Override
		public void finalizar() {
			System.out.println("Finalizando principal");
		}
	}
	
	public static void main(String[] a) {
		try {
			new JuegoTonto().correr(null);
		}
		catch (EjecucionException e) {
			System.err.println(e);
		}
	}
}

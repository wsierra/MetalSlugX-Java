import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;

public class LayoutInfoJugador {
	
	public static final Path DIR_INFO = MetalSlugT.DIR_IMAGENES.resolve("info");
	
	private static final int M = 4; 		// margen
	private static final int FUENTE = 14;
	private static final int ALTURA_LINEA = (int)(FUENTE * 0.9);
	private static final int ESPACIO_VIDAS = 44;
	
	private static final Point P_NUMJUG = new Point(M, M + ALTURA_LINEA);
	private static final Rectangle R_ICONO = new Rectangle(M, P_NUMJUG.y + M, 44, 36);
	private static final Point P_VIDAS = new Point(R_ICONO.x + R_ICONO.width + M,
												   R_ICONO.y + R_ICONO.height / 2);
	
	private static final Rectangle R_MUNI = new Rectangle(R_ICONO.x + R_ICONO.width + ESPACIO_VIDAS, M,
														  132, M + ALTURA_LINEA + M + ALTURA_LINEA + M);				  
	private static final Point P_BALAS = new Point(R_MUNI.x + M, R_MUNI.y + M + ALTURA_LINEA);
	private static final Point P_BOMBAS = new Point(P_BALAS.x, P_BALAS.y + M + ALTURA_LINEA);
	
	private static final Point P_PUNTOS = new Point(R_MUNI.x + R_MUNI.width,
													R_MUNI.y + R_MUNI.height + M + ALTURA_LINEA);
	private static final Dimension TAMANIO = new Dimension(P_PUNTOS.x + M, P_PUNTOS.y + M);
		   														 
	private static final Point P_CORAZONES;
	
	private static HashMap<String, BufferedImage> iconos;
	private static BufferedImage fondo;
	private static BufferedImage fondoIcono;
	private static BufferedImage corazon;
	static {
		iconos = new HashMap<String, BufferedImage>();
		try { 
			fondo = ImageIO.read(DIR_INFO.resolve("fondo.png").toFile());
			fondoIcono = ImageIO.read(DIR_INFO.resolve("fondo_icono.png").toFile());
			corazon = ImageIO.read(DIR_INFO.resolve("corazon.gif").toFile());
			iconos.put("Marco", ImageIO.read(DIR_INFO.resolve("icono_Marco.gif").toFile()));  //Leer contenido de carpeta
			iconos.put("Eri", ImageIO.read(DIR_INFO.resolve("icono_Eri.gif").toFile()));
			iconos.put("Tarma", ImageIO.read(DIR_INFO.resolve("icono_Tarma.gif").toFile()));
			iconos.put("Fio", ImageIO.read(DIR_INFO.resolve("icono_Fio.gif").toFile()));
		} 
		catch (IOException ex) {}
		
		P_CORAZONES = new Point(R_ICONO.x + R_ICONO.width + M,
								R_ICONO.y + R_ICONO.height - corazon.getHeight());
	}
	
	public static void pintar(Graphics2D g, SoldadoJugador s, Point posicion) {
		
		g.translate(posicion.x, posicion.y);
		
		g.drawImage(fondo, 0, 0, TAMANIO.width, TAMANIO.height, null);
		g.drawImage(fondoIcono, R_ICONO.x, R_ICONO.y, R_ICONO.width, R_ICONO.height, null);
		g.drawImage(iconos.get(s.getClass().getName()), 
					R_ICONO.x, R_ICONO.y, 
					R_ICONO.width, R_ICONO.height, 
					null);
					
		for (int i = 0; i < s.getEnergia(); i++)
			g.drawImage(corazon, 
						P_CORAZONES.x + i * corazon.getWidth(),
						P_CORAZONES.y,
						corazon.getWidth(),
						corazon.getHeight(),
						null);			
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, TAMANIO.width, TAMANIO.height);
		g.drawRect(R_ICONO.x, R_ICONO.y, R_ICONO.width, R_ICONO.height);
		g.drawRect(R_MUNI.x, R_MUNI.y, R_MUNI.width, R_MUNI.height);

		//~ Textos.setRelleno(Color.BLACK);
		//~ Textos.setBorde(Color.WHITE);
		g.setFont( g.getFont().deriveFont(Font.BOLD) );
		
		Textos.pintar(g, "P" + (s.getJugador().getNumJugador() + 1),
					  P_NUMJUG.x, P_NUMJUG.y, FUENTE, Textos.IZQUIERDA);
		Textos.pintar(g, "x" + s.getVidas(),
					  P_VIDAS.x, P_VIDAS.y, FUENTE, Textos.IZQUIERDA);
		Textos.pintar(g, "BALAS",
		              P_BALAS.x, P_BALAS.y, FUENTE, Textos.IZQUIERDA);
		Textos.pintar(g, "BOMBAS",
					  P_BOMBAS.x, P_BOMBAS.y, FUENTE, Textos.IZQUIERDA);
					  
		int cantMun = s.getCantMuniciones();
		if (cantMun == Arma.INFINITO)
			Textos.pintar(g, "∞", R_MUNI.x + R_MUNI.width - M + 3, P_BALAS.y + 3, FUENTE * 2, Textos.DERECHA);
		else
			Textos.pintar(g, String.valueOf(cantMun), R_MUNI.x + R_MUNI.width - M, P_BALAS.y, FUENTE, Textos.DERECHA);
			
		Textos.pintar(g, String.valueOf(s.getCantGranadas()),
		              R_MUNI.x + R_MUNI.width - M, P_BOMBAS.y, FUENTE, Textos.DERECHA);
		Textos.pintar(g, String.valueOf(s.getJugador().getPuntaje()),
					  P_PUNTOS.x, P_PUNTOS.y, FUENTE, Textos.DERECHA);
		
		g.setFont( g.getFont().deriveFont(Font.PLAIN) );
		g.translate(-posicion.x, -posicion.y);
	}
	
	public static int getAncho() {
		return TAMANIO.width;
	}
	
	public static int getAlto() {
		return TAMANIO.height;
	}
	
	public static void main(String[] args) {
		try {
			new MetalSlugT().correr(null);
		}
		catch (Juego.EjecucionException e) {}
	}
}

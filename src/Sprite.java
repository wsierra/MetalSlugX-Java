import java.awt.Rectangle; 
import java.awt.Dimension; 
import java.awt.Point; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class Sprite {
	
	private final Dimension medidasLogicas; 
	private final BufferedImage imagen;
	private ArrayList<Elemento[]> filasElementos;
	private HashMap<String, ArrayList<Cuadro>> secuencias;
	
	public Sprite(Path rutaImagen, Dimension medidasLogicas) {
	
		this.medidasLogicas = medidasLogicas;
		
		BufferedImage aux = null;
		try {                
			aux = ImageIO.read(rutaImagen.toFile());
		} 
		catch (IOException ex) {}
		imagen = aux;
		
		filasElementos = new ArrayList<Elemento[]>();
		secuencias = new HashMap<String, ArrayList<Cuadro>>();
	}
	
	public void addFilaElementos(Elemento... elementos) {
		filasElementos.add(elementos.clone());
	}
	
	public void addFilaElementos(int y, int ancho, int alto, int deltaX, int deltaY, int cantidad) {
		Elemento[] elementos =  new Elemento[cantidad];
		for (int i = 0; i < cantidad; i++)
			elementos[i] = new Elemento(i * ancho, y, ancho, alto, deltaX, deltaY);
		filasElementos.add(elementos);
	}
	
	public void addCuadroSecuencia(String nombreSecuencia, int fila, int columna, int duracion) {
		ArrayList<Cuadro> sec = secuencias.get(nombreSecuencia);
		if (sec == null) { 
			sec = new ArrayList<Cuadro>();
			secuencias.put(nombreSecuencia, sec);
		}
		sec.add( new Cuadro(filasElementos.get(fila)[columna], duracion) );
	}
	
	public Dimension getMedidasLogicas() {
		return medidasLogicas;
	}
	
	public BufferedImage getImagen() {
		return imagen;
	}
	
	public ArrayList<Cuadro> getSecuencia(String nombreSecuencia) {
		return secuencias.get(nombreSecuencia);
	}
	
	public static class Cuadro {
		public final Elemento elemento;
		public final int duracion;
		
		public Cuadro(Elemento elemento, int duracion) {
			this.elemento = elemento;
			this.duracion = duracion;
		}
		
		public String toString() {
			return getClass().getName() + ". elemento: " + elemento + ", duracion: " + duracion;
		}	
	}
	
	public static class Elemento {
		public final int x, y;
		public final int ancho, alto;
		public final int deltaX, deltaY;
		
		public Elemento(int x, int y, int ancho, int alto, int deltaX, int deltaY) {
			this.x = x;
			this.y = y;
			this.ancho = ancho;
			this.alto = alto;
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}
		
		public String toString() {
			return getClass().getName() + ". x: " + x + ", y: " + y + ", ancho: " + ancho + ", alto: " + alto + ", deltaX: " + deltaX + ", deltaY: " + deltaY;
		}
	}
}

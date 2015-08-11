import java.util.*;
import java.io.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.nio.file.Path;
import javax.imageio.ImageIO;


public class Ranking extends BloqueMST {
	
	private static final Path PATH_FONDO = MetalSlugT.DIR_IMAGENES.resolve("fondo_ranking.png");
	
	private final MetalSlugT mst;
	private final Queue<Jugador> jugadores;
	
	private Info info;
	private Lectura lectura;
	private BufferedImage fondo;
	
	public Ranking(MetalSlugT mst, List<Jugador> listaJugadores) {
		this.mst = mst;
		Collections.sort(listaJugadores, new Comparator<Jugador>() {
			@Override public int compare(Jugador o1, Jugador o2) {
				int a = o1.getPuntaje();
				int b = o2.getPuntaje();
				return a < b ? 1 : a > b ? -1 : 0;
			}
			@Override public boolean equals(Object obj) {
				return super.equals(obj);
			}
		});
		this.jugadores = new LinkedList<Jugador>(listaJugadores);
	}
	
	public Ranking(MetalSlugT mst) {
		this.mst = mst;
		this.jugadores = new LinkedList<Jugador>();
	}
	
	@Override
	public void inicializar() {
		info = Info.getInfo();
		
		if (!jugadores.isEmpty())
			if (info.entraEnRanking(jugadores.peek().getPuntaje())) {
				lectura = new Lectura();
				mst.addKeyListener(lectura);
			}
			else
				jugadores.clear();
		
		mst.addKeyListener(new KeyAdapter() {
			@Override public void keyPressed(KeyEvent e) {
				if (jugadores.isEmpty() && e.getKeyCode() == KeyEvent.VK_ENTER && !e.isConsumed()) {
					mst.removeKeyListener(this);
					mst.getEjecucion().cambiarBloque(new SeleccionPersonaje(mst));
				}
			}			
		});
		
		try {
			fondo = ImageIO.read(PATH_FONDO.toFile());
		}
		catch (IOException ex) {}
				
		Textos.setRelleno(Color.WHITE);
		Textos.setBorde(Color.BLACK);
	}

	@Override
	public void actualizar() {}
	
	@Override
	public void pintarBloque(Graphics2D g) {
		
		g.drawImage(fondo, 0, 0, null);
		
		if (!jugadores.isEmpty()) 
			Textos.pintar(g, MetalSlugT.jugadorToString(jugadores.peek()) + " - Ingresar nombre: " + lectura.getString(),
						  50, 80, 20, Textos.IZQUIERDA);
						  
		info.pintar(g, new Rectangle(50,130,540,300));
	}
	
	@Override
	public void finalizar() {}
	
	private void lecturaFinalizada(String nombreJugador) {
		info.add(nombreJugador, jugadores.poll().getPuntaje());
		
		if ( jugadores.isEmpty() || !info.entraEnRanking(jugadores.peek().getPuntaje()) ) {
			jugadores.clear();
			mst.removeKeyListener(lectura);
			info.guardar();
		}
	}
	
	private class Lectura extends KeyAdapter {
		
		private static final int MIN = 2;
		private static final int MAX = 15;

		private StringBuilder buffer = new StringBuilder(); 
		
		@Override 
		public void keyPressed(KeyEvent e) {
			
			int kc = e.getKeyCode();
			if (kc == KeyEvent.VK_ENTER && buffer.length() >= MIN) {
				e.consume();
				lecturaFinalizada(getString());
				buffer = new StringBuilder();				
			}
			else if (kc == KeyEvent.VK_BACK_SPACE && buffer.length() != 0)
				buffer.deleteCharAt(buffer.length() - 1);
			else if (buffer.length() < MAX){
				String str = KeyEvent.getKeyText(e.getKeyCode());
				if (str.length() == 1)
					buffer.append(str);
			}
		}
		
		public String getString() {
			return buffer.toString();
		}
	}
	
	private static class Info implements Serializable {
		
		private static final long serialVersionUID = 1234L;
		
		private static final Path PATH_RANK = MetalSlugT.DIR.resolve("ranking.data");
		
		private static final int TAMANIO = 10;
		private static final float POS_NOMBRES = 2.0f; 	// proporcional a la altura de las lineas
		
		private ArrayList<String> nombres = new ArrayList<String>();
		private ArrayList<Integer> puntajes = new ArrayList<Integer>();
		
		public static Info getInfo() {
			
			Info info = null;
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream( new FileInputStream(PATH_RANK.toFile()) );
				info = (Info)ois.readObject();
			} catch (FileNotFoundException | StreamCorruptedException e) {
				if (info == null)
					info = new Info();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			
			return info;
		}
				
		public void guardar() {
			
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream( new FileOutputStream(PATH_RANK.toFile()) );
				oos.writeObject(this);
			} catch(IOException e){
				e.printStackTrace();
			} finally {
				try {
					if (oos != null)
						oos.close(); 
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
		}
		
		public boolean entraEnRanking(int puntaje) {
			int size = puntajes.size();
			return size < TAMANIO || puntaje > puntajes.get(TAMANIO - 1); 
		}
		
		public void add(String nombre, int puntaje) {
			if (entraEnRanking(puntaje)) {
				int ubi = ubicacion(puntaje);
				puntajes.add(ubi, puntaje);
				nombres.add(ubi, nombre);
				
				if (puntajes.size() > TAMANIO) {
					puntajes.remove(TAMANIO);
					nombres.remove(TAMANIO);
				}
			}
		}
		
		private int ubicacion(int puntaje) {
			int i = 0;
			while (i < puntajes.size() && puntaje <= puntajes.get(i)) 
				i++;	
			return i;
		}
		
		public void pintar(Graphics2D g, Rectangle r) {
			
			final int alturaLinea = r.height / TAMANIO;
			
			for (int i = 0; i < TAMANIO; i++) {
				int y = r.y + (i + 1) * alturaLinea;
				Textos.pintar(g, (i + 1) + ".", r.x, y, alturaLinea, Textos.IZQUIERDA);
				
				if (i < puntajes.size()) {
					Textos.pintar(g, nombres.get(i), r.x + (int)(alturaLinea * POS_NOMBRES), y, alturaLinea, Textos.IZQUIERDA);
					Textos.pintar(g, String.valueOf(puntajes.get(i)), r.x + r.width - 1, y, alturaLinea, Textos.DERECHA);
				}
				else
					Textos.pintar(g, ". . .", r.x + (int)(alturaLinea * POS_NOMBRES), y, alturaLinea, Textos.IZQUIERDA);
			}
		}
	}	
		
	public static void main(String[] args) {
		try {
			new MetalSlugT().correr(null);
		}
		catch (Juego.EjecucionException e) {}
	}
}

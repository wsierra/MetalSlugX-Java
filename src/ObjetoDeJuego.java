import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.nio.file.Path;

public abstract class ObjetoDeJuego {

	public static final Path DIR_SPRITES = MetalSlugT.DIR_IMAGENES.resolve("sprites");
	private static final int MARGEN_DESACTIVACION = 50;
	
	/*---------debug info----------*/
	private static int proximoId = 1;
	private int id;	
	/*-----------------------------*/
	
	private Rectangle area;
	private Sprite sprite;
	private AdminSprite admSprite;
	
	private boolean iniciarEnSuelo;
	private boolean removible;
	protected Partida mundo;
	
	public ObjetoDeJuego(Point posicion) {
		this(posicion, null);
	}

	public ObjetoDeJuego(Point posicion, Sprite sprite) {
		id = proximoId++;
		area = new Rectangle(posicion);
		setSprite(sprite);
	}

	public void activar(Partida mundo) {
		if (this.mundo == null) {
			this.mundo = mundo;
			this.mundo.addActivo(this);
			if (iniciarEnSuelo)
				posicionarEnSuelo();
		}
	}
	
	public void desactivar() {
		if (mundo != null) {
			mundo.removeActivo(this);
			mundo = null;
		}
	}
	
	public boolean isActivo() {
		return mundo != null;
	}
	
	public void desplazar(int despX, int despY) {
		area.x += despX;
		area.y += despY;
		
		if (sprite != null) {			//	┐ 
			admSprite.dx1 += despX; 	//	│ 
			admSprite.dy1 += despY;		//	├──> parche feo
			admSprite.dx2 += despX;		//	│ 
			admSprite.dy2 += despY;		//  ┘ 
		}
	}

	public Point getPosicion() {
		return new Point(area.x, area.y);
	}
	
	public Dimension getTamanio() {
		return new Dimension(area.width, area.height);
	}
	
	public int getAncho() {
		return area.width;
	}
	
	public int getAlto() {
		return area.height;
	}
	
	public int getLadoInferior() {
		return area.y + area.height - 1;
	}
	public int getLadoSuperior() {
		return area.y;
	}
	public int getLadoDerecho() {
		return area.x + area.width - 1;
	}
	public int getLadoIzquierdo() {
		return area.x;
	}
	
	protected boolean intersecar(ObjetoDeJuego otro) {
		return area.intersects(otro.area);
	}
	
	protected boolean estaFueraDeLimites() {
		Rectangle limites = mundo.getCamara();
		limites.grow(MARGEN_DESACTIVACION, MARGEN_DESACTIVACION);
		return !limites.intersects(area);
	}
	
	protected boolean estaVisible() {
		return mundo.getCamara().intersects(area);
	}
	
	protected void verificarDesactivacion() {
		if (mundo != null) {
			if (removible) {
				if (estaFueraDeLimites())
					desactivar();
			}
			else if (!estaFueraDeLimites())
				removible = true;
		}
	}
	
	protected int getSuelo() {
		return mundo.getSuelo( (getLadoIzquierdo() + getLadoDerecho()) / 2 );
	}
	
	protected boolean enAire() {
		return getLadoInferior() + 1 < getSuelo();
	}
	
	protected boolean enSuelo() {
		return getLadoInferior() + 1 == getSuelo();
	}
	
	protected void setIniciarEnSuelo(boolean iniciarEnSuelo) {
		this.iniciarEnSuelo = iniciarEnSuelo;	
	}

	protected void posicionarEnSuelo() {
		desplazar(0, getSuelo() - getLadoInferior() - 1);
	}
	
	protected void setSprite(Sprite nuevo) {
		
		sprite = nuevo;
		
		if (sprite != null) {
			area.setSize(sprite.getMedidasLogicas());
			admSprite = new AdminSprite();
		} 
		else
			area.setSize(new Dimension(0, 0));
	}

	protected boolean actualizarSecuencia(String nombre, int voltear) {
		return admSprite.actualizar(nombre, voltear);
	}
	
	public void pintar(Graphics2D g) {
		if (sprite != null) 
			g.drawImage( sprite.getImagen(),
						 admSprite.dx1, admSprite.dy1,
						 admSprite.dx2, admSprite.dy2,
						 admSprite.sx1, admSprite.sy1,
						 admSprite.sx2, admSprite.sy2,
						 null );
	}

	public abstract void actualizar();

	@Override
	public String toString() {
		return "Id: " + id + ". " + getClass().getName() +
			   ". Pos: " + area.x + ", " + area.y + ". Tam: " + area.width + ", " + area.height;
	}

	protected class AdminSprite {

		public static final int NINGUNO = 0;
		public static final int HORIZONTAL = 1;
		public static final int VERTICAL = 2;
		public static final int AMBOS = 3;

		private String nombreSecuencia = "";
		private ArrayList<Sprite.Cuadro> secuencia;
		private int numCuadro;
		private int duracionCuadro;
		private int voltear;

		private int dx1, dy1;
		private int dx2, dy2;
		private int sx1, sy1;
		private int sx2, sy2;

		public boolean actualizar(String nombre, int voltear) {

			if (!nombreSecuencia.equals(nombre)) {
				nombreSecuencia = nombre;
				secuencia = sprite.getSecuencia(nombre);
				numCuadro = 0;
				duracionCuadro = secuencia.get(numCuadro).duracion;
			}

			this.voltear = voltear;
			generarParametros();

			if (duracionCuadro != -1 && --duracionCuadro == 0) {
				numCuadro = (numCuadro + 1) % secuencia.size();
				duracionCuadro = secuencia.get(numCuadro).duracion;
				return numCuadro == 0;
			}

			return false;
		}
		
		private void generarParametros() {
			Sprite.Elemento e = secuencia.get(numCuadro).elemento;

			sx1 = e.x;
			sy1 = e.y;
			sx2 = sx1 + e.ancho;
			sy2 = sy1 + e.alto;

			if (voltear != HORIZONTAL && voltear != AMBOS) {
				dx1 = area.x - e.deltaX;
				dx2 = dx1 + e.ancho;
			}
			else {
				dx1 = area.x + area.width + e.deltaX;
				dx2 = dx1 - e.ancho;
			}

			if (voltear != VERTICAL && voltear != AMBOS) {
				dy1 = area.y - e.deltaY;
				dy2 = dy1 + e.alto;
			}
			else {
				dy1 = area.y + area.height + e.deltaY;
				dy2 = dy1 - e.alto;
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

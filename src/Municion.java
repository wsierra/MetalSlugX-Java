import java.awt.Point;

public abstract class Municion extends ObjetoMovil {

	public static final int DERECHA = 1;
	public static final int IZQUIERDA = 2;
	public static final int ARRIBA = 4;
	public static final int DIAG_DERECHA = DERECHA | ARRIBA;
	public static final int DIAG_IZQUIERDA = IZQUIERDA | ARRIBA;

	private ObjetoCombate creador;
	private int direccion;
	private int danio;
	private boolean colisiono;
	private boolean desactivarEnColision = true;
	private boolean colisionarMuniciones = true;

	public Municion(Point posicion, Sprite sprite, ObjetoCombate creador, Partida mundo) {
		super(posicion, sprite);
		this.creador = creador;
		activar(mundo);
	}

	@Override
	public void actualizar() {
		if ( colisiono && desactivarEnColision || estaFueraDeLimites() )
			desactivar();
		else
			mover();
	}

	public boolean esEnemiga() {
		return creador instanceof Enemigo;
	}
	
	public boolean colisionar(ObjetoCombate oc) {
		boolean estabaVivo = oc.isVivo();
		if (!colisiono && estaVisible() && intersecar(oc) && oc.recibirDanio(danio)) {
			colisiono = true;
			if (!esEnemiga() && estabaVivo && !oc.isVivo())
				((SoldadoJugador)creador).getJugador().aumentarPuntos( ((Enemigo)oc).getPuntos() );
		}
		return !colisiono;
	}
	
	public boolean colisionar(Municion m) {
		if (colisionarMuniciones && m.colisionarMuniciones && !colisiono && !m.colisiono && intersecar(m))
			colisiono = m.colisiono = true;
		return !colisiono;
	}
	
	public boolean colisionar(Rehen r) {
		if (!colisiono && intersecar(r) && r.liberar()) {
			colisiono = true;
			((SoldadoJugador)creador).getJugador().aumentarPuntos( r.getPuntos() );
		}
		return !colisiono;
	}
	
	protected void setColisionarMuniciones(boolean colisionar) {
		colisionarMuniciones = colisionar;
	} 
	
	protected void setDesactivarEnColision(boolean desactivar) {
		desactivarEnColision = desactivar;
	}
	
	protected void setDanio(int danio) {
		this.danio = danio;
	}
	
	protected void setMovimiento(float rapidez, int direc) {
		
		direccion = direc;

		if (direccion == DIAG_DERECHA || direccion == DIAG_IZQUIERDA)
			rapidez /= Math.sqrt(2.0f);

		if ( (direccion & DERECHA) != 0 )
			setVelocidadX(rapidez);
		else if ( (direccion & IZQUIERDA) != 0 )
			setVelocidadX(-rapidez);

		if ( (direccion & ARRIBA) != 0 )
			setVelocidadY(-rapidez);
	}
	
	protected void posicionarCola(int tamanioCola, int direc) {
		desplazar(-getAncho() / 2, -getAlto() / 2);
		
		if (direc == DIAG_DERECHA || direc == DIAG_IZQUIERDA)
			tamanioCola = (int)(tamanioCola / Math.sqrt(2.0f));

		if ( (direc & DERECHA) != 0 )
			desplazar(tamanioCola, 0);
		else if ( (direc & IZQUIERDA) != 0 )
			desplazar(-tamanioCola, 0);

		if ( (direc & ARRIBA) != 0 )
			desplazar(0, -tamanioCola);
	}
	
	protected boolean huboColision() {
		return colisiono;
	}
	
	protected int getDireccion() {
		return direccion;
	}
	
	@Override
	public String toString() {
		return super.toString() + ". " + (esEnemiga() ? "Enemiga" : "Aliada") + ". Dir: " +
			   (direccion == DERECHA ? "D" : 
			    direccion == IZQUIERDA ? "I" : 
			    direccion == ARRIBA ? "A" : 
			    direccion == DIAG_DERECHA ? "DD" :
			    direccion == DIAG_IZQUIERDA ? "DI" :
			    String.valueOf(direccion));
	}
}

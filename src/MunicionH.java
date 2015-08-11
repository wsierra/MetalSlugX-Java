import java.awt.Point;
import java.awt.Dimension;

public class MunicionH extends Municion {

	private static final float RAPIDEZ = 17.0f;
	private static final int DANIO = 2;
	private static final int TAMANIO_COLA = 36;
	
	private static final int RECORRIDO = 150;
		
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("municion_h.gif"), new Dimension(10, 10));

		spriteClase.addFilaElementos( new Sprite.Elemento(0, 0, 10, 60, 0, 2),
									  new Sprite.Elemento(10, 0, 60, 10, 48, 0),
									  new Sprite.Elemento(10, 10, 46, 46, 34, 2) );

		spriteClase.addCuadroSecuencia("vertical", 0, 0, -1);
		spriteClase.addCuadroSecuencia("horizontal", 0, 1, -1);
		spriteClase.addCuadroSecuencia("diagonal", 0, 2, -1);
	}

	public MunicionH(Point posDisparo, int direccion, int desviacion, ObjetoCombate creador, Partida mundo) {
		super(posDisparo, spriteClase, creador, mundo);
		setMovimiento(RAPIDEZ, direccion);
		setDanio(DANIO);
		posicionarCola(TAMANIO_COLA, direccion);

		float velocidadBase = 0;
		if (direccion == DIAG_DERECHA || direccion == DIAG_IZQUIERDA) {
			desviacion /= Math.sqrt(2.0f);
			velocidadBase = (float)(-RAPIDEZ / Math.sqrt(2.0));
		}
		
		if ( (direccion & DERECHA) != 0 || (direccion & IZQUIERDA) != 0 )
			iniciarMovimientoY(new Desviacion(velocidadBase, desviacion));
		
		switch (direccion) {
			case DIAG_DERECHA : iniciarMovimientoX(new Desviacion(-velocidadBase, desviacion)); break;
			case DIAG_IZQUIERDA : iniciarMovimientoX(new Desviacion(velocidadBase, -desviacion)); break;
			case ARRIBA : iniciarMovimientoX(new Desviacion(velocidadBase, desviacion));
		}
	}

	@Override
	public void actualizar() {
		String secuencia;
		switch (getDireccion()) {
			case DERECHA :
			case IZQUIERDA : secuencia = "horizontal"; break;
			case ARRIBA : secuencia = "vertical"; break;
			default : secuencia = "diagonal";
		}
		actualizarSecuencia(secuencia, (getDireccion() & IZQUIERDA) == 0 ? AdminSprite.NINGUNO : AdminSprite.HORIZONTAL);
		super.actualizar();
	}
	
	private static class Desviacion implements Movimiento {
		
		private final float velocidadBase;
		private final float velocidadDesv;
		private final float duracion;
		
		public Desviacion(float velocidadBase, int desviacion) {
			this.velocidadBase = velocidadBase;
			velocidadDesv = desviacion * RAPIDEZ / RECORRIDO;
			duracion = RECORRIDO / RAPIDEZ;
		}
		
		@Override
		public float calcular(int t) {
			return velocidadBase * t + velocidadDesv * Math.min(t, duracion);
		}
	}
}

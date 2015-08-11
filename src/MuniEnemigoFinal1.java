import java.awt.Point;
import java.awt.Dimension;

public class MuniEnemigoFinal1 extends Municion {

	private static final float RAPIDEZ = 2.0f;
	private static final float CAIDA = 0.01f;
	private static final int DANIO = 5;
	private static final int TAMANIO_COLA = 70;

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("muni_enemigo_final_1.gif"), new Dimension(27, 27));
		spriteClase.addFilaElementos(0, 92, 44, 3, 10, 4);
		for (int j = 0; j < 4; j++)
			spriteClase.addCuadroSecuencia("unica", 0, j, 3);
	}

	public MuniEnemigoFinal1(Point posicion, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setDanio(DANIO);
		setColisionarMuniciones(false);
		posicionarCola(TAMANIO_COLA, Municion.IZQUIERDA);
		
		setVelocidadX(-RAPIDEZ);
		iniciarMovimientoY(new Caida(0.0f) {
			@Override public float calcular(int t) {
				return super.calcular(t) * CAIDA;
			}
		});
	}

	@Override
	public void actualizar() {
		if (enAire()) {
			actualizarSecuencia("unica", AdminSprite.NINGUNO);
			super.actualizar();
		}
		else 
			desactivar();
	}
}

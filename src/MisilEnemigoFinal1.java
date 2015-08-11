import java.awt.Point;
import java.awt.Dimension;

public class MisilEnemigoFinal1 extends MunicionExplosiva {

	private static final float RAPIDEZ = 4.0f;
	private static final int DANIO = 10;
	private static final int TAMANIO_COLA = 52;

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("misil_enemigo_final_1.gif"), new Dimension(20, 20));
		spriteClase.addFilaElementos(new Sprite.Elemento(0, 0, 100, 40, 7, 10));
		spriteClase.addFilaElementos(new Sprite.Elemento(0, 40, 100, 40, 7, 10));
		spriteClase.addCuadroSecuencia("unica", 0, 0, 3);
		spriteClase.addCuadroSecuencia("unica", 1, 0, 3);
	}

	public MisilEnemigoFinal1(Point posicion, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setDanio(DANIO);
		posicionarCola(TAMANIO_COLA, Municion.IZQUIERDA);
		setMovimiento(RAPIDEZ, Municion.IZQUIERDA);
	}

	@Override
	public void actualizar() {
		actualizarSecuencia("unica", AdminSprite.NINGUNO);
		super.actualizar();
	}
}

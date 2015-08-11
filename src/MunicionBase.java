import java.awt.Point;
import java.awt.Dimension;

public class MunicionBase extends Municion {

	private static final float RAPIDEZ = 17.0f;
	private static final int DANIO = 1;

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("municion_base.gif"), new Dimension(16, 16));
		spriteClase.addFilaElementos(new Sprite.Elemento(0, 0, 16, 16, 0, 0));
		spriteClase.addCuadroSecuencia("sencilla", 0, 0, -1);
	}

	public MunicionBase(Point posicion, int direccion, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setMovimiento(RAPIDEZ, direccion);
		setDanio(DANIO);
	}

	@Override
	public void actualizar() {
		actualizarSecuencia("sencilla", AdminSprite.NINGUNO);
		super.actualizar();
	}
}

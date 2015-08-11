import java.awt.Point;
import java.awt.Dimension;

public class MunicionHelicoptero extends Municion {

	private static final int DANIO = 4;
	private static final float RAPIDEZ_VERTICAL = 5.0f;
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("municion_helicoptero.gif"), new Dimension(16, 16));
		spriteClase.addFilaElementos(0, 24, 24, 4, 4, 2);
		spriteClase.addCuadroSecuencia("bala", 0, 0, 5);
		spriteClase.addCuadroSecuencia("bala", 0, 1, 5);
	}

	public MunicionHelicoptero(Point posicion, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setDanio(DANIO);
		setVelocidadY(RAPIDEZ_VERTICAL);
		setColisionarMuniciones(false);
	}

	@Override
	public void actualizar() {
		if (!enAire())
			desactivar();
		else {
			actualizarSecuencia("bala", AdminSprite.NINGUNO);
			super.actualizar();
		}
	}
}

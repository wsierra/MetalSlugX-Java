import java.awt.Point;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class MunicionS extends Municion {
	
	private static final int DANIO = 1;
	private static final int DIAMETRO = 4;
	private static final float RAPIDEZ = 12.0f;
	private static final int ALCANCE = 175;
	
	private final Trigger alcance = new Trigger((int)(ALCANCE / RAPIDEZ));
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("municion_s.gif"), new Dimension(DIAMETRO, DIAMETRO));

		spriteClase.addFilaElementos(0, 4, 4, 0, 0, 1);
		spriteClase.addCuadroSecuencia("punto", 0, 0, -1);
	}
	
	public MunicionS(Point posicion, int direccion, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setDanio(DANIO);
		setMovimiento(RAPIDEZ, direccion);
	}
	
	@Override
	public void actualizar() {
		if (alcance.test())
			desactivar();
		else {
			actualizarSecuencia("punto", AdminSprite.NINGUNO);
			super.actualizar();
		}
	}
}

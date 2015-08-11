import java.awt.Point;
import java.awt.Dimension;

public class Helicoptero2 extends Helicoptero {

	private static final int PUNTOS = 250;
	
	private static final Dimension TAMANIO = new Dimension(79, 82);
	private static final int ENERGIA = 30;
	private static final int RADIO_X = (MetalSlugT.getAncho() - TAMANIO.width) / 2;
	private static final int RADIO_Y = 50;
	
	private static final Point MIRA = new Point(6, 75);
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("helicoptero_2.gif"), TAMANIO);
		spriteClase.addFilaElementos(0, 164, 138, 27, 45, 6);
		for (int j = 0; j < 6; j++)
			spriteClase.addCuadroSecuencia(VUELO, 0, j, 1);
	}
	
	public Helicoptero2(Point centro) {
		super(ENERGIA, centro, RADIO_X, RADIO_Y, spriteClase);
	}

	public void disparar() {
		Municion m = new MunicionHelicoptero(getPosicion(), this, mundo);
		m.desplazar(MIRA.x, MIRA.y);
	}
	
	@Override protected void atacar() {}
	
	@Override
	public int getPuntos() {
		return PUNTOS;
	}
}

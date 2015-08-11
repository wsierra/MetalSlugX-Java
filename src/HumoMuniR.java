import java.awt.Point;
import java.awt.Dimension;

public class HumoMuniR extends EfectoEfimero {

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("humo_muni_r.gif"), new Dimension(0, 0));
		spriteClase.addFilaElementos(0, 34, 48, 14, 36, 16);
		
		for (int j = 0; j < 16; j++)
			spriteClase.addCuadroSecuencia(EFECTO, 0, j, 3);
	}

	public HumoMuniR(Point posicion, Partida mundo) {
		super(posicion, spriteClase, mundo);
	}
}

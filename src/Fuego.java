import java.awt.Point;
import java.awt.Dimension;

public class Fuego extends EfectoPerenne {

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("fuego.gif"), new Dimension(0, 0));
		spriteClase.addFilaElementos(0, 108, 130, 54, 119, 6);
		spriteClase.addFilaElementos(130, 108, 130, 54, 119, 6);
		
		for (int j = 0; j < 4; j++)
			spriteClase.addCuadroSecuencia(EFECTO, 0, j, 5);
		for (int j = 0; j < 4; j++)
			spriteClase.addCuadroSecuencia(EFECTO, 1, j, 5);
	}

	public Fuego(Point posicion, Partida mundo) {
		super(posicion, spriteClase, mundo);
	}
}

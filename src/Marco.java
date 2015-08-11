import java.awt.Dimension;
import java.awt.Point;

public class Marco extends SoldadoJugador {
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite( DIR_SPRITES.resolve("marco.gif"), new Dimension(42, 74) );

		spriteClase.addFilaElementos(0, 82, 82, 6, 8, 6);
		spriteClase.addFilaElementos(82, 62, 96, 6, 22, 6);
		spriteClase.addFilaElementos(178, 62, 120, 6, 46, 6);
		spriteClase.addFilaElementos(298, 102, 100, 16, 26, 1);
		spriteClase.addFilaElementos(398, 94, 88, 51, 14, 5);

		spriteClase.addCuadroSecuencia(PARADO, 0, 0, -1);	
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 1, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 2, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 3, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 4, 7);
		spriteClase.addCuadroSecuencia(SALTANDO, 0, 5, -1);
		
		spriteClase.addCuadroSecuencia(PARADO + DIAGONAL, 1, 0, -1);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 4, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 1, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 2, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 3, 7);
		spriteClase.addCuadroSecuencia(SALTANDO + DIAGONAL, 1, 5, -1);

		spriteClase.addCuadroSecuencia(PARADO + ARRIBA, 2, 0, -1);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 4, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 1, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 2, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 3, 7);
		spriteClase.addCuadroSecuencia(SALTANDO + ARRIBA, 2, 5, -1);

		spriteClase.addCuadroSecuencia(GRANADA, 3, 0, 10);

		spriteClase.addCuadroSecuencia(MURIENDO, 4, 0, 10);
		spriteClase.addCuadroSecuencia(MURIENDO, 4, 1, 10);
		spriteClase.addCuadroSecuencia(MURIENDO, 4, 2, 10);
		for (int i = 0; i < 20; i++) {
			spriteClase.addCuadroSecuencia(MURIENDO, 4, 3, 3);
			spriteClase.addCuadroSecuencia(MURIENDO, 4, 4, 3);
		}
	}
	
	public Marco() {
		this(new Point());
	}
	
	public Marco(Point posicion) {
		super(posicion, spriteClase);
		setMiras(new Point(51, 18),new Point(0, -39),new Point(30, -17));
	}
}

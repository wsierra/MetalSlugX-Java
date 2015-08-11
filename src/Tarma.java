import java.awt.Dimension;
import java.awt.Point;

public class Tarma extends SoldadoJugador {
	
	private static final Sprite spriteClase;
	static {
		
		spriteClase = new Sprite( DIR_SPRITES.resolve("tarma.gif"), new Dimension(42, 76) );

		spriteClase.addFilaElementos(0, 78, 82, 4, 6, 6);
		spriteClase.addFilaElementos(82, 64, 100, 8, 24, 6);
		spriteClase.addFilaElementos(182, 62, 124, 8, 48, 6);
		spriteClase.addFilaElementos(306, 80, 96, 10, 20, 1);
		spriteClase.addFilaElementos(402, 92, 90, 29, 14, 5);

		spriteClase.addCuadroSecuencia(PARADO,    0, 0, -1);	
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 1, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 2, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 3, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 0, 4, 7);
		spriteClase.addCuadroSecuencia(SALTANDO,  0, 5, -1);
		
		spriteClase.addCuadroSecuencia(PARADO + DIAGONAL, 1, 0, -1);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 1, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 2, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 3, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + DIAGONAL, 1, 4, 7);
		spriteClase.addCuadroSecuencia(SALTANDO + DIAGONAL,  1, 5, -1);

		spriteClase.addCuadroSecuencia(PARADO + ARRIBA, 2, 0, -1);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 1, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 2, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 3, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO + ARRIBA, 2, 4, 7);
		spriteClase.addCuadroSecuencia(SALTANDO + ARRIBA,  2, 5, -1);

		spriteClase.addCuadroSecuencia(GRANADA, 3, 0, 10);

		spriteClase.addCuadroSecuencia(MURIENDO, 4, 0, 10);
		spriteClase.addCuadroSecuencia(MURIENDO, 4, 1, 10);
		spriteClase.addCuadroSecuencia(MURIENDO, 4, 2, 10);
		for (int i = 0; i < 20; i++) {
			spriteClase.addCuadroSecuencia(MURIENDO, 4, 3, 3);
			spriteClase.addCuadroSecuencia(MURIENDO, 4, 4, 3);
		}
	}
	
	public Tarma() {
		this(new Point());
	}
	
	public Tarma(Point posicion) {
		super(posicion, spriteClase);
		setMiras(new Point(47, 18),new Point(3, -37),new Point(30, -19));	
	}
}

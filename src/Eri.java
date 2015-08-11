import java.awt.Dimension;
import java.awt.Point;

public class Eri extends SoldadoJugador {
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite( DIR_SPRITES.resolve("eri.gif"), new Dimension(44, 76) );

		spriteClase.addFilaElementos(0, 78, 88, 6, 12, 6);
		spriteClase.addFilaElementos(88, 64, 90, 10, 14, 6);
		spriteClase.addFilaElementos(178, 70, 120, 10, 44, 6);
		spriteClase.addFilaElementos(298, 92, 84, 14, 8, 1);
		spriteClase.addFilaElementos(382, 80, 78, 33, 2, 5);

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
	
	public Eri() {
		this(new Point());
	}
	
	public Eri(Point posicion) {
		super(posicion, spriteClase);
		setMiras(new Point(45, 17),new Point(-3, -30),new Point(29, -4));
	}
}

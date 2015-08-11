import java.awt.Dimension;
import java.awt.Point;

public class Fio extends SoldadoJugador {
	
	private static final Sprite spriteClase;
	static {		
		spriteClase = new Sprite( DIR_SPRITES.resolve("fio.gif"), new Dimension(42, 72) );

		spriteClase.addFilaElementos(0, 82, 78, 10, 6, 6);
		spriteClase.addFilaElementos(78, 74, 78, 10, 6, 6);
		spriteClase.addFilaElementos(156, 66, 110, 10, 38, 6);
		spriteClase.addFilaElementos(266, 92, 90, 16, 18, 1);
		spriteClase.addFilaElementos(356, 90, 80, 50, 8, 5);

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
	
	public Fio() {
		this(new Point());
	}
	
	public Fio(Point posicion) {
		super(posicion, spriteClase);
		setMiras(new Point(49, 14),new Point(1, -32),new Point(39, 8));
	}
}

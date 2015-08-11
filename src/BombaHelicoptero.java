import java.awt.Point;
import java.awt.Dimension;

public class BombaHelicoptero extends MunicionExplosiva {

	private static final int DANIO = 20;
	private static final float FLOTABILIDAD = 6.0f;
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("bomba_helicoptero.gif"), new Dimension(22, 24));
		spriteClase.addFilaElementos(0, 32, 50, 5, 23, 7);
		
		for (int j = 0; j < 7; j++)
			spriteClase.addCuadroSecuencia("cayendo", 0, j, 3);
		for (int j = 5; j > 0; j--)
			spriteClase.addCuadroSecuencia("cayendo", 0, j, 3);
	}

	public BombaHelicoptero(Point posicion, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setDanio(DANIO);
		
		iniciarMovimientoY(new Caida(0.0f) {
			@Override
			public float calcular(int t) {
				return super.calcular(t) / FLOTABILIDAD;
			}
		});
	}

	@Override
	public void actualizar() {
		if (!enAire()) {
			posicionarEnSuelo();
			explotar();
		}
		else {
			actualizarSecuencia("cayendo", AdminSprite.NINGUNO);
			super.actualizar();
		}
	}
}

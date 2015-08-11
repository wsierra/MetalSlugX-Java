import java.awt.Point;
import java.awt.Dimension;

public class MunicionTanque extends MunicionExplosiva {

	private static final float RAPIDEZ = 1.5f;
	private static final int DANIO = 10;

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("municion_tanque.gif"), new Dimension(28, 28));
		spriteClase.addFilaElementos(0, 28, 28, 0, 0, 21);
		for (int j = 0; j < 21; j++)
			spriteClase.addCuadroSecuencia("rodar", 0, j, 5);
	}

	public MunicionTanque(Point posicion, int direccion, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setMovimiento(RAPIDEZ, direccion);
		setColisionarMuniciones(false);
		setDanio(DANIO);
		
		iniciarMovimientoY( new Caida(0.0f) {

			@Override
			public float calcular(int t) {
				if (enAire())
					return super.calcular(t);
				else {
					setVelocidadY(0.0f);
					iniciarMovimientoY(null);
					posicionarEnSuelo();
					return super.calcular(t-1);
				}
			}
		});
	}

	@Override
	public void actualizar() {
		actualizarSecuencia("rodar", AdminSprite.NINGUNO);
		super.actualizar();
	}
	
}

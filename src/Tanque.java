import java.awt.Point;
import java.awt.Dimension;

public class Tanque extends ObjetoCombate implements Enemigo {

	private static final int PUNTOS = 200;

	private static final int ENERGIA = 50;
	private static final float RAPIDEZ = 5.0f;
	private static final int DURACION_ESPERA = 240;

	private static final int MIRA_X = 9;
	private static final int MIRA_Y = 14;
	private static final int FUEGO_X = 60;
	private static final int FUEGO_Y = 110;

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("tanque.gif"), new Dimension(98, 161));

		spriteClase.addFilaElementos(0, 158, 196, 17, 35, 2);
		spriteClase.addFilaElementos(196, 158, 196, 17, 35, 4);
		spriteClase.addFilaElementos(392, 182, 200, 37, 39, 4);

		spriteClase.addCuadroSecuencia("normal", 0, 0, 5);
		spriteClase.addCuadroSecuencia("normal", 0, 1, 5);

		for (int i = 0; i < DURACION_ESPERA / 10; i++) {
			spriteClase.addCuadroSecuencia("esperando", 0, 0, 5);
			spriteClase.addCuadroSecuencia("esperando", 0, 1, 5);
		}

		spriteClase.addCuadroSecuencia("disparando", 1, 0, 7);
		spriteClase.addCuadroSecuencia("disparando", 1, 1, 7);
		spriteClase.addCuadroSecuencia("disparando", 1, 2, 7);
		spriteClase.addCuadroSecuencia("disparando", 1, 3, 7);

		spriteClase.addCuadroSecuencia("destruccion", 2, 0, 10);
		spriteClase.addCuadroSecuencia("destruccion", 2, 1, 10);
		spriteClase.addCuadroSecuencia("destruccion", 2, 2, 10);
		spriteClase.addCuadroSecuencia("destruccion", 2, 3, -1);
	}

	private final int xFinal;
	private boolean listoParaAtacar;
	private boolean disparando;
	
	private boolean fuegoIniciado;

	public Tanque(int xInicial, int xFinal) {
		super(ENERGIA, new Point(xInicial, 0), spriteClase);
		this.xFinal = xFinal;
		setVulnerable(true);
		setIniciarEnSuelo(true);
		setVelocidadX(-RAPIDEZ);
	}

	@Override
	public void actualizar() {

		if (isVivo()) {
			if (!listoParaAtacar) {
				mover();
				actualizarSecuencia("normal", AdminSprite.NINGUNO);
				if (getLadoIzquierdo() < xFinal) {
					desplazar(xFinal - getLadoIzquierdo(), 0);
					listoParaAtacar = true;
					disparando = true;
				}
			}
			else {
				if (!disparando)
					disparando = actualizarSecuencia("esperando", AdminSprite.NINGUNO);
				else if (actualizarSecuencia("disparando", AdminSprite.NINGUNO)) {
					disparando = false;
					MunicionTanque m = new MunicionTanque(getPosicion(), Municion.IZQUIERDA, this, mundo);
					m.desplazar(MIRA_X - m.getAncho() / 2, MIRA_Y - m.getAlto() / 2);
				}
			}
		}
		else {
			actualizarSecuencia("destruccion", AdminSprite.NINGUNO);
			if (!fuegoIniciado) {
				fuegoIniciado = true;
				Fuego f = new Fuego(getPosicion(), mundo);
				f.desplazar(FUEGO_X, FUEGO_Y);
			}
		}

		verificarDesactivacion();
	}

	@Override
	protected void notificacionMuerte() {
		mundo.setAvanceCamara(true);
	}

	@Override
	public void activar(Partida mundo) {
		super.activar(mundo);
		mundo.setAvanceCamara(false);
	}
	
	@Override
	public int getPuntos() {
		return PUNTOS;
	}
}

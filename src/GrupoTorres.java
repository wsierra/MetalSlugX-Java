import java.awt.Point;
import java.awt.Dimension;

public class GrupoTorres extends Grupo implements NoMovible {

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
		spriteClase = new Sprite(DIR_SPRITES.resolve("edificio_torres.gif"), new Dimension(63, 49)); 

		spriteClase.addFilaElementos(0, 180, 140, 62, 75, 5);

		spriteClase.addCuadroSecuencia("cerrado", 0, 0, 5);
		spriteClase.addCuadroSecuencia("abriendo", 0, 1, 5);
		spriteClase.addCuadroSecuencia("abriendo", 0, 2, 5);
		spriteClase.addCuadroSecuencia("listo", 0, 3, -1);
		spriteClase.addCuadroSecuencia("destruida", 0, 4, -1);
	}

	public GrupoTorres(Point posicionCamara) {
		super(posicionCamara, spriteClase);
		setVulnerable(true);
	}

	@Override
	public void actualizar() {
		
	}

	@Override
	protected void notificacionMuerte() {}

	@Override
	public void activar(Partida mundo) {
		super.activar(mundo);
	}
	
	@Override
	public int getPuntos() {
		return PUNTOS;
	}
}

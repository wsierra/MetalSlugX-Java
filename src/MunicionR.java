import java.awt.Point;
import java.awt.Dimension;

public class MunicionR extends MunicionExplosiva {

	private static final float RAPIDEZ = 12.0f;
	private static final int DANIO = 10;
	private static final int TAMANIO_COLA = 35;

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("municion_r.gif"), new Dimension(22, 22));
		
		spriteClase.addFilaElementos(0, 118, 30, 86, 4, 1);
		spriteClase.addFilaElementos(30, 118, 30, 86, 4, 1);
		spriteClase.addFilaElementos(60, 30, 118, 4, 12, 2);

		spriteClase.addCuadroSecuencia("horizontal", 0, 0, 3);
		spriteClase.addCuadroSecuencia("horizontal", 1, 0, 3);
		
		spriteClase.addCuadroSecuencia("vertical", 2, 0, 3);
		spriteClase.addCuadroSecuencia("vertical", 2, 1, 3);
	}

	private String secuencia;
	private int voltear;
	private Point posHumo;
	RandomTrigger triggerHumo = new RandomTrigger(1, 4);
		
	public MunicionR(Point posCola, int direccion, ObjetoCombate creador, Partida mundo) {
		super(posCola, spriteClase, creador, mundo);
		setMovimiento(RAPIDEZ, direccion);
		setDanio(DANIO);
		posicionarCola(TAMANIO_COLA, direccion);
		
		posHumo = new Point();
		switch (direccion) {
			case DERECHA :
				secuencia = "horizontal";
				voltear = AdminSprite.NINGUNO;
				posHumo.translate(-TAMANIO_COLA, getAlto() / 2);
				break;
			case IZQUIERDA :
				secuencia = "horizontal";
				voltear = AdminSprite.HORIZONTAL;
				posHumo.translate(TAMANIO_COLA, getAlto() / 2);
				break;
			case ARRIBA :
				secuencia = "vertical";
				voltear = AdminSprite.NINGUNO;
				posHumo.translate(getAncho() / 2, TAMANIO_COLA);
				break;
		}
	}
	
	@Override
	public void actualizar() {
		actualizarSecuencia(secuencia, voltear);
		if (triggerHumo.test())
			new HumoMuniR(new Point(getPosicion().x + posHumo.x, getPosicion().y + posHumo.y), mundo);
		super.actualizar();
 	}
}

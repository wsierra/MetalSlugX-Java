import java.awt.Point;
import java.awt.Dimension;

public class Granada extends MunicionExplosiva {

	private static final int DANIO = 10;
	
	protected static final String SECUENCIA = "secuencia";

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("granada.gif"), new Dimension(20, 20));
		spriteClase.addFilaElementos(0, 52, 52, 16, 16, 16);
		for (int j = 0; j < 16; j++)
			spriteClase.addCuadroSecuencia(SECUENCIA, 0, j, 2);
	}
	
	private int voltear;
	
	public Granada(Point posicion, float angulo, float impulsoInicial, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo);
		setDanio(DANIO);
		
		voltear = angulo <= 90 ? AdminSprite.HORIZONTAL : AdminSprite.NINGUNO;
		
		angulo = (float)Math.toRadians(angulo);
		setVelocidadX( (float)(impulsoInicial * Math.cos(angulo)) );
		iniciarMovimientoY( new Caida( (float)(-impulsoInicial * Math.sin(angulo)) ) );
	}

	@Override
	public void actualizar() {
		if (!enAire()) {
			posicionarEnSuelo();
			explotar();
		}
		else {
			actualizarSecuencia(SECUENCIA, voltear);
			super.actualizar();
		}
	}
}

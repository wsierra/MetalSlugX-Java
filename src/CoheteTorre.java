import java.awt.Point;
import java.awt.Dimension;

public class CoheteTorre extends MunicionExplosiva {

	private static final int DANIO = 10;
	private static final float RAPIDEZ = 2.0f;
	private static final int AMPLITUD_SENO = 10;
	private static final float RAPIDEZ_SENO = 0.15f;
	
	private static final int TAMANIO_COLA = 50;
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("cohete_torre.gif"), new Dimension(18, 18));
		spriteClase.addFilaElementos(0, 97, 97, 72, 72, 9);
		spriteClase.addFilaElementos(97, 97, 97, 72, 72, 9);
		
		for (int j = 0; j < 9; j++) {
			spriteClase.addCuadroSecuencia(j * 10 + "grados", 0, j, 3);
			spriteClase.addCuadroSecuencia(j * 10 + "grados", 1, j, 3);
		}
	}
	
	private final String nombreSecuencia;
	private final int voltear;
	
	public CoheteTorre(Point posicion, float anguloDisparo, ObjetoCombate creador, Partida mundo) {
		super(posicion, spriteClase, creador, mundo); 
		setDanio(DANIO);
		
		anguloDisparo = (float)Math.min(anguloDisparo, 80.0);
		anguloDisparo = (float)Math.max(anguloDisparo, -80.0);
		
		final int angulo = (int)Math.rint(anguloDisparo / 10.0f) * 10;
		nombreSecuencia = (angulo < 0 ? -angulo : angulo) + "grados";
		voltear = angulo < 0 ? AdminSprite.HORIZONTAL : AdminSprite.NINGUNO;
		
		final float factorX = (float)Math.sin(Math.toRadians(angulo));
		final float factorY = (float)Math.cos(Math.toRadians(angulo));
		
		desplazar( -getAncho() / 2 + (int)(TAMANIO_COLA * factorX), 
				   -getAlto() / 2 + (int)(TAMANIO_COLA * factorY) );
		
		iniciarMovimientoX(new Movimiento() {
			final float velLineal = factorX * RAPIDEZ;
			@Override public float calcular(int t) {
				return velLineal * t - factorY * AMPLITUD_SENO * (float)Math.sin(RAPIDEZ_SENO * t);
			}
		});
		iniciarMovimientoY(new Movimiento() {
			final float velLineal = factorY * RAPIDEZ;
			@Override public float calcular(int t) {
				return velLineal * t + factorX * AMPLITUD_SENO * (float)Math.sin(RAPIDEZ_SENO * t);
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
			actualizarSecuencia(nombreSecuencia, voltear);
			super.actualizar();
		}
	}
}

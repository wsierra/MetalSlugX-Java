import java.awt.Point;
import java.awt.Dimension;

public class Explosion extends EfectoEfimero {

	private static final Sprite spriteClase;
	static {MetalSlugT.sfx.cargar(MetalSlugT.DIR_SFX.resolve("mslug-041.wav"), 15);
		spriteClase = new Sprite(DIR_SPRITES.resolve("explosion.gif"), new Dimension(0, 0));
		spriteClase.addFilaElementos(0, 146, 142, 67, 95, 4);
		
		for (int j = 0; j < 4; j++)
			spriteClase.addCuadroSecuencia(EFECTO, 0, j, 7);
	}

	public Explosion(Point posicion, Partida mundo) {
		super(posicion, spriteClase, mundo);
		MetalSlugT.sfx.reproducir(MetalSlugT.DIR_SFX.resolve("mslug-041.wav"));
	}
}

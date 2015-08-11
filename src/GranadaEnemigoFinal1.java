import java.awt.Point;
import java.awt.Dimension;

public class GranadaEnemigoFinal1 extends Granada {
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("granada_enemigo_final_1.gif"), new Dimension(24, 24));
		spriteClase.addFilaElementos(new Sprite.Elemento(0, 0, 24, 24, 0, 0));
		spriteClase.addCuadroSecuencia(SECUENCIA, 0, 0, 2);
	}

	public GranadaEnemigoFinal1(Point posicion,	float angulo, float impulsoInicial, 
								ObjetoCombate creador, Partida mundo) {
		super(posicion, angulo, impulsoInicial, creador, mundo);
		setSprite(spriteClase);
		desplazar(-getAncho() / 2, -getAlto() / 2);
	}
}

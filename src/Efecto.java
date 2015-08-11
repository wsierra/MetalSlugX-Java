import java.awt.Point;

public abstract class Efecto extends ObjetoDeJuego {
	
	protected static final String EFECTO = "efecto";
	
	public Efecto(Point posicion, Sprite sprite, Partida mundo) {
		super(posicion, sprite);
		activar(mundo);
	}
}

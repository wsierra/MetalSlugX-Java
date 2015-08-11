import java.awt.Point;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class ArmaBase extends ArmaSimple {
	
	private static final int TIEMPO_RECUPERACION = 6;

	public ArmaBase() {
		this(new Point());
	}
	
	public ArmaBase(Point posicion) {
		super(posicion, TIEMPO_RECUPERACION);
		recargar(INFINITO);
	}

	@Override
	public Municion crearMunicion() {
		Municion m = new MunicionBase(getPosicion(), getDireccionDisparo(), getPortador(), mundo);
		m.desplazar(-m.getAncho() / 2, -m.getAlto() / 2);
		return m;
	}
}

import java.awt.Point;

public abstract class EfectoPerenne extends Efecto {
	
	public EfectoPerenne(Point posicion, Sprite sprite, Partida mundo) {
		super(posicion, sprite, mundo);
	}
	
	@Override
	public void actualizar() {
		actualizarSecuencia(EFECTO, AdminSprite.NINGUNO);
		verificarDesactivacion();
	}
}

import java.awt.Point;

public abstract class EfectoEfimero extends Efecto {
	
	public EfectoEfimero(Point posicion, Sprite sprite, Partida mundo) {
		super(posicion, sprite, mundo);
	}

	@Override
	public void actualizar() {
		if (actualizarSecuencia(EFECTO, AdminSprite.NINGUNO))
			desactivar();
	}
}

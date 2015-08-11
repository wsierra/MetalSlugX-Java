import java.awt.Point;

public abstract class MunicionExplosiva extends Municion {

	public MunicionExplosiva(Point posicion, Sprite sprite, ObjetoCombate creador, Partida mundo) {
		super(posicion, sprite, creador, mundo);
		setDesactivarEnColision(false);
	}
	
	@Override
	public void actualizar() {
		if (huboColision())
			explotar();
		else
			super.actualizar();
 	}
	
	protected void explotar() {
		Point posExplosion = getPosicion();
		posExplosion.translate(getAncho() / 2, getAlto() / 2);
		new Explosion(posExplosion, mundo);
		desactivar();
	}
}

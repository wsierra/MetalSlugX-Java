import java.awt.Point;

public abstract class BonusMunicion extends Bonus {
	
	private Integer cantMuniciones;
	
	public BonusMunicion(int puntos, int cantMuniciones, Point posicion, Sprite sprite) {
		super(puntos, posicion, sprite);
		this.cantMuniciones = cantMuniciones;
	}
	
	@Override
	public void consumirBonus(SoldadoJugador soldado) {
		if(cantMuniciones != null) {
			entregarMuniciones(cantMuniciones, soldado);
			cantMuniciones = null;
			super.consumirBonus(soldado);
		}
	}
	
	protected abstract void entregarMuniciones(int cantMuniciones, SoldadoJugador soldado);
	
}

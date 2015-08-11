import java.awt.Point;

public abstract class BonusArma extends BonusMunicion {
	
	private Arma arma;

	public BonusArma(int puntos, Arma arma, int cantMuniciones, Point posicion, Sprite sprite) {
		super(puntos, cantMuniciones, posicion, sprite);
		this.arma = arma;
	}
	
	@Override
	public void consumirBonus(SoldadoJugador soldado) {
		if (arma != null) {
			if( !soldado.getClassArma().equals(arma.getClass()) )
				soldado.setArma(arma);
			arma = null;
			super.consumirBonus(soldado);
		}
	}
	
	@Override
	protected void entregarMuniciones(int cantMuniciones, SoldadoJugador soldado) {
		soldado.recargarArma(cantMuniciones);
	}
	
}

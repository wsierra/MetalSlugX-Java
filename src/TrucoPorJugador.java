public abstract class TrucoPorJugador extends Truco {
	
	private SoldadoJugador soldado;
	
	public TrucoPorJugador(String clave, SoldadoJugador soldado) {
		super(clave + (soldado.getJugador().getNumJugador() + 1));
		this.soldado = soldado;
	}
	
	@Override
	protected final void realizarAccion() {
		realizarAccion(soldado);
	}
	
	protected abstract void realizarAccion(SoldadoJugador soldado);
}

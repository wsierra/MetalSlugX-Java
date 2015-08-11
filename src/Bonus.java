import java.awt.Point;

public abstract class Bonus extends ObjetoDeJuego {
	
	private Integer puntos;
	
	public Bonus(int puntos, Point posicion, Sprite sprite) {
		super(posicion, sprite);
		this.puntos = puntos;
	}
	
	@Override
	public void actualizar() {
		if (puntos == null)
			desactivar();
		else
			verificarDesactivacion();
	}
	
	public void consumirBonus(SoldadoJugador soldado) {
		if(puntos != null) {
			soldado.getJugador().aumentarPuntos(puntos);
			puntos = null;
		}
	}
}


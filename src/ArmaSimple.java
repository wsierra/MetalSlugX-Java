import java.awt.Point;

public abstract class ArmaSimple extends Arma {
	
	private int tiempoRecuperacion;
	private int contadorRecuperacion;	

	public ArmaSimple(Point posicion, int tiempoRecuperacion) {
		super(posicion);
		this.tiempoRecuperacion = tiempoRecuperacion;
	}
	
	@Override
	public void actualizar() {
		if (contadorRecuperacion != 0)
			contadorRecuperacion--;
	}

	@Override
	public void disparar() {
		if (contadorRecuperacion == 0) {
			crearMunicion();
			restarMuniciones(1);
			contadorRecuperacion = tiempoRecuperacion;
		}
	}
	
	public abstract Municion crearMunicion();
}

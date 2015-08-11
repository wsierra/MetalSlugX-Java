import java.awt.Point;

public class ArmaR extends ArmaSimple {
	
	private static final int TIEMPO_RECUPERACION = 15;
	private static final int CANT_MUNICIONES = 15;
	
	public ArmaR() {
		this(new Point());
	}
	
	public ArmaR(Point posicion) {
		super(posicion, TIEMPO_RECUPERACION);
		recargar(CANT_MUNICIONES);
	}

	@Override
	public Municion crearMunicion() {
		return new MunicionR(getPosicion(), getDireccionDisparo(), getPortador(), mundo);
	}
	
	@Override
	public boolean evaluarDireccion(int direccion) {
		return direccion != DIAGONAL; 
	}
}

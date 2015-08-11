import java.awt.Point;

public class ArmaBaseNoDiagonal extends ArmaBase {
	
	private int tiempoRecuperacion = 6;
	private int contadorRecuperacion;
	
	public ArmaBaseNoDiagonal(Point posicion) {
		super(posicion);
		recargar(INFINITO);
	}
	  
	@Override
	public boolean evaluarDireccion(int direccion) {			
		return direccion != Arma.DIAGONAL;
	}
}

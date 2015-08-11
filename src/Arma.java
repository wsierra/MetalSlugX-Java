import java.awt.Point;

public abstract class Arma extends ObjetoDeJuego {

	private ObjetoCombate portador;
	private int cantMuniciones;
	private int direccionDisparo; 		//Municion.<alguna direccion>
	
	public static final int INFINITO = -1;
	
	public static final int HORIZONTAL = 8;
	public static final int VERTICAL = 16;
	public static final int DIAGONAL = 32;
	
	public Arma(Point posicion) {
		super(posicion);
		direccionDisparo = Municion.DERECHA;
	}
	
	protected abstract void disparar();
	
	protected void restarMuniciones(int cantidad) {
		if (cantMuniciones != INFINITO) {
			cantMuniciones -= cantidad;
			if (cantMuniciones < 0) 
				cantMuniciones = 0;
		}
	}
	
	protected int getDireccionDisparo() {
		return direccionDisparo;
	}
	
	public void setDireccionDisparo(int direccion) { 		//Municion.<alguna direccion>
		direccionDisparo = direccion;
	}
	
	public void setPortador(ObjetoCombate portador) {
		this.portador = portador;
	}
	
	protected ObjetoCombate getPortador() {
		return portador;
	}
	
	public void recargar(int cantRecarga) {
		if (cantMuniciones != INFINITO)
			if (cantRecarga == INFINITO)
				cantMuniciones = INFINITO;
			else
				cantMuniciones += cantRecarga;
	}
	
	public int getCantMuniciones() {
		return cantMuniciones;
	}
	
	public boolean isDescargada() {
		return cantMuniciones == 0;
	}
	
	public boolean evaluarDireccion(int direccion) {     //Arma.<alguna direccion>
		return true;
	}
}

public class Trigger {

	private int tiempo;
	private int tiempoRestante;

	public Trigger(int tiempo) {
		set(tiempo);
	}

	public boolean test() {
		if (tiempoRestante-- == 0) {
			reset();
			return true;
		}
		return false;
	}

	public void reset() {
		tiempoRestante = tiempo;
	}

	public void set(int nuevoTiempo) {
		tiempo = nuevoTiempo;
		reset();
	}

	public boolean getEstado() {
		return tiempoRestante == 0;
	}
}

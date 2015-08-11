public class Jugador {

	private int puntaje;
	private int numJugador;
	
	public Jugador(int numJugador) {
		this.numJugador = numJugador;
	}
	
	public int getNumJugador() {
		return numJugador;
	}
	
	public int getPuntaje() {
		return puntaje;
	}
	
	public void aumentarPuntos(int puntos) {
		puntaje += puntos;
	}

}

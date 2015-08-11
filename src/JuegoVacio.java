import java.awt.Dimension;

public class JuegoVacio extends Juego {
	
	@Override
	protected String getParamTitulo() {
		return null;
	}
	
	@Override
	protected Dimension getParamTamanio() {
		return new Dimension(480, 360);
	}
	
	@Override
	protected boolean getParamPantallaCompleta() {
		return false;
	}
	
	@Override
	protected EjecucionJuego getParamEjecucionJuego() {
		return null;
	}
}

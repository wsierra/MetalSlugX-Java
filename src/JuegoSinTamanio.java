import java.awt.Dimension;

public class JuegoSinTamanio extends Juego {
	
	@Override
	protected String getParamTitulo() {
		return null;
	}
	
	@Override
	protected Dimension getParamTamanio() {
		return null;
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

import java.nio.file.Path;
import java.nio.file.Paths;

public class PacMan extends JuegoTontoSinImagen {
	
	@Override
	protected String getParamTitulo() {
		return "Pac-Man";
	}
	
	@Override
	public Path getScreenshot() {
		return Paths.get("imagenes", "PacManScreen.png");
	}
}

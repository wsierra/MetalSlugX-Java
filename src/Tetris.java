import java.nio.file.Path;
import java.nio.file.Paths;

public class Tetris extends JuegoTontoSinImagen {
	
	@Override
	protected String getParamTitulo() {
		return "Tetris";
	}
	
	@Override
	public Path getScreenshot() {
		return Paths.get("imagenes", "TetrisScreen.png");
	}
}

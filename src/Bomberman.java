import java.nio.file.Path;
import java.nio.file.Paths;

public class Bomberman extends JuegoTontoSinImagen {
	
	@Override
	protected String getParamTitulo() {
		return "Bomberman";
	}
	
	@Override
	public Path getScreenshot() {
		return Paths.get("imagenes", "BombermanScreen.png");
	}
}

import java.nio.file.Path;
import java.nio.file.Paths;

public class SuperMarioBros extends JuegoTontoSinImagen {
	
	protected String getParamTitulo() {
		return "Super Mario Bros";
	}
	
	public Path getScreenshot() {
		return Paths.get("imagenes", "SuperMarioBrosScreen.png");
	}
}

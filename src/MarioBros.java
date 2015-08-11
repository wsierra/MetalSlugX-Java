import java.nio.file.Path;
import java.nio.file.Paths;

public class MarioBros extends JuegoTontoSinImagen {
	
	@Override
	protected String getParamTitulo() {
		return "Mario Bros";
	}
	
	@Override
	public Path getScreenshot() {
		return Paths.get("noexite");
	}
}

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Nivel {

	private BufferedImage fondo;
	private int tiempoMaximo;
	private Map<Integer, List<ObjetoDeJuego>> disposicion;

	public Nivel() {
		 disposicion = new HashMap<Integer, List<ObjetoDeJuego>>();
	}

	public abstract void cargar();
	
	public abstract int getSuelo(int x);
	
	public final void reiniciar() {
		liberarRecursos();
		cargar();
	}

	public final void liberarRecursos() {
		fondo = null;
		disposicion.clear();
	}

	public final BufferedImage getFondo() {
		return fondo;
	}
	
	public final int getAncho() {
		return fondo.getWidth();
	}
	
	public final int getTiempoMaximo() {
		return tiempoMaximo;
	}
	
	public final void activar(int posCamaraInf, int posCamaraSup, Partida mundo) {
		for (int pos = posCamaraInf; pos < posCamaraSup; pos++) {
			List<ObjetoDeJuego> listaActivar = disposicion.get(pos);
			if (listaActivar != null)
				for (int i = 0; i < listaActivar.size(); i++)
					listaActivar.get(i).activar(mundo);
		}
	}

	protected void setFondo(Path rutaFondo) {
		try {
			fondo = ImageIO.read(rutaFondo.toFile());
		}
		catch (IOException ex) {}
	}

	protected void setTiempoMaximo(int segundos) {
		tiempoMaximo = segundos;
	}

	protected void add(int posCamara, ObjetoDeJuego obj) {
		List<ObjetoDeJuego> lista;

		if ( disposicion.containsKey(posCamara) )
			lista = disposicion.get(posCamara);
		else
			disposicion.put( posCamara, lista = new ArrayList<ObjetoDeJuego>(1) );

		lista.add(obj);
	}
}

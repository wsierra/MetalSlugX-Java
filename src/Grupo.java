import java.awt.Point;
import java.util.Vector;

public abstract class Grupo extends ObjetoDeJuego {
	
	protected Vector<ObjetoDeJuego> miembros = new Vector<ObjetoDeJuego>();
	
	public Grupo(Point posicion) {
		super(posicion);
	}
	
	public Grupo(Point posicion, Sprite sprite) {
		super(posicion, sprite);
	}
	
	protected boolean quedanActivos() {
		for (ObjetoDeJuego o : miembros)
			if (o.isActivo())
				return true;
				
		return false;
	}
	
	protected boolean quedanVivos() {
		for (ObjetoDeJuego o : miembros)
			if (o instanceof ObjetoCombate && ((ObjetoCombate)o).isVivo())
				return true;
				
		return false;
	}
}

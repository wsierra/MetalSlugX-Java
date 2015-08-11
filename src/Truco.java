import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class Truco extends KeyAdapter{
	
	private final String clave;
	private int posicion;
	
	public Truco(String clave) {
		this.clave = clave;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ( KeyEvent.getExtendedKeyCodeForChar(clave.codePointAt(posicion)) == e.getKeyCode() ) {
			if (++posicion == clave.length()) {
				realizarAccion();
				posicion = 0;
			}
		}
		else
			posicion = 0;
	}
	
	protected abstract void realizarAccion();
}

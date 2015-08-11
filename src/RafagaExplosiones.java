import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

public class RafagaExplosiones {
	
	private RandomTrigger trigger = new RandomTrigger(3, 10);
	
	private int explosionesRestantes;
	private final int cantExpFinales;
	private final Random random;
	private final Dimension tamanio;
	
	public RafagaExplosiones(Dimension tamanio, int cantExplosiones, int cantExpFinales) {
		explosionesRestantes = cantExplosiones;
		this.cantExpFinales = cantExpFinales;
		this.tamanio = tamanio;
		random = new Random();
	}
	
	public RafagaExplosiones(Dimension tamanio, int cantExplosiones) {
		this(tamanio, cantExplosiones, 0);
	}
	
	public boolean actualizar(Point posicion, Partida mundo) {
		if (trigger != null && trigger.test())
			if (explosionesRestantes > cantExpFinales)
				nuevaExplosion(posicion, mundo);
			else {
				while (explosionesRestantes != 0)
					nuevaExplosion(posicion, mundo);
				trigger = null;
			}		
		return trigger == null;
	}
	
	private void nuevaExplosion(Point posicion, Partida mundo) {
		ObjetoDeJuego exp = new Explosion(posicion, mundo);
		exp.desplazar(random.nextInt(tamanio.width), random.nextInt(tamanio.height));
		explosionesRestantes--;
	}
}

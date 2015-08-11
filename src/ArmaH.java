import java.awt.Point;
import javafx.scene.media.Media;

public class ArmaH extends Arma {
	
	private static final int CANT_MUNICIONES = 50;
	private static final int CANT_DISPAROS = 4;
	private static final int TIEMPO_RECUPERACION = 6;
	private static final int MAX_VARIACION = 25;
	
	static {
		MetalSlugT.sfx.cargar(MetalSlugT.DIR_SFX.resolve("arma_h.wav"), MetalSlugT.MAXJUGADORES + 1);
	}
	
	private int contadorRecuperacion;
	private int disparosPendientes;
	
	public ArmaH() {
		this(new Point());			
	}
	
	public ArmaH(Point posicion) {
		super(posicion);
		recargar(CANT_MUNICIONES);			
	}
	
	@Override
	public void actualizar() {
		if (contadorRecuperacion != 0)
			contadorRecuperacion--;
		else if (disparosPendientes != 0 && !isDescargada()) {
			disparosPendientes--;
			int desviacion = (int)(Math.random() * MAX_VARIACION * 2) - MAX_VARIACION;
			new MunicionH(getPosicion(), getDireccionDisparo(), desviacion, getPortador(), mundo);
			restarMuniciones(1);
			contadorRecuperacion = TIEMPO_RECUPERACION;
		}	
	}

	@Override
	public void disparar() {
		if (disparosPendientes == 0) {
			disparosPendientes = CANT_DISPAROS;
			MetalSlugT.sfx.reproducir(MetalSlugT.DIR_SFX.resolve("arma_h.wav"));
		}
	}
	
	//~private void posicionarMunicion(MunicionH m) {
		//~int desp = (int)(Math.random() * MAX_VARIACION * 2) - MAX_VARIACION;
		//~int direccion = getDireccionDisparo();
		//~
		//~if (direccion == Municion.DIAG_IZQUIERDA)
			//~m.desplazar(0, -desp);
		//~else if (direccion != Municion.ARRIBA)
			//~m.desplazar(0, desp);
			//~
		//~if ( (direccion & Municion.ARRIBA) != 0 )
			//~m.desplazar(desp, 0);		
	//~}
}

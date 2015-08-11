import java.awt.Point;
import java.awt.Dimension;

public abstract class Helicoptero extends ObjetoCombate implements Enemigo {
		
	protected static final String VUELO = "vuelo";
	
	private static final float RAPIDEZ_DESCENSO = 2.0f;
	private static final float ATENUACION_CAIDA = 30.0f;
	private static final float ACELERACION = 0.015f;
	
	private static final int TOTAL_EXPLOSIONES = 20;
	private static final int EXPLOSIONES_FINALES = 5;
	
	private final RafagaExplosiones destruccion;
	
	public Helicoptero(int energia, Point centroMovimiento, int radioX, int radioY, Sprite sprite) {
		super(energia, new Point(centroMovimiento.x + radioX, 0), sprite);
		desplazar(-getAncho() / 2, -2 * getAlto());
		setVulnerable(true);
		
		destruccion = new RafagaExplosiones(getTamanio(), TOTAL_EXPLOSIONES, EXPLOSIONES_FINALES);
		
		iniciarMovimientoX(new Movimiento() {
			@Override public float calcular(int t) {
				return radioX * (float)Math.cos(ACELERACION * t);
			}
		});
		
		Movimiento transicion = new Movimiento() {
			@Override public float calcular(int t) {
				if (getPosicion().x + getAncho() / 2 <= centroMovimiento.x - radioX)
					iniciarMovimientoY(new Movimiento() {
						@Override public float calcular(int t) {
							return -radioY * (float)Math.sin(ACELERACION * t);
						}
					});
				return 0;
			}
		};
		
		iniciarMovimientoY(new Movimiento() {
			@Override public float calcular(int t) {
				if (getPosicion().y + getAlto() / 2 < centroMovimiento.y)
					return t * RAPIDEZ_DESCENSO;
				else {
					if (radioY == 0)
						iniciarMovimientoY(null);
					else
						iniciarMovimientoY(transicion);
					return (t - 1) * RAPIDEZ_DESCENSO;
				}
			}
		});
	}
	
	@Override
	public void actualizar() {
		
		mover();
		actualizarSecuencia(VUELO, AdminSprite.NINGUNO);
				
		if (isVivo()) 
			atacar();
		else if (destruccion.actualizar(getPosicion(), mundo))
			desactivar();		
	}
	
	protected abstract void atacar();
	
	@Override
	protected void notificacionMuerte() {
		iniciarMovimientoY(new Caida(0.0f) {
			@Override public float calcular(int t) {
				return super.calcular(t) / ATENUACION_CAIDA;
			}
		});
	}
}

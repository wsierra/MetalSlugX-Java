import java.awt.Point;

public abstract class ObjetoMovil extends ObjetoDeJuego {
	
	private Movimiento movX, movY;
	private MovLineal defaultMovX, defaultMovY;
	private int tX, tY;
	private float xRelativo, yRelativo;
	
	private Movimiento pendienteX, pendienteY;
	
	public ObjetoMovil(Point posicion) {
		this(posicion, null);
	}
	
	public ObjetoMovil(Point posicion, Sprite sprite) {
		super(posicion, sprite);
		defaultMovX = new MovLineal();
		defaultMovY = new MovLineal();
		iniciarMovimientoX(defaultMovX);
		iniciarMovimientoY(defaultMovY);
	}
		
	protected void setVelocidadX(float vel){
		if (movX == defaultMovX && vel != defaultMovX.velocidad)
			iniciarMovimientoX(defaultMovX);
		defaultMovX.velocidad = vel;
	};
	
	protected void setVelocidadY(float vel){
		if (movY == defaultMovY && vel != defaultMovY.velocidad)
			iniciarMovimientoY(defaultMovY);
		defaultMovY.velocidad = vel;
	};
	
	protected final void mover() {
		if (this instanceof NoMovible)
			throw new NoMovibleException("Se ha invocado el método mover() en la clase " + getClass().getName() + " que implementa NoMovible");
		
		if (pendienteX != null)
			iniciarX();
		if (pendienteY != null)
			iniciarY();
		
		int intUltimoX = (int)Math.floor(xRelativo);
		int intUltimoY = (int)Math.floor(yRelativo);
		xRelativo = movX.calcular(tX++);
		yRelativo = movY.calcular(tY++);
		
		desplazar( (int)Math.floor(xRelativo - intUltimoX), (int)Math.floor(yRelativo - intUltimoY) );
	}
		
	protected void iniciarMovimientoX(Movimiento mov) {
		pendienteX = mov != null ? mov : defaultMovX;
	}
		
	protected void iniciarMovimientoY(Movimiento mov) {
		pendienteY = mov != null ? mov : defaultMovY;
	}
	
	protected void iniciarX() {
		movX = pendienteX;
		pendienteX = null;
		xRelativo = movX.calcular(0);
		tX = 1;
	}
		
	protected void iniciarY() {
		movY = pendienteY;
		pendienteY = null;
		yRelativo = movY.calcular(0);
		tY = 1;
	}
	
	private static class NoMovibleException extends RuntimeException {	
			
		private NoMovibleException(String message) {
			super(message);
		}	
	}
	
	protected interface NoMovible {}
	
	protected interface Movimiento {
		float calcular(int t);
	}
	
	private static class MovLineal implements Movimiento {
		
		private float velocidad;
		
		@Override
		public float calcular(int t) {
			return velocidad * t;
		}
	}
	
	protected class Caida implements Movimiento {

		private final float impulso;

		public Caida(float impulso) {
			this.impulso = impulso;
		}

		@Override
		public float calcular(int t) {
			return impulso * t + mundo.getGravedad() * t * t / 2.0f;
		}
	}
	
	/*public static void main(String[] a) {
		Juego j = new JuegoVacio() {
			
			ObjetoMovil om = new ObjetoMovil(new java.awt.Point(50,100)) {
				{
					setVelocidadX(0.5f);
				//	setVelocidadY(0.01f);
				}
				public void actualizar() {
					mover();
				}
			};
			
			ObjetoMovil om2 = new ObjetoMovil(new java.awt.Point(350,120)) {
				{
					setVelocidadX(0.02f);
					//setVelocidadY(0.02f);
				}
				public void actualizar() {
					mover();
				}
			};
			
			ObjetoMovil om3 = new ObjetoMovil(new java.awt.Point(50,140)) {
				{
					setVelocidadX(1.0f);
					//setVelocidadY(1.0f);
				}
				public void actualizar() {
					mover();
				}
			};
			
			class Planeta extends ObjetoMovil {
				
				public Planeta(float velAng){
					
					super(new java.awt.Point(200,160));
					setVelocidadX(1.1f);
					//setVelocidadY(1.1f);
					this.velAng = velAng;
					color = new java.awt.Color((int)(Math.random() * 256),(int)(Math.random() * 256),(int)(Math.random() * 256));
				}
				
				private float velAng;
				private java.awt.Color color;
				public void actualizar() {
					mover();
					if (getPosicion().x >= 350 && !inicio) {
						inicio = true;
						iniciarMovimientoX(new Movimiento() {
							@Override
							public float calcular(int t) {
								return 150 * (float)Math.cos(velAng * t);
							}
						});
						iniciarMovimientoY(new Movimiento() {
							@Override
							public float calcular(int t) {
								return -150 * (float)Math.sin(velAng/3.0* t);
							}
						});	
					}
				
				}
				
				boolean inicio;
			}

			Planeta[] ps = new Planeta[50];
			
			{
				for (int i = 0; i < ps.length; i++) 
					ps[i] = new Planeta((i+1) / 500.0f);
			}
			@Override
			protected EjecucionJuego getParamEjecucionJuego() {
				return new EjecucionJuego( new Bloque() {
					@Override
					public void actualizar() {
						//~ om.actualizar();
						//~ om2.actualizar();
						//~ om3.actualizar();
						//~ om4.actualizar();
						//~ om5.actualizar();
						for (Planeta p : ps)
							p.actualizar();
						
						
					}
					@Override
					public void pintar(java.awt.Graphics2D g) {
						
						
						g.setColor(java.awt.Color.WHITE);
						g.drawOval(50, 10 , 299, 299);
						//~ g.fillRect(om.getPosicion().x, om.getPosicion().y, 10, 10);
						//~ g.fillRect(om2.getPosicion().x, om2.getPosicion().y, 10, 10);
						//~ g.fillRect(om3.getPosicion().x, om3.getPosicion().y, 10, 10);
						
						for (Planeta p : ps) {
							g.setColor(p.color);
							g.fillOval(p.getPosicion().x-5, p.getPosicion().y-5, 10, 10);
						}
						//System.out.println(om4.getPosicion().x + " " +  om4.getPosicion().y);
						
					}
					public void inicializar(){}
					public void finalizar(){}
					
				}){
					public void inicializar(){}
					public void finalizar(){}
					
					
					
				};
			}
			
		};
		try {
			j.correr(null);
		}
		catch (Juego.EjecucionException e) {}
		
	}*/
}

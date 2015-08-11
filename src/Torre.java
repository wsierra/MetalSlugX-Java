import java.awt.Point;
import java.awt.Dimension;

public class Torre extends ObjetoCombate implements NoMovible, Enemigo {

		private static final int PUNTOS = 300;
		private static final int ENERGIA = 50;
		
		private static final Point CENTRO_BASE = new Point(27, 50);
		private static final Point MIRA = new Point(37, 22);
		
		private static final int RADIO_EXPLOSIONES = 60;
		private static final int TOTAL_EXPLOSIONES = 10;
		
		private final RandomTrigger triggerExplosiones = new RandomTrigger(3, 10);	
		private final RandomTrigger triggerDisparo = new RandomTrigger(180, 250);	
		
		private static final Sprite spriteClase;
		static {
			spriteClase = new Sprite(DIR_SPRITES.resolve("torre.gif"), new Dimension(63, 49)); 

			spriteClase.addFilaElementos(0, 180, 140, 62, 75, 5);

			spriteClase.addCuadroSecuencia("cerrado", 0, 0, -1);
			spriteClase.addCuadroSecuencia("abriendo", 0, 1, 10);
			spriteClase.addCuadroSecuencia("abriendo", 0, 2, 10);
			spriteClase.addCuadroSecuencia("listo", 0, 3, -1);
			spriteClase.addCuadroSecuencia("destruida", 0, 4, -1);
		}

		private String secuencia;
		private int contExplosiones;
		
		public Torre(Point centroBase) {
			super(ENERGIA, new Point(centroBase.x - CENTRO_BASE.x, centroBase.y - CENTRO_BASE.y), spriteClase);
			secuencia = "cerrado";
		}

		@Override
		public void actualizar() {

			boolean finSec = actualizarSecuencia(secuencia, AdminSprite.NINGUNO);
			
			if (finSec && secuencia.equals("abriendo")) {
				secuencia =  "listo";
				setVulnerable(true);
			}
			else if (secuencia.equals("listo")) {
				if (triggerDisparo.test())
					disparar();
			}
			else if (secuencia.equals("destruida"))
				if (contExplosiones < TOTAL_EXPLOSIONES && triggerExplosiones.test()) {
					contExplosiones++;
					Point posExp = getPosicion();
					posExp.translate( (int)(2 * RADIO_EXPLOSIONES * Math.random()) - RADIO_EXPLOSIONES + getAncho() / 2,
									  (int)(2 * RADIO_EXPLOSIONES * Math.random()) - RADIO_EXPLOSIONES + getAlto() / 2 );
					new Explosion(posExp, mundo);
				}
		}
		
		public void iniciarAtaque() {
			secuencia = "abriendo";
		}
		
		public void disparar() {
			Point mira = getPosicion();
			mira.translate(MIRA.x, MIRA.y);
			Point objetivo = mundo.getPosicionSJMasCercano(mira);
			double anguloDisparo = Math.toDegrees( Math.atan2(objetivo.x - mira.x, objetivo.y - mira.y) ); 
			new CoheteTorre(mira, (float)anguloDisparo, this, mundo);
		}
		
		@Override
		protected void notificacionMuerte() {
			secuencia  = "destruida";
		}
		
		@Override
		public int getPuntos() {
			return PUNTOS;
		}
	}

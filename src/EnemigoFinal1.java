import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Random;

public class EnemigoFinal1 extends ObjetoCombate implements Enemigo {
	
	private static final int PUNTOS = 500;
	private static final int ENERGIA = 750;
	private static final float RAPIDEZ = 2.2f;
	
	private static final int DIST_SJ_MIN = 200;
	private static final int DIST_SJ_MAX = 325;
	private static final int DIST_GRANADAS = 60;
	private static final int PUNTO_CAMBIO_LIMITES = 155;
	private static final int MINIMO_VISIBLE = 60;	
	
	private static final int RECU_GRANADAS = 100;	
	private static final int MAX_SOLDADOS = 5;
	private static final int TOTAL_EXP = 50;
	private static final int EXP_FINALES = 10;
	private static final int ESPERA_FINAL = 200;
	
	private static final Point MIRA_CP = new Point(12, 22);
	private static final Point MIRA_CCI = new Point(8, 51);
	private static final Point MIRA_CCD = new Point(43, 55);
	
	private RandomTrigger triggerCP = new RandomTrigger(350, 450);
	private RandomTrigger triggerCCI = new RandomTrigger(200, 250);
	private RandomTrigger triggerCCD = new RandomTrigger(200, 250);
	private RandomTrigger triggerSoldados = new RandomTrigger(600, 900);
	private Trigger esperaFinal;
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("enemigo_final_1.gif"), new Dimension(223, 134));
		spriteClase.addFilaElementos(0, 286, 138, 52, 4, 2);
		spriteClase.addFilaElementos(138, 286, 138, 52, 4, 3);
		spriteClase.addFilaElementos(138*2, 286, 138, 52, 4, 1);
		
		spriteClase.addCuadroSecuencia("normal", 0, 0, 5);
		spriteClase.addCuadroSecuencia("normal", 0, 1, 5);
			
		spriteClase.addCuadroSecuencia("cañonPrincipal", 1, 0, 15);
		spriteClase.addCuadroSecuencia("cañonChicoIzq", 1, 1, 10);
		spriteClase.addCuadroSecuencia("cañonChicoDer", 1, 2, 10);
		
		spriteClase.addCuadroSecuencia("destruido", 2, 0, -1);
	}
			
	private String secuenciaDisparo;
	private boolean entradaCompleta;
	private int recuGranadas;
	private RafagaExplosiones explosiones;
	
	private Random random = new Random();
	
	public EnemigoFinal1(int posicionX) {
		super(ENERGIA, new Point(posicionX, 0), spriteClase);
		setVulnerable(true);
		setIniciarEnSuelo(true);
		setVelocidadX(-RAPIDEZ);
	}

	@Override
	public void actualizar() {
		
		if (isVivo()) {
			if (secuenciaDisparo == null) {
				actualizarSecuencia("normal", AdminSprite.NINGUNO);
				
				if (triggerCP.test()) {
					secuenciaDisparo = "cañonPrincipal";
					lanzarMisil();
				}
				else if (triggerCCI.test()) {
					secuenciaDisparo = "cañonChicoIzq";
					disparar(MIRA_CCI);
				}
				else if (triggerCCD.test()) {
					secuenciaDisparo = "cañonChicoDer";
					disparar(MIRA_CCD);
				}
			}
			else if (actualizarSecuencia(secuenciaDisparo, AdminSprite.NINGUNO))
				secuenciaDisparo = null;
			
			if (entradaCompleta) {
				Point pos = getPosicion();
				int distSJ = pos.x - mundo.getPosicionSJMasCercano(pos).x;
				
				if (distSJ > getLimite(DIST_SJ_MAX))
					setVelocidadX(-RAPIDEZ);
				else if ( distSJ < getLimite(DIST_SJ_MIN) && mundo.getCamara().contains(pos.x + MINIMO_VISIBLE, pos.y) )
					setVelocidadX(RAPIDEZ);
				else
					setVelocidadX(0.0f);
					
				if (recuGranadas-- <= 0 && distSJ < DIST_GRANADAS)
					lanzarGranadas();
			}
			else if ( mundo.getCamara().contains(getLadoDerecho(), getLadoSuperior()) )
				entradaCompleta = true;
			
			if (triggerSoldados.test())
				crearSoldados(random.nextInt(MAX_SOLDADOS) + 1);
						
			mover();
		}
		else if (esperaFinal == null) {
			actualizarSecuencia("normal", AdminSprite.NINGUNO);
			if (explosiones.actualizar(getPosicion(), mundo)) {
				esperaFinal = new Trigger(ESPERA_FINAL);
				crearFuego();
			}
		}
		else {
			actualizarSecuencia("destruido", AdminSprite.NINGUNO);
			if (esperaFinal.test())
				mundo.nivelFinalizado();
		}
	
	}
	
	private void disparar(Point posCañon) {
		new MuniEnemigoFinal1(getPosMira(posCañon), this, mundo);
	}
	
	private void lanzarMisil() {
		new MisilEnemigoFinal1(getPosMira(MIRA_CP), this, mundo);
	}
	
	private void lanzarGranadas() {
		recuGranadas = RECU_GRANADAS;
		new GranadaEnemigoFinal1(getPosMira(MIRA_CP), 93.6f, 21.0f, this, mundo);
		new GranadaEnemigoFinal1(getPosMira(MIRA_CCI), 91.5f, 22.0f, this, mundo);
		new GranadaEnemigoFinal1(getPosMira(MIRA_CCD), 91.5f, 22.5f, this, mundo);
	}

	private Point getPosMira(Point mira) {
		Point posMira = getPosicion();
		posMira.translate(mira.x, mira.y);
		return posMira;
	}
		
	private void crearSoldados(int cantSoldados) {
		Rectangle cam = mundo.getCamara();
		while (cantSoldados-- > 0) {
			Point posicion;
			int variacion = random.nextInt(200);
			if (random.nextBoolean())
				posicion = new Point(cam.x - 120 - variacion , getLadoSuperior());
			else
				posicion = new Point(cam.x + cam.width + 60 + variacion , getLadoSuperior());
			
			new SoldadoEnemigo(posicion).activar(mundo);
		}
	}
	
	private void crearFuego() {
		int cant = random.nextInt(4) + 1;
		while (cant-- > 0)
			new Fuego(new Point( getLadoIzquierdo() + random.nextInt(getAncho()),
							     getLadoSuperior() + getAlto() / 2 + random.nextInt(getAlto() / 2) ), mundo);
	}
	
	private int getLimite(int lim) {
		if ( !mundo.getCamara().contains(getLadoIzquierdo() + PUNTO_CAMBIO_LIMITES, getLadoSuperior()) )
			return lim / 2;
		else
			return lim;
	}
	
	@Override
	protected void notificacionMuerte() {
		secuenciaDisparo = null;
		explosiones = new RafagaExplosiones(new Dimension(getAncho(), getAlto()), TOTAL_EXP, EXP_FINALES);
	}
	
	@Override
	public int getPuntos() {
		return PUNTOS;
	}
}

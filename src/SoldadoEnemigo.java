import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

public class SoldadoEnemigo extends Soldado implements Enemigo {

	private static final int PUNTOS = 50;

	private static final int ENERGIA = 1;
	private static final float RAPIDEZ = 1.0f;
	private static final float IMPULSO = -15.0f;
	private static final int CANT_GRANADAS = 50;
	private static final float ANGULO_GRANADA = 75.0f;
	private static final float IMPULSO_GRANADA = 17.0f;
	
	/*-----------intervalo de validez------------*/
	private static final int EXTREMO_INTERNO = 50;
	private static final int EXTREMO_EXTERNO = 150;
	private static final int DESP_MAXIMO = 120;
		
	private static final RandomTrigger triggerBonus = new RandomTrigger(5, 15);
	private static final String[] TIPOS_BONUS = {BonusFactory.BANANAS, BonusFactory.FRUTAS,
											     BonusFactory.JOYA_ROJA, BonusFactory.JOYA_AZUL};

	private RandomTrigger triggerIntervalo = new RandomTrigger(100, 200);
	private RandomTrigger triggerGranada = new RandomTrigger(150, 300);
	private RandomTrigger triggerSalto = new RandomTrigger(150, 500);
	private RandomTrigger triggerCruce = new RandomTrigger(300, 800);
	private RandomTrigger triggerFinCruce = new RandomTrigger(200, 300);
	private RandomTrigger triggerDisparo = new RandomTrigger(200, 600);
	private RandomTrigger triggerDetener = new RandomTrigger(100, 120);
	private RandomTrigger triggerAvanzar = new RandomTrigger(100, 200);

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite( DIR_SPRITES.resolve("soldado_enemigo.gif"), new Dimension(41, 75) );

		spriteClase.addFilaElementos(0, 72, 76, 7, 1, 1);
		spriteClase.addFilaElementos(76, 52, 78, 5, 3, 4);
		spriteClase.addFilaElementos(154, 80, 76, 18, 1, 3);
		spriteClase.addFilaElementos(230, 88, 79, 42, 4, 4);

		spriteClase.addCuadroSecuencia(PARADO, 0, 0, -1);

		spriteClase.addCuadroSecuencia(SALTANDO, 1, 3, -1);

		spriteClase.addCuadroSecuencia(AVANZANDO, 1, 0, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 1, 1, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 1, 2, 7);
		spriteClase.addCuadroSecuencia(AVANZANDO, 1, 3, 7);

		spriteClase.addCuadroSecuencia(GRANADA, 2, 0, 10);
		spriteClase.addCuadroSecuencia(GRANADA, 2, 1, 10);
		spriteClase.addCuadroSecuencia(GRANADA, 2, 2, 10);

		spriteClase.addCuadroSecuencia(MURIENDO, 3, 0, 10);
		spriteClase.addCuadroSecuencia(MURIENDO, 3, 1, 10);
		spriteClase.addCuadroSecuencia(MURIENDO, 3, 2, 10);
		spriteClase.addCuadroSecuencia(MURIENDO, 3, 3, 30);
	}

	private Random random;
	private boolean cruzando;
	private int despIntervalo;
	private int tiempoReubicacion;
	private int contReubicacion;
	private boolean detenido;

	public SoldadoEnemigo() {
		this(new Point());
	}

	public SoldadoEnemigo(Point posicion) {
		super(ENERGIA, posicion, spriteClase);
		setRapidez(RAPIDEZ);
		setImpulsoSalto(IMPULSO);
		setCantGranadas(CANT_GRANADAS);
		setAnguloGranada(ANGULO_GRANADA);
		setImpulsoGranada(IMPULSO_GRANADA);
		setMiras(new Point(40, 14), null, null);
		random = new Random();
		calcularDespIntervalo();
	}
	//~
	//~public void avanzarIzquierda(){}
	//~public void avanzarDerecha(){}
	
	
	@Override
	public void actualizar() {


		if (cruzando) {
			DebugInfo.addLinea(toString() + "  ----------------------------- CRUCE");
			cruzando = !triggerFinCruce.test();
		}
		else if ( mundo.entreDosJugadores(getPosicion()) ) {
				//~ if (triggerDetener.test()) {
					//~ detener();
					//~ if (!enAire())
						//~ disparar();
				//~ }
				//~ else {

					if (triggerAvanzar.test())
						if (random.nextInt(2) == 0)
							avanzarDerecha();
						else
							avanzarIzquierda();
					if (triggerDetener.test()) {
						detener();
						detenido = true;
					}

				//~ }
				//~ switch (random.nextInt(120)) {
					//~ case 0 : avanzarDerecha(); break;
					//~ case 1 : avanzarIzquierda(); break;
					//~ case 3 : detener();
							 //~ detenido = 15;
							 //~ break;
				//~ }
		}
		else {
			int distancia = getPosicion().x - mundo.getPosicionSJMasCercano(getPosicion()).x;
			boolean invertido = distancia < 0;

			if (triggerCruce.test()) {
				cruzando = true;
				acercarse(invertido);
			}
			else {DebugInfo.addLinea(toString() + "  ----------------------------- NORMAL");
				if (invertido)
					distancia = -distancia;

				if (distancia < EXTREMO_INTERNO + despIntervalo)
					alejarse(invertido);
				else {
					acercarse(invertido);
					if (distancia < EXTREMO_EXTERNO + despIntervalo) {
						detener();
						detenido = true;
					}
				}
			}
		}

		if (detenido && triggerDisparo.test())
			disparar();
		if (triggerIntervalo.test())
			calcularDespIntervalo();
		if (triggerGranada.test())
			lanzarGranada();
		if (triggerSalto.test())
			saltar();
		
		super.actualizar();
	}

	@Override
	protected void notificacionMuerte() {
		
		if (triggerBonus.test()) {
			Bonus bonus = BonusFactory.newBonusPuntos( TIPOS_BONUS[random.nextInt(TIPOS_BONUS.length)] );
			bonus.desplazar( getLadoIzquierdo() - bonus.getLadoIzquierdo(),
							 getLadoInferior() - bonus.getLadoInferior() );
			bonus.activar(mundo);
		}
		
		super.notificacionMuerte();
	}

	private void calcularDespIntervalo() {
		despIntervalo = random.nextInt(DESP_MAXIMO + 1);
	}

	private void acercarse(boolean invertido) {
		alejarse(!invertido);
	}

	private void alejarse(boolean invertido) {
		if (!invertido)
			avanzarDerecha();
		else
			avanzarIzquierda();
	}
		
	@Override
	public int getPuntos() {
		return PUNTOS;
	}
}

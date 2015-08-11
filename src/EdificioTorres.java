import java.awt.Point;
import java.awt.Dimension;

public class EdificioTorres extends Grupo {
	
	private static final int VUELO_IZQUIERDO = 131;
	private static final int[] POS_TORRES_X = {129, 315, 497};
	private static final int[] POS_TORRES_Y = {-256, -256, -252};
	
	private static final int ALTURA = 200;
	private static final int TOTAL_EXP = 50;
	private static final int EXP_FINALES = 30;
		
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("edificio_torres.gif"), new Dimension(610, 1));
		spriteClase.addFilaElementos(new Sprite.Elemento(0, 0, 778, 318, 142, 279));
		spriteClase.addFilaElementos(new Sprite.Elemento(0, 318, 778, 318, 142, 279));
		spriteClase.addCuadroSecuencia("normal", 0, 0, -1);
		spriteClase.addCuadroSecuencia("destruido", 1, 0, -1);
	}
	
	private boolean comenzoAtaque;
	private RafagaExplosiones destruccion;
	private String secuencia;
	
	public EdificioTorres(Point posicion) {
		super(posicion, spriteClase);
		secuencia = "normal";
		for (int i = 0; i < 3; i++)
			miembros.add(new Torre(new Point( posicion.x + POS_TORRES_X[i],
											  posicion.y + POS_TORRES_Y[i] )));
	}	
	
	@Override
	public void actualizar() {
		
		actualizarSecuencia(secuencia, AdminSprite.NINGUNO);
		
		if (!comenzoAtaque && mundo.getCamara().x >= getLadoIzquierdo()) {
			comenzoAtaque = true;
			mundo.setAvanceCamara(false);
			for (ObjetoDeJuego o : miembros)
				((Torre)o).iniciarAtaque();
				
			((Torre)miembros.get(1)).disparar();
		}
		else if (destruccion != null) {
			if (destruccion.actualizar(getPosDestruccion(), mundo)) {
				destruccion = null;
				mundo.setAvanceCamara(true);
				secuencia = "destruido";
			}
		}
		else
			verificarDesactivacion();
	}
		
	@Override
	public void activar(Partida mundo) {
		super.activar(mundo);
		for (ObjetoDeJuego o : miembros)
			o.activar(mundo);
	}
	
	@Override
	public void desactivar() {
		super.desactivar();
		for (ObjetoDeJuego o : miembros)
			o.desactivar();
	}
	
	private void torreDestruida() {
		if (!quedanVivos()) 
			destruccion = new RafagaExplosiones(new Dimension(getAncho(), ALTURA), TOTAL_EXP, EXP_FINALES);
	}
	
	private Point getPosDestruccion() {
		Point pos = getPosicion();
		pos.translate(0, -ALTURA);
		return pos;
	}
	
	public static int getXActivacion(int posicionX) {
		int x =	posicionX - VUELO_IZQUIERDO * 3 / 2 - MetalSlugT.getAncho();
		return x > 0 ? x : 0;
	}
	

	/*-----------------------Torre----------------------*/
	
	private static final Sprite spriteTorre;
	static {
		spriteTorre = new Sprite(DIR_SPRITES.resolve("torre.gif"), new Dimension(63, 49)); 

		spriteTorre.addFilaElementos(0, 180, 140, 62, 75, 5);

		spriteTorre.addCuadroSecuencia("cerrado", 0, 0, -1);
		spriteTorre.addCuadroSecuencia("abriendo", 0, 1, 10);
		spriteTorre.addCuadroSecuencia("abriendo", 0, 2, 10);
		spriteTorre.addCuadroSecuencia("listo", 0, 3, -1);
		spriteTorre.addCuadroSecuencia("destruida", 0, 4, -1);
	}
	
	private class Torre extends ObjetoCombate implements NoMovible, Enemigo {

		private static final int PUNTOS = 200;
		private static final int ENERGIA = 50;
		
		private static final int CENTRO_BASE_X = 27;
		private static final int CENTRO_BASE_Y = 50;
		private static final int MIRA_X = 37;
		private static final int MIRA_Y = 22;
		
		private static final int RADIO_EXP = 60;
		private static final int TOTAL_EXP = 10;
		
		private final RandomTrigger triggerDisparo = new RandomTrigger(180, 250);	
	
		private RafagaExplosiones destruccion;
		private String secuencia;
		
		public Torre(Point centroBase) {
			super(ENERGIA, new Point(centroBase.x - CENTRO_BASE_X, centroBase.y - CENTRO_BASE_Y), spriteTorre);
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
			else if (destruccion != null && destruccion.actualizar(getPosDestruccion(), mundo))
				destruccion = null;
		}
		
		public void iniciarAtaque() {
			secuencia = "abriendo";
		}
		
		public void disparar() {
			Point mira = getPosicion();
			mira.translate(MIRA_X, MIRA_Y);
			Point objetivo = mundo.getPosicionSJMasCercano(mira);
			double anguloDisparo = Math.toDegrees( Math.atan2(objetivo.x - mira.x, objetivo.y - mira.y) ); 
			new CoheteTorre(mira, (float)anguloDisparo, this, mundo);
		}
		
		@Override
		protected void notificacionMuerte() {
			secuencia  = "destruida";
			destruccion = new RafagaExplosiones(new Dimension(2 * RADIO_EXP, 2 * RADIO_EXP), TOTAL_EXP);
			torreDestruida();
		}
		
		@Override
		public int getPuntos() {
			return PUNTOS;
		}
		
		private Point getPosDestruccion() {
			Point pos = getPosicion();
			pos.translate(getAncho() / 2 - RADIO_EXP, getAlto() / 2 - RADIO_EXP);
			return pos;
		}
	}
}

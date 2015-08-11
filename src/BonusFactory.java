import java.awt.Point;
import java.awt.Dimension;
import java.util.HashMap;

public class BonusFactory {
	
	public static final String ARMA_H = "armaH";
	public static final String ARMA_R = "armaR";
	public static final String ARMA_S = "armaS";
	
	public static final String BANANAS = "bananas";
	public static final String FRUTAS = "frutas";
	public static final String JOYA_ROJA = "joyaR";
	public static final String JOYA_AZUL = "joyaA";
	
	private static final String GRANADAS = "granadas";
	private static final String TIEMPO = "tiempo";
	
	private static final HashMap<String, Integer> tablaPuntos = new HashMap<String, Integer>();
	private static final HashMap<String, Integer> tablaMuniciones = new HashMap<String, Integer>();
	static {
		tablaPuntos.put(ARMA_H, 45);
		tablaPuntos.put(ARMA_R, 75);
		tablaPuntos.put(ARMA_S, 60);
		tablaPuntos.put(GRANADAS, 35);
		tablaPuntos.put(TIEMPO, 25);
		tablaPuntos.put(BANANAS, 20);
		tablaPuntos.put(FRUTAS, 20);
		tablaPuntos.put(JOYA_ROJA, 20);
		tablaPuntos.put(JOYA_AZUL, 20);
		
		tablaMuniciones.put(ARMA_H, 150);
		tablaMuniciones.put(ARMA_R, 15);
		tablaMuniciones.put(ARMA_S, 10);
		tablaMuniciones.put(GRANADAS, 10);
	}
	
	private static final Sprite sprites;
	static {
		sprites = new Sprite(ObjetoDeJuego.DIR_SPRITES.resolve("bonus.gif"), new Dimension(18, 18));

		//-----armas, tiempo
		sprites.addFilaElementos(0, 44, 40, 13, 22, 4);
		sprites.addCuadroSecuencia(ARMA_H, 0, 0, -1);
		sprites.addCuadroSecuencia(ARMA_R, 0, 1, -1);
		sprites.addCuadroSecuencia(ARMA_S, 0, 2, -1);
		sprites.addCuadroSecuencia(TIEMPO, 0, 3, -1);

		//-----bombas
		sprites.addFilaElementos(40, 64, 64, 21, 46, 6);
		sprites.addCuadroSecuencia(GRANADAS, 1, 0, 30);
		for (int j = 1; j < 6; j++)
			sprites.addCuadroSecuencia(GRANADAS, 1, j, 5);

		//-----banana
		sprites.addFilaElementos(104, 40, 40, 11, 18, 8);
		sprites.addFilaElementos(144, 40, 40, 11, 18, 8);
		for (int j = 0; j < 8; j++)
			sprites.addCuadroSecuencia(BANANAS, 2, j, 3);
		for (int j = 0; j < 8; j++)
			sprites.addCuadroSecuencia(BANANAS, 3, j, 3);
		sprites.addCuadroSecuencia(BANANAS, 2, 0, -1);
		
		//-----frutassecas
		sprites.addFilaElementos(184, 56, 48, 20, 31, 1);
		sprites.addCuadroSecuencia(FRUTAS, 4, 0, -1);
		
		//-----joyas
		sprites.addFilaElementos(232, 24, 22, -1, 0, 21);
		sprites.addFilaElementos(254, 24, 22, -1, 0, 21);
		for (int j = 0; j < 21; j++) {
			sprites.addCuadroSecuencia(JOYA_ROJA, 5, j, 3);
			sprites.addCuadroSecuencia(JOYA_AZUL, 6, j, 3);
		}			
	}
	
	private BonusFactory() {}
	
	public static BonusMunicion newBonusGranadas() {
		return new BonusMunicion(tablaPuntos.get(GRANADAS), tablaMuniciones.get(GRANADAS), new Point(0,0), sprites) {
			@Override
			public void actualizar() {
				actualizarSecuencia(GRANADAS, AdminSprite.NINGUNO);
				super.actualizar();
			}
			
			@Override
			protected void entregarMuniciones(int cantMuniciones, SoldadoJugador soldado) {
				soldado.aumentarCantGranadas(cantMuniciones);
			}
		};
	}
	
	private static Arma crearArma(String tipo) {
		switch (tipo) {
			case ARMA_H : return new ArmaH();
			case ARMA_R : return new ArmaR();
			case ARMA_S : return new ArmaS();
			default : return null;
		}
	}
	
	public static BonusArma newBonusArma(String tipo) {
		return new BonusArma(tablaPuntos.get(tipo), crearArma(tipo), tablaMuniciones.get(tipo), new Point(0,0), sprites) {
			@Override public void actualizar() {
				actualizarSecuencia(tipo, AdminSprite.NINGUNO);
				super.actualizar();
			}
		};
	}
	
	public static Bonus newBonusPuntos(String tipo) {
		return new BonusSimple(tipo); 
	}
	
	public static Bonus newBonusTiempo(int time) {
		return new BonusSimple(TIEMPO) {
			private Integer tiempo = time;
			@Override public void consumirBonus(SoldadoJugador soldado) {
				if (tiempo != null) {
					mundo.aumentarTiempo(tiempo);
					tiempo = null;
					super.consumirBonus(soldado);
				}
			}
		};
	}
	
	private static class BonusSimple extends Bonus {
		private String secuencia;
		
		public BonusSimple(String nombre) {
			super(tablaPuntos.get(nombre), new Point(0,0), sprites);
			secuencia = nombre;
		}
		
		@Override 
		public void actualizar() {
			actualizarSecuencia(secuencia, AdminSprite.NINGUNO);
			super.actualizar();
		}
	}
}

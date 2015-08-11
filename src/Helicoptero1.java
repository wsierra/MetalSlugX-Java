import java.awt.Point;
import java.awt.Dimension;

public class Helicoptero1 extends Helicoptero {

	private static final int PUNTOS = 250;
	
	private static final Dimension TAMANIO = new Dimension(107, 93);
	private static final int ENERGIA = 30;
	private static final int RADIO_MOV = (MetalSlugT.getAncho() - TAMANIO.width) / 2;
	private static final int RADIO_ATAQUE = 30;
	
	private static final Point MIRA = new Point(54, 109);
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("helicoptero_1.gif"), TAMANIO);
		spriteClase.addFilaElementos(0, 184, 144, 22, 41, 2);
		spriteClase.addCuadroSecuencia(VUELO, 0, 0, 5);
		spriteClase.addCuadroSecuencia(VUELO, 0, 1, 5);
	}
	
	private static final int ATACANDO = 0;
	private static final int BOMBARDEANDO = 1;
	private static final int DESCANSANDO = 2;
	
	private Trigger triggerBombas = new Trigger(20);
	private Trigger triggerFinDescanso = new Trigger(60);
	
	private int estado;
	
	public Helicoptero1(int centro, int yFinal) {
		super(ENERGIA, new Point(centro, yFinal), RADIO_MOV, 0, spriteClase);
	}
	
	@Override
	protected void atacar() {
		switch (estado) {
			case ATACANDO :
				Point centro = new Point((getLadoDerecho() + getLadoIzquierdo()) / 2, getLadoInferior());
				int distancia = (int)Math.abs( mundo.getPosicionSJMasCercano(centro).x - centro.x );
				if (distancia <= RADIO_ATAQUE) {
					lanzarBomba();
					estado = BOMBARDEANDO;
				}
				break;
			case BOMBARDEANDO :
				if (triggerBombas.test()) {
					lanzarBomba();
					estado = DESCANSANDO;
				}
				break;
			case DESCANSANDO :
				if (triggerFinDescanso.test()) 
					estado = ATACANDO;
		}	
	}
	
	private void lanzarBomba() {
		Municion bomba = new BombaHelicoptero(getPosicion(), this, mundo);
		bomba.desplazar(MIRA.x, MIRA.y);
	}
	
	@Override
	protected void notificacionMuerte() {
		super.notificacionMuerte();
		mundo.setAvanceCamara(true);
	}

	@Override
	public void activar(Partida mundo) {
		super.activar(mundo);
		mundo.setAvanceCamara(false);
	}

	@Override
	public int getPuntos() {
		return PUNTOS;
	}
}

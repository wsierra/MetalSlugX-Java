import java.awt.Point;
import java.awt.Dimension;

public class Rehen extends ObjetoMovil {
	
	private static final int X_BONUS = -20;
	private static final int PUNTOS = 100;
	
	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("rehen.gif"), new Dimension(36, 61));

		spriteClase.addFilaElementos(0, 90, 84, 28, 23, 3);
		spriteClase.addFilaElementos(84, 90, 84, 44, 23, 4);
		spriteClase.addFilaElementos(168, 90, 84, 35, 23, 2);
		spriteClase.addFilaElementos(252, 90, 84, 24, 23, 4);

		spriteClase.addCuadroSecuencia("atado", 0, 0, -1);

		spriteClase.addCuadroSecuencia("liberandose", 0, 1, 10);
		spriteClase.addCuadroSecuencia("liberandose", 0, 2, 10);

		spriteClase.addCuadroSecuencia("libre", 1, 0, 50);
		spriteClase.addCuadroSecuencia("libre", 1, 1, 50);

		spriteClase.addCuadroSecuencia("bonus", 1, 2, 15);
		spriteClase.addCuadroSecuencia("bonus", 1, 3, 15);
		
		spriteClase.addCuadroSecuencia("saludo", 1, 0, 50);
		spriteClase.addCuadroSecuencia("saludo", 2, 0, 15);
		spriteClase.addCuadroSecuencia("saludo", 2, 1, 30);

		spriteClase.addCuadroSecuencia("huida", 3, 0, 7);
		spriteClase.addCuadroSecuencia("huida", 3, 1, 7);
		spriteClase.addCuadroSecuencia("huida", 3, 2, 7);
		spriteClase.addCuadroSecuencia("huida", 3, 3, 7);

		//~spriteClase.addCuadroSecuencia("prueba", 0, 0, 100);
		//~spriteClase.addCuadroSecuencia("prueba", 0, 1, 10);
		//~spriteClase.addCuadroSecuencia("prueba", 0, 2, 10);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 0, 50);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 1, 50);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 0, 50);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 1, 50);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 0, 50);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 1, 50);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 2, 15);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 3, 15);
		//~spriteClase.addCuadroSecuencia("prueba", 1, 0, 50);
		//~spriteClase.addCuadroSecuencia("prueba", 2, 0, 30);
		//~spriteClase.addCuadroSecuencia("prueba", 2, 1, 40);
	}
	
	private Bonus bonus;
	private String secuencia;
	
	public Rehen(int posicionX, Bonus bonus) {
		super(new Point(posicionX, 0), spriteClase);
		this.bonus = bonus;
		secuencia = "atado";
		setVelocidadX(-3.0f);
		setIniciarEnSuelo(true);
	}

	@Override
	public void actualizar() {
		
		if (actualizarSecuencia(secuencia, AdminSprite.NINGUNO)) {
			switch (secuencia) {
				case "liberandose" : 
					secuencia = "libre";
					break;
				case "bonus" :
					bonus.desplazar( getLadoIzquierdo() + X_BONUS - bonus.getLadoIzquierdo() - bonus.getAncho() / 2,
									 getLadoInferior() - bonus.getLadoInferior() );
					bonus.activar(mundo);
					secuencia = "saludo";
					break;
				case "saludo" :
					secuencia = "huida";
					break;
			}
		}
		
		if (secuencia.equals("huida"))
			mover();
		
		verificarDesactivacion();
	}
	
	public boolean soltarBonus() {
		if (secuencia.equals("libre")) {
			secuencia = "bonus";
			return true;
		}
		
		return false;
	}
	
	public boolean liberar() {
		if (secuencia.equals("atado") && estaVisible()) {
			secuencia = "liberandose";
			return true;
		}
		
		return false;
	}
	
	@Override 
	public String toString() {
		return super.toString() + ". Secuencia: " + secuencia;
	}
	
	public int getPuntos() {
		return PUNTOS;
	}
}

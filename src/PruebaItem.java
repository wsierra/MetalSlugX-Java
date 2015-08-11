import java.awt.Point;
import java.awt.Dimension;

public class PruebaItem extends Bonus {

	private static final Sprite spriteClase;
	static {
		spriteClase = new Sprite(DIR_SPRITES.resolve("bonus.gif"), new Dimension(40, 40));

		//-----armas, tiempo
		//~spriteClase.addFilaElementos(0, 44, 40, 0, 0, 4);
		//~
		//~for (int i = 0; i < 4; i++)
			//~spriteClase.addCuadroSecuencia("bonus", 0, i, 60);

		//-----bombas
		//~spriteClase.addFilaElementos(40, 64, 64, 0, 0, 6);
		//~
		//~spriteClase.addCuadroSecuencia("bombas", 0, 0, 30);
		//~for (int i = 1; i < 6; i++)
			//~spriteClase.addCuadroSecuencia("bombas", 0, i, 5);

		//-----banana
		spriteClase.addFilaElementos(104, 40, 40, 0, 0, 8);
		spriteClase.addFilaElementos(144, 40, 40, 0, 0, 8);
		
		for (int i = 0; i < 8; i++)
			spriteClase.addCuadroSecuencia("banana", 0, i, 3);
		for (int i = 0; i < 8; i++)
			spriteClase.addCuadroSecuencia("banana", 1, i, 3);
		spriteClase.addCuadroSecuencia("banana", 0, 0, -1);

		//-----joyas
		//~spriteClase.addFilaElementos(232, 24, 22, 0, 0, 21);
		//~spriteClase.addFilaElementos(254, 24, 22, 0, 0, 21);
		//~
		//~for (int i = 0; i < 21; i++)
			//~spriteClase.addCuadroSecuencia("roja", 0, i, 3);
		//~for (int i = 0; i < 21; i++)
			//~spriteClase.addCuadroSecuencia("azul", 1, i, 3);
	}

	public PruebaItem() {
		super(new Point(200, 350), spriteClase);
		setIniciarEnSuelo(true);
	}


	boolean listo;
	@Override
	public void actualizar() {
		actualizarSecuencia("banana", AdminSprite.NINGUNO);
	}

}

import java.awt.Point;
import java.awt.Dimension;
import java.util.Random;

public class ArmaS extends ArmaSimple {
	
	private static final int CANT_MUNICIONES = 20;
	private static final int TIEMPO_RECUPERACION = 30;
	
	private static final int PERDIGONES_POR_CARTUCHO = 7;
	private static final Dimension AREA_PERDIGONES = new Dimension(50, 120);
	
	private Random random = new Random();
	
	public ArmaS() {
		this(new Point());
	}
	
	public ArmaS(Point posicion) {
		super(posicion, TIEMPO_RECUPERACION);
		recargar(CANT_MUNICIONES);
	}
	
	@Override
	public Municion crearMunicion() {
		Point pos = getPosicion();
		int dir = getDireccionDisparo();
		Dimension area;
		switch (dir) {
			case Municion.DERECHA :
			case Municion.IZQUIERDA :
				area = AREA_PERDIGONES;
				break;
			case Municion.ARRIBA :
				area = new Dimension(AREA_PERDIGONES.height, AREA_PERDIGONES.width);
				break;
			default :
				int max = AREA_PERDIGONES.width > AREA_PERDIGONES.height ? AREA_PERDIGONES.width : AREA_PERDIGONES.height;
				area = new Dimension(max, max);
		} 
		
		for (int i = 0; i < PERDIGONES_POR_CARTUCHO; i++)
			new MunicionS(new Point( pos.x + random.nextInt(area.width) - area.width / 2,
						             pos.y + random.nextInt(area.height) - area.height / 2 ),
						  dir, getPortador(), mundo);
						  
		new FX(pos, dir, mundo);
		
		return null;
	}

	private static class FX extends EfectoEfimero {
		
		private static final Sprite spriteClase;
		static {
			spriteClase = new Sprite(DIR_SPRITES.resolve("municion_s_fx.gif"), new Dimension(0, 0));

			spriteClase.addFilaElementos(0, 193, 131, 0, 66, 5);
			spriteClase.addFilaElementos(131, 193, 131, 0, 66, 4);
			spriteClase.addFilaElementos(262, 132, 132, -57, 66, 4);
			
			spriteClase.addFilaElementos(394, 131, 193, 66, 192, 9);
			spriteClase.addFilaElementos(new Sprite.Elemento(528,         262, 132, 132, 66, 182),
										 new Sprite.Elemento(528 + 132,   262, 132, 132, 66, 182),
										 new Sprite.Elemento(528 + 2*132, 262, 132, 132, 66, 182),
										 new Sprite.Elemento(528 + 3*132, 262, 132, 132, 66, 182));

			spriteClase.addFilaElementos(587, 188, 191, 19, 170, 6);
			spriteClase.addFilaElementos(778, 188, 191, 19, 170, 3);
			spriteClase.addFilaElementos(969, 132, 132, -37, 170, 4);
			
			for (int j = 0; j < 5; j++)	spriteClase.addCuadroSecuencia("horizontal", 0, j, 3);
			for (int j = 0; j < 4; j++)	spriteClase.addCuadroSecuencia("horizontal", 1, j, 3);
			for (int j = 0; j < 4; j++)	spriteClase.addCuadroSecuencia("horizontal", 2, j, 3);
				
			for (int j = 0; j < 9; j++) spriteClase.addCuadroSecuencia("vertical", 3, j, 3);
			for (int j = 0; j < 4; j++)	spriteClase.addCuadroSecuencia("vertical", 4, j, 3);
				
			for (int j = 0; j < 6; j++) spriteClase.addCuadroSecuencia("diagonal", 5, j, 3);
			for (int j = 0; j < 3; j++) spriteClase.addCuadroSecuencia("diagonal", 6, j, 3);
			for (int j = 0; j < 4; j++) spriteClase.addCuadroSecuencia("diagonal", 7, j, 3);
		}
		
		private final String secuencia;
		private final int voltear;
		
		public FX(Point posicion, int direccion, Partida mundo) {
			super(posicion, spriteClase, mundo);
			
			voltear = (direccion & Municion.IZQUIERDA) != 0 ? AdminSprite.HORIZONTAL : AdminSprite.NINGUNO;
			
			switch (direccion) {
				case Municion.DERECHA :
				case Municion.IZQUIERDA : 
					secuencia = "horizontal";  break;
				case Municion.ARRIBA : 
					secuencia = "vertical";    break;
				default :
					secuencia = "diagonal";
			}
		}
		
		@Override
		public void actualizar() {
			if (actualizarSecuencia(secuencia, voltear))
				desactivar();
		}	
	}
}

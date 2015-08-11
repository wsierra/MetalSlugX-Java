import java.util.Random;
import java.awt.Point;

public class Nivel1 extends Nivel {
	
	public int getSuelo(int x) {
		return 400;
	}

	public void cargar() {
		
		setTiempoMaximo(200);
		setFondo(MetalSlugT.DIR_IMAGENES.resolve("mision1.gif"));
		
		crearCuadrillas(346, false, false, 1);
		crearCuadrillas(522, false, false, 2);
		crearCuadrillas(923, false, false, 1, 1, 1, 1);
		crearCuadrillas(1325, false, false, 1, 1, 1, 1);
		crearCuadrillas(1499, false, false, 2);
		
		add(1675, new Helicoptero1(1675 + MetalSlugT.getAncho() / 2,100));
		crearCuadrillas(1675, false, false, 2, 2, 1, 1);
		crearCuadrillas(1675, true, false, 1, 2, 2, 1);
		crearCuadrillas(1675, true, true, 2);
		
		crearCuadrillas(1838, false, false, 2);
		crearCuadrillas(1960, false, true, 1);
		crearCuadrillas(1960, false, false, 1);
		crearCuadrillas(2465, false, false, 2);
		
		add(2577, new Tanque(2577 + MetalSlugT.getAncho() + 40, 2577 + 500));
		crearCuadrillas(2577, false, false, 3, 2, 2);
		crearCuadrillas(2577, true, false, 2,1,3,2);
		
		crearCuadrillas(2665, false, true, 2);
		crearCuadrillas(2735, false, false, 4);
		crearCuadrillas(3191, false, false, 3);
		crearCuadrillas(3379, false, true, 1);
		crearCuadrillas(3644, false, true, 1);
		
		add(EdificioTorres.getXActivacion(3812), new EdificioTorres(new Point(3812, 377)));
		crearCuadrillas(3812, false, false, 3);
		crearCuadrillas(3812, true, false, 2, 2);
		crearCuadrillas(3812, true, true, 1);
		
		crearCuadrillas(4300, true, false, 2);
		crearCuadrillas(4300, false, false, 2);
		add(4357, new Grupo(new Point(4357, 0)) {
			
			Random random = new Random();
			
			{
				for (int i = 0; i < 30; i++) {
					Point pos = new Point(getPosicion().x, 300);
					if (random.nextBoolean()) 
						pos.translate(MetalSlugT.getAncho() + 100, 0);
					else
						pos.translate(-150, 0);
					miembros.add(new SoldadoEnemigo(pos));
				}
			} 
			private final RandomTrigger triggerSoldados = new RandomTrigger(200, 400);
			private int proximoActivar;
			
			@Override
			public void actualizar() {
				
				if (triggerSoldados.test()) {
					int tamañoHorda = random.nextInt(6) + 2;
 					while(tamañoHorda-- > 0 && proximoActivar < miembros.size()) {
						ObjetoDeJuego o = miembros.get(proximoActivar++);
						o.activar(mundo);
						o.desplazar(random.nextInt(150) - 75, 0);
					}
				}
				
				if (!quedanVivos()) {
					mundo.setAvanceCamara(true);
					desactivar();
				}
			}
			
			@Override
			public void activar(Partida mundo) {
				super.activar(mundo);
				mundo.setAvanceCamara(false);
			}
		});
		
		crearCuadrillas(5009, false, true, 2);
		crearCuadrillas(5073, false, false, 2, 2, 2);
		
		add(5373, new Tanque(5373 + MetalSlugT.getAncho() + 40, 5373 + 500));
		crearCuadrillas(5373, false, false, 3, 2, 2);
		crearCuadrillas(5373, true, false, 2,1,3,2);
		
		add(5586, new Helicoptero1(5586 + MetalSlugT.getAncho() / 2,100));
		crearCuadrillas(5586, false, false, 2, 2);
		crearCuadrillas(5586, true, false, 1, 2);
		crearCuadrillas(5586, true, true, 2);
		
		add(6375, new GrupoHelicopteros(new Point(6375, 0), 5));
		
		add(6750, new EnemigoFinal1(7640));
		
		int separacion = MetalSlugT.getAncho() + 50;
		add( 1362 - separacion, new Rehen(1362, BonusFactory.newBonusArma(BonusFactory.ARMA_H)) );
		add( 1517 - separacion, new Rehen(1517, BonusFactory.newBonusArma(BonusFactory.ARMA_H)) );
		add( 2596 - separacion, new Rehen(2596, BonusFactory.newBonusPuntos(BonusFactory.JOYA_ROJA)) );
		add( 2791 - separacion, new Rehen(2791, BonusFactory.newBonusPuntos(BonusFactory.BANANAS)) );
		add( 3300 - separacion, new Rehen(3300, BonusFactory.newBonusGranadas()) );
		add( 4023 - separacion, new Rehen(4023, BonusFactory.newBonusArma(BonusFactory.ARMA_R)) );
		add( 4825 - separacion, new Rehen(4825, BonusFactory.newBonusArma(BonusFactory.ARMA_S)) );
		add( 5064 - separacion, new Rehen(5064, BonusFactory.newBonusArma(BonusFactory.ARMA_H)) );
		add( 5222 - separacion, new Rehen(5222, BonusFactory.newBonusGranadas()) );
		add( 5630 - separacion, new Rehen(5630, BonusFactory.newBonusArma(BonusFactory.ARMA_H)) );
		add( 6824 - separacion, new Rehen(6824, BonusFactory.newBonusTiempo(60)) );
		add( 7153 - separacion, new Rehen(7153, BonusFactory.newBonusArma(BonusFactory.ARMA_R)) );
	}
	
	private void crearCuadrillas(int posCamara, boolean izquierda, boolean arriba, int... soldadosPorCuadrilla) {
		
		final Random random = new Random();
		int distanciaH = izquierda ? 100 : MetalSlugT.getAncho() + 60;
		int distanciaV = arriba ? -100 : 300;
		if (arriba)
			distanciaH -= 100;
		
		for (int cantSoldados : soldadosPorCuadrilla) {
			while (cantSoldados-- > 0) {
				distanciaH += random.nextInt(50) + 30;
				if (arriba)
					distanciaV -= random.nextInt(70) + 50;
				
				add(posCamara, new SoldadoEnemigo(new Point(posCamara + (izquierda ? -1 : 1) * distanciaH, distanciaV)));
			}
			
			if (!arriba)
				distanciaH += 300;
			else
				distanciaV -= 1000;
		}
	}

	public static void main(String[] args) {
		try {
			new MetalSlugT().correr(null);
		}
		catch (Juego.EjecucionException e) {}
	}
}

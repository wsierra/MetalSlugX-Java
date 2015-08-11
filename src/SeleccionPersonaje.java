import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class SeleccionPersonaje extends BloqueMST implements KeyListener {
	
	private static final int[] XS_CASILLAS = {52, 188, 324, 460};
	private static final int Y_CASILLA = 92;
	private static final int ANCHO_CASILLA = 128;
	private static final int ALTO_CASILLA = 300;
	private static final int X_SOLDADO = 34;
	private static final int SUELO_SOLDADO = 235;
	
	private static final Path DIR_SELECCION = MetalSlugT.DIR_IMAGENES.resolve("seleccion");
	private static final Path MUSICA = MetalSlugT.DIR_MUSICA.resolve("Barracks.wav");
	private static final int INICIO_LOOP = 690;
	private static final int TECLA_RANKING = KeyEvent.VK_R;
	private static final int[] TECLAS_ACT = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4};
	
	private MetalSlugT mst;

	private BufferedImage fondo;
	private BufferedImage[][] personajes;
	private BufferedImage[] compuerta;
	
	private int tiempoEspera = 60;
	private boolean ingresoAlguno;
	
	private SoldadoJugador[] soldados;
	private int[] casillas;
	private int[] jugadores;
	
	private static final int VACIA = -1;     	//casillas;
	private static final int AUSENTE = 0;		//jugadores;
	private static final int ACTIVO = 1;		//jugadores;
	private static final int BLOQUEADO = 2;		//jugadores;
	
	public SeleccionPersonaje(MetalSlugT mst) {
		this.mst = mst;
	}

	@Override
	public void inicializar() {
		try {
			fondo = ImageIO.read(DIR_SELECCION.resolve("fondo.gif").toFile());

			personajes = new BufferedImage[MetalSlugT.MAXJUGADORES][4];
			compuerta = new BufferedImage[MetalSlugT.MAXJUGADORES];

			for (int i = 0; i < personajes.length; i++) {
				for (int j = 0; j < personajes[i].length; j++)
					personajes[i][j] = ImageIO.read(DIR_SELECCION.resolve("p" + (i+1) + "_heroe" + (j+1) + ".gif").toFile());
				compuerta[i] = ImageIO.read(DIR_SELECCION.resolve("p" + (i+1) + "_compuerta.gif").toFile());
			}
		} 
		catch (IOException ex) {}
		
		casillas = new int[personajes[0].length];
		for (int i = 0; i < casillas.length; i++)
			casillas[i] = VACIA;
		jugadores = new int[MetalSlugT.MAXJUGADORES];
		soldados = new SoldadoJugador[MetalSlugT.MAXJUGADORES];
		
		mst.addKeyListener(this);
		
		addAyuda("1 - " + MetalSlugT.MAXJUGADORES + ": Agregar jugador   |   R: Ver ranking");
		addAyuda("Teclas de dirección (ver configuración): Seleccionar personaje");
		addAyuda("Teclas de acción (ver configuración): Aceptar selección");
		
		MetalSlugT.musica.reproducirNuevo(MUSICA, INICIO_LOOP);
	}
	
	@Override
	public void actualizar() {
		
		for (int i = 0; i < casillas.length; i++)
			if (casillas[i] != VACIA && jugadores[casillas[i]] == BLOQUEADO)	
				if (soldados[casillas[i]] == null) {
				
					SoldadoJugador s = null;
					switch (i) {
						case 0 : s = new Marco();	break;
						case 1 : s = new Eri(); 	break;
						case 2 : s = new Tarma(); 	break;
						case 3 : s = new Fio(); 	break;
					}
					s.desplazar(XS_CASILLAS[i] + X_SOLDADO, Y_CASILLA + SUELO_SOLDADO - s.getAlto());
					soldados[casillas[i]] = s;
				}
				else
					soldados[casillas[i]].actualizar();
					
		if (listoParaComenzar())
			if (--tiempoEspera == 0)
				mst.getEjecucion().cambiarBloque(new Partida(mst, soldados));
	}

	@Override
	public void pintarBloque(Graphics2D g) {
		g.drawImage(fondo, 0, 0, MetalSlugT.getAncho(), MetalSlugT.getAlto(), null);

		for (int i = 0; i < casillas.length; i++) {
			if (casillas[i] != VACIA) {
				if (jugadores[casillas[i]] != BLOQUEADO)
					g.drawImage(personajes[casillas[i]][i], XS_CASILLAS[i], Y_CASILLA, ANCHO_CASILLA, ALTO_CASILLA, null);
				else {
					g.drawImage(compuerta[casillas[i]], XS_CASILLAS[i], Y_CASILLA, ANCHO_CASILLA, ALTO_CASILLA, null);
					soldados[casillas[i]].pintar(g);
				}
			}
		}
	}
	
	private boolean listoParaComenzar() {
		for (int j : jugadores)
			if (j == ACTIVO)
				return false;
				
		return ingresoAlguno;
	}
	
	private void mover(int jugador, boolean derecha) {
		int numCasilla = 0;
		while (casillas[numCasilla] != jugador)
			numCasilla++;

		int numViejo = numCasilla;
		do {
			numCasilla = ( numCasilla + casillas.length + (derecha ? 1 : -1) ) % casillas.length;
		} while (casillas[numCasilla] != VACIA && numCasilla != numViejo);

		casillas[numViejo] = VACIA;
		casillas[numCasilla] = jugador;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (!listoParaComenzar()) {
			
			int kc = e.getKeyCode();
			
			if (kc == TECLA_RANKING)
				mst.getEjecucion().cambiarBloque(new Ranking(mst));
			else
				for (int jug = 0; jug < MetalSlugT.MAXJUGADORES; jug++) {

					if (jugadores[jug] == AUSENTE && kc == TECLAS_ACT[jug]) {
						
						ingresoAlguno = true;
						
						int numCasilla = 0;
						while (casillas[numCasilla] != VACIA)
							numCasilla++;

						casillas[numCasilla] = jug;
						jugadores[jug] = ACTIVO;
						mst.addKeyListener(new ListenerJugador(jug));
					}
				}
		}
	}
	
	@Override public void keyReleased(KeyEvent e) {}
	@Override public void keyTyped(KeyEvent e) {}
	
	@Override
	public void finalizar() {
		mst.removeKeyListener(this);
		MetalSlugT.musica.finalizarTodo();
	}

	private class ListenerJugador extends KeyAdapter {

		private int j;

		public ListenerJugador(int jugador) {
			j = jugador;
		}
	
		@Override
		public void keyPressed(KeyEvent e) {
			Integer kc = e.getKeyCode();

			if ( kc.equals(mst.getConf().getControl(j, "derecha")) )
				mover(j, true);
			else if ( kc.equals(mst.getConf().getControl(j, "izquierda")) )
				mover(j, false);
			else if ( kc.equals(mst.getConf().getControl(j, "saltar")) ||
					  kc.equals(mst.getConf().getControl(j, "disparar")) ||
					  kc.equals(mst.getConf().getControl(j, "granada")) ) {
				jugadores[j] = BLOQUEADO;
				mst.removeKeyListener(this);
			}
		}
	}
}

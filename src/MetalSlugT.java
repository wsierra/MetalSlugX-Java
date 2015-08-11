import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class MetalSlugT extends Juego {

	private static final int ANCHO = 640;
	private static final int ALTO = 480;

	public static final int MAXJUGADORES = 4;

	public static final Path DIR = Paths.get("MetalSlugT", "archivos");
	public static final Path DIR_IMAGENES = DIR.resolve("imagenes");
	public static final Path DIR_SONIDOS = DIR.resolve("sonidos");
	public static final Path DIR_CONF = DIR.resolve("conf");
	public static final Path DIR_MUSICA = DIR_SONIDOS.resolve("musica");
	public static final Path DIR_SFX = DIR_SONIDOS.resolve("fx");
	public static final Path DIR_PISTAS = DIR_MUSICA.resolve("pistas");
	public static final Path ARCH_CONF = DIR_CONF.resolve("conf.data");
	
	public static final AdministradorSonido musica = new AdministradorSonido();
	public static final AdministradorSonido sfx = new AdministradorSonido();
	
	private Configuracion configuracion;

	@Override
	protected String getParamTitulo() {
		return "Metal Slug T";
	}

	@Override
	public Path getScreenshot() {
		return DIR_IMAGENES.resolve("screenshot.png");
	}

	@Override
	protected Dimension getParamTamanio() {
		return new Dimension(ANCHO, ALTO);
	}

	@Override
	protected boolean getParamPantallaCompleta() {
		return false;
	}

	@Override
	protected EjecucionJuego getParamEjecucionJuego() {
		return new EjecucionMST();
	}

	public static String jugadorToString(Jugador j) {
		return "P" + (j.getNumJugador() + 1);
	}
	
	public static int getAncho() {
		return ANCHO;
	}

	public static int getAlto() {
		return ALTO;
	}

	public Configuracion getConf() {
		return configuracion;
	}
	
	private class EjecucionMST extends EjecucionJuego {
			
		public EjecucionMST() {
			super(new SeleccionPersonaje(MetalSlugT.this));
		}

		@Override
		protected void inicializar() {
			configuracion = new Configuracion();
			try {
				configuracion.cargar(ARCH_CONF);
			}
			catch (IOException | ClassNotFoundException e) {
				configuracion = new ConfiguracionMST();
			}
			
			setAdminMusica(musica);
			setAdminSFX(sfx);
			
			setPantallaCompleta(configuracion.getPantallaCompleta());
			setMantenerRelacionAspecto(configuracion.getMantenerRelacionAspecto());
			musica.setMute(!configuracion.getEstadoSonido());
			sfx.setMute(!configuracion.getEstadoSonido());
			
			BloqueMST.addAyudaGeneral("F1: Ayuda   |   F2: Configuración   |   F3: Música on/off   |   F4: FX on/off");
			BloqueMST.addAyudaGeneral("F11: Pantalla completa   |   F12: Relación de aspecto   |   ESC: Salir");
			 
			addKeyListener(new KeyAdapter() {
				@Override public void keyPressed(KeyEvent e) {    
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
						parar();
					else if (e.getKeyCode() == KeyEvent.VK_F1)
						BloqueMST.setAyudaVisible(true);
					else if (e.getKeyCode() == KeyEvent.VK_F2) {
						mostrarPanel(new PanelConfMST(MetalSlugT.this, configuracion));
						setPantallaCompleta(configuracion.getPantallaCompleta());
						setMantenerRelacionAspecto(configuracion.getMantenerRelacionAspecto());
						musica.setMute(!configuracion.getEstadoSonido());
						sfx.setMute(!configuracion.getEstadoSonido());
						try {
							configuracion.guardar(ARCH_CONF);
						}
						catch (IOException ex) {}
					}
					else if (e.getKeyCode() == KeyEvent.VK_F3)
						getAdminMusica().setMute(!getAdminMusica().isMute());
					else if (e.getKeyCode() == KeyEvent.VK_F4)
						getAdminSFX().setMute(!getAdminSFX().isMute());
					else if (e.getKeyCode() == KeyEvent.VK_F11)
						setPantallaCompleta(!isPantallaCompleta());
					else if (e.getKeyCode() == KeyEvent.VK_F12)
						setMantenerRelacionAspecto(!isMantenerRelacionAspecto());
					else if (e.getKeyCode() == KeyEvent.VK_F9)
						DebugInfo.setOn(!DebugInfo.isOn());
				}
				@Override public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_F1)
						BloqueMST.setAyudaVisible(false);
				}
			});				
		}

		@Override
		protected void finalizar() {
			BloqueMST.borrarAyudaGeneral();
		}
	}

	public static void main(String[] args) {
		try {
			new MetalSlugT().correr(null);
		}
		catch (EjecucionException e) {}
	}
}

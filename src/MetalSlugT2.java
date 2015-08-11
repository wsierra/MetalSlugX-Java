import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MetalSlugT2 extends Juego {

	private static final int ANCHO = 640;
	private static final int ALTO = 480;
	Sprite s;
	@Override
	protected String getParamTitulo() {
		return "Metal Slug T";
	}

	@Override
	public String getScreenshot() {
		return "Imagenes/MetalSlugT/screenshot.png";
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

	private class EjecucionMST extends EjecucionJuego {

		private BufferedImage fondo;
		private BufferedImage marco;
		private int camara;
		private float marcoX = 320;
		private float marcoY = 300;
		private int dir = 1;
		Keyboard kb = new Keyboard();
		private Configuracion conf;


		public synchronized void sumarMarcoX(float x) {
			marcoX += x;
		}

		public synchronized float getMarcoX() {
			return marcoX;
		}

		protected void inicializar() {

			canvas.addKeyListener(kb);

			setMantenerRelacionAspecto(true);

			try {
				fondo = ImageIO.read(new File("Imagenes/MetalSlugT/mision1.gif"));
				marco = ImageIO.read(new File("Imagenes/MetalSlugT/marco.gif"));
				conf = new Configuracion("conf.dat");
			}
			catch (Exception ex) {
				conf = new ConfiguracionMST();
				System.err.println(ex);
			}

			s = new Sprite("Imagenes/MetalSlugT/marco.gif", null);

			Sprite.Elemento[] aux = new Sprite.Elemento[6];

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 82, 0, 82, 82, 20 + i, 15 - i);
			s.addFilaElementos(aux);

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 62, 82, 62, 96, 10 + i, 11 - i);
			s.addFilaElementos(aux);

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 62, 178, 62, 120, 3 - i, 5 + i);
			s.addFilaElementos(aux);

			s.addFilaElementos(new Sprite.Elemento(0, 298, 102, 100, 12, 23));

			for (int i = 0; i < 5; i++)
				aux[i] = new Sprite.Elemento(i * 94, 398, 94, 88, 10 - i, 10 + i);
			s.addFilaElementos(aux);


			s.addCuadroSecuencia("parado",    0, 0, 1);
			s.addCuadroSecuencia("corriendo", 0, 1, 5);
			s.addCuadroSecuencia("corriendo", 0, 2, 5);
			s.addCuadroSecuencia("corriendo", 0, 3, 5);
			s.addCuadroSecuencia("corriendo", 0, 4, 5);
			s.addCuadroSecuencia("saltando",  0, 5, 1);

			s.addCuadroSecuencia("paradoDiag",    1, 0, 1);
			s.addCuadroSecuencia("corriendoDiag", 1, 1, 5);
			s.addCuadroSecuencia("corriendoDiag", 1, 2, 5);
			s.addCuadroSecuencia("corriendoDiag", 1, 3, 5);
			s.addCuadroSecuencia("corriendoDiag", 1, 4, 5);
			s.addCuadroSecuencia("saltandoDiag",  1, 5, 1);

			s.addCuadroSecuencia("paradoArr",    2, 0, 1);
			s.addCuadroSecuencia("corriendoArr", 2, 1, 5);
			s.addCuadroSecuencia("corriendoArr", 2, 2, 5);
			s.addCuadroSecuencia("corriendoArr", 2, 3, 5);
			s.addCuadroSecuencia("corriendoArr", 2, 4, 5);
			s.addCuadroSecuencia("saltandoArr",  2, 5, 1);

			s.addCuadroSecuencia("granada", 3, 0, 20);

			s.addCuadroSecuencia("muriendo", 0, 0, 60);
			s.addCuadroSecuencia("muriendo", 4, 0, 10);
			s.addCuadroSecuencia("muriendo", 4, 1, 10);
			s.addCuadroSecuencia("muriendo", 4, 2, 10);
			s.addCuadroSecuencia("muriendo", 4, 3, 10);
			s.addCuadroSecuencia("muriendo", 4, 4, 10);
			s.addCuadroSecuencia("muriendo", 4, 3, 10);
			s.addCuadroSecuencia("muriendo", 4, 4, 10);
			s.addCuadroSecuencia("muriendo", 4, 3, 10);
			s.addCuadroSecuencia("muriendo", 4, 4, 10);
			s.addCuadroSecuencia("muriendo", 4, 3, 10);
			s.addCuadroSecuencia("muriendo", 4, 4, 10);
			s.addCuadroSecuencia("muriendo", 4, 3, 10);
			s.addCuadroSecuencia("muriendo", 4, 4, 10);
			s.addCuadroSecuencia("muriendo", 4, 3, 10);
			s.addCuadroSecuencia("muriendo", 4, 4, 10);


			////////////aca///////////
			sec = s.getSecuencia("corriendo");

			for (Sprite.Cuadro c : sec)
				System.out.println(c);
			System.out.println();
		}

		boolean f1 = false;

		protected void actualizar() {
			kb.update();

			if (kb.isKeyPressed(KeyEvent.VK_F1) && !f1) {
						f1 = true;
						final PanelConfMST pConf = new PanelConfMST(conf);
						WindowAdapter wa = new WindowAdapter() {

							private boolean cerrado = false;

							public void windowClosed(WindowEvent e) {
								if (!cerrado) {
									System.out.println(this);
									try {
										conf.guardar("conf.dat");
									}
									catch (Exception ex) {}
									setPantallaCompleta(conf.getPantallaCompleta());
									setMantenerRelacionAspecto(conf.getMantenerRelacionAspecto());
									cerrado = true;
								}
							}
						};
						lanzarDialogo("Configuracion", true, pConf, wa);
			}
			if (!kb.isKeyPressed(KeyEvent.VK_F1))
				 f1 = false;

			if (kb.isKeyPressed(conf.getControl("izquierda"))) {
						if (dir == -1)
							sumarMarcoX(-4.0f);
						else
							dir = -1;
						caminando = true;
					}

					else if (kb.isKeyPressed( conf.getControl("derecha"))) {
						if (dir == 1)
							sumarMarcoX(4.0f);
						else
							dir = 1;
						caminando = true;
					}
					else caminando = false;

			if (kb.isKeyPressed( conf.getControl("saltar"))){
							marcoY -= 8.0f;

					}else if (marcoY < 300 )
						marcoY += 8.0f;


			if (getMarcoX() > 320)
				camara = (int)getMarcoX() - 320;
			//marcoX += 2.0f;
			System.out.println(i++);
			if (caminando)
				sprite = ( i / 5 ) % 4 + 1;
			else
				sprite = 0;
		}
		int i =0;
		boolean caminando = false;
		int sprite = 0;

		int cont = 0;
		int numCuadro = 0;
		java.util.ArrayList<Sprite.Cuadro> sec;

		protected void pintar(Graphics2D g) {
			g.translate(-camara, 0);
			//g.drawImage(fondo, 0, 0, null);

			Sprite.Cuadro c = sec.get(numCuadro);
			Sprite.Elemento e = c.elemento;

			g.drawImage(marco, (int)getMarcoX(), (int)marcoY, (int)getMarcoX() + e.ancho, (int)marcoY + e.alto, e.x, e.y, e.x + e.ancho, e.y + e.alto, null);

			if (cont == c.duracion - 1) {
				cont = 0;
				if (numCuadro == sec.size() - 1)
					numCuadro = 0;
				else
					numCuadro++;
			}
			else cont++;
		}

		protected void finalizar() {}
	}

	public static void main(String[] args) {
		try {
			new MetalSlugT2().correr(null);
		}
		catch (EjecucionException e) {}
	}
}

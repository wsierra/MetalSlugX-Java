import java.awt.*;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MetalSlugT4 extends Juego {

	private static final int ANCHO = 640;
	private static final int ALTO = 480;

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

	private Sprite sprite;
	private int camara;
	private Rectangle area = new Rectangle(100, 311, 36, 71);
	private AdminSprite adm = new AdminSprite();

	private class AdminSprite {

	public static final int NINGUNO = 0;
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	public static final int AMBOS = 3;

	private String nombreSecuencia = "";
	private ArrayList<Sprite.Cuadro> secuencia;
	private int numCuadro;
	private int duracionCuadro;
	private int voltear;

	private int dx1, dy1;
	private int dx2, dy2;
	private int sx1, sy1;
	private int sx2, sy2;


	public boolean setSecuencia(String nombre, int voltear) {

		if (!nombreSecuencia.equals(nombre)) {
			nombreSecuencia = nombre;
			secuencia = sprite.getSecuencia(nombre);
			numCuadro = 0;
			duracionCuadro = secuencia.get(numCuadro).duracion;
		}

		if (duracionCuadro != -1 && --duracionCuadro == 0 || this.voltear != voltear) {
			this.voltear = voltear;
			generarParametros();
			numCuadro = (numCuadro + 1) % secuencia.size();
			duracionCuadro = secuencia.get(numCuadro).duracion;
			return numCuadro == 0;
		}
		return false;
	}

	private void generarParametros() {
		Sprite.Elemento e = secuencia.get(numCuadro).elemento;

		sx1 = e.x;
		sy1 = e.y;
		sx2 = e.x + e.ancho;
		sy2 = e.y + e.alto;

		if (voltear != HORIZONTAL && voltear != AMBOS) {
			dx1 = area.x - e.deltaX;
			dx2 = dx1 + e.ancho;
		}
		else {
			dx1 = area.x + area.width + e.deltaX;
			dx2 = dx1 - e.ancho;
		}

		if (voltear != VERTICAL && voltear != AMBOS) {
			dy1 = area.y - e.deltaY;
			dy2 = dy1 + e.alto;
		}
		else {
			dy1 = area.y + area.height + e.deltaY;
			dy2 = dy1 - e.alto;
		}
	}
}





	private class EjecucionMST extends EjecucionJuego {

		private BufferedImage fondo;

		private int dir = 1;
		Keyboard kb = new Keyboard();
		private Configuracion conf;


		protected void inicializar() {

			canvas.addKeyListener(kb);

			setMantenerRelacionAspecto(true);

			try {
				fondo = ImageIO.read(new File("Imagenes/MetalSlugT/mision1.gif"));
				conf = new Configuracion("conf.dat");
			}
			catch (Exception ex) {
				conf = new ConfiguracionMST();
				System.err.println(ex);
			}

			sprite = new Sprite("Imagenes/MetalSlugT/marco.gif", null);

			Sprite.Elemento[] aux = new Sprite.Elemento[6];

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 82, 0, 82, 82, 13, 10);
			sprite.addFilaElementos(aux);

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 62, 82, 62, 96, 0, 0);
			sprite.addFilaElementos(aux);

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 62, 178, 62, 120, 0, 0);
			sprite.addFilaElementos(aux);

			sprite.addFilaElementos(new Sprite.Elemento(0, 298, 102, 100, 0, 0));

			for (int i = 0; i < 5; i++)
				aux[i] = new Sprite.Elemento(i * 94, 398, 94, 88, 0, 0);
			sprite.addFilaElementos(aux);


			sprite.addCuadroSecuencia("parado",    0, 0, 7);
			sprite.addCuadroSecuencia("corriendo", 0, 1, 7);
			sprite.addCuadroSecuencia("corriendo", 0, 2, 7);
			sprite.addCuadroSecuencia("corriendo", 0, 3, 7);
			sprite.addCuadroSecuencia("corriendo", 0, 4, 7);
			sprite.addCuadroSecuencia("saltando",  0, 5, 1);

			sprite.addCuadroSecuencia("paradoDiag",    1, 0, 1);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 1, 7);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 2, 7);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 3, 7);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 4, 7);
			sprite.addCuadroSecuencia("saltandoDiag",  1, 5, 1);

			sprite.addCuadroSecuencia("paradoArr",    2, 0, 1);
			sprite.addCuadroSecuencia("corriendoArr", 2, 1, 7);
			sprite.addCuadroSecuencia("corriendoArr", 2, 2, 7);
			sprite.addCuadroSecuencia("corriendoArr", 2, 3, 7);
			sprite.addCuadroSecuencia("corriendoArr", 2, 4, 7);
			sprite.addCuadroSecuencia("saltandoArr",  2, 5, 1);

			sprite.addCuadroSecuencia("granada", 3, 0, 20);

			sprite.addCuadroSecuencia("muriendo", 0, 0, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 0, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 1, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 2, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);


			////////////aca///////////
			sec = sprite.getSecuencia("corriendo");

			for (Sprite.Cuadro c : sec)
				System.out.println(c);
			System.out.println();
		}

		boolean f1 = false;

		protected void actualizar() {
			kb.update();

			for (Marco m : marcos)
				m.actualizar();


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
					area.x -= 4;
				else
					dir = -1;

				if (!saltando)
					adm.setSecuencia("corriendo", AdminSprite.HORIZONTAL);
			}

			else if (kb.isKeyPressed( conf.getControl("derecha"))) {
				if (dir == 1)
					area.x += 4;
				else
					dir = 1;

				if (!saltando)
					adm.setSecuencia("parado", AdminSprite.NINGUNO);
			}
			else if (!saltando)
				adm.setSecuencia("parado", dir == 1 ? AdminSprite.NINGUNO : AdminSprite.HORIZONTAL);

			if (saltando) {

				area.y = yInicial + (int)(- impulso * cont + 0.5f * cont * cont / 2);
				adm.setSecuencia("saltando", dir == 1 ? AdminSprite.NINGUNO : AdminSprite.HORIZONTAL);
				cont++;

				if (area.y == yInicial )
					saltando = false;
			}
			else if (kb.isKeyPressed( conf.getControl("saltar"))){
				cont = 1;
				saltando = true;
			}

			if (area.x > 320)
				camara = area.x - 320;

		}

		Marco[] marcos = new Marco[0];
		{
			for (int i = 0; i < marcos.length; i++)
				marcos[i] = new Marco();

		}

		int i =0;
		boolean saltando = false;
		int yInicial = 311;
		float impulso = 10.0f;
		int cont = 1;
		int numCuadro = 0;
		java.util.ArrayList<Sprite.Cuadro> sec;

		protected void pintar(Graphics2D g) {


			g.translate(-camara, 0);
			g.drawImage(fondo, 0, 0, null);
			for (Marco m : marcos)
				m.pintar(g);
			//Sprite.Cuadro c = sec.get(numCuadro);
			//Sprite.Elemento e = c.elemento;
			//~System.out.println(adm.dx1 + " " + adm.dy1 + " " +
								//~adm.dx2 + " " + adm.dy2 + " " +
								//~adm.sx1 + " " + adm.sy1 + " " +
								//~adm.sx2 + " " + adm.sy2 );
			g.drawImage(sprite.getImagen(),
						adm.dx1, adm.dy1,
						adm.dx2, adm.dy2,
						adm.sx1, adm.sy1,
						adm.sx2, adm.sy2,
						null);

			//~if (cont == c.duracion - 1) {
				//~cont = 0;
				//~if (numCuadro == sec.size() - 1)
					//~numCuadro = 0;
				//~else
					//~numCuadro++;
			//~}
			//~else cont++;
		}

		protected void finalizar() {}
	}


	/**
	 *
	 *
	 *
	 *
	 *
	 * marco
	 *
	 *
	 *
	 *
	 *
	 **/




	private class Marco{
		private class AdminSprite {

		public static final int NINGUNO = 0;
		public static final int HORIZONTAL = 1;
		public static final int VERTICAL = 2;
		public static final int AMBOS = 3;

		private String nombreSecuencia = "";
		private ArrayList<Sprite.Cuadro> secuencia;
		private int numCuadro;
		private int duracionCuadro;
		private int voltear;

		private int dx1, dy1;
		private int dx2, dy2;
		private int sx1, sy1;
		private int sx2, sy2;

		public boolean setSecuencia(String nombre, int voltear) {

			if (!nombreSecuencia.equals(nombre)) {
				nombreSecuencia = nombre;
				secuencia = sprite.getSecuencia(nombre);
				numCuadro = 0;
				duracionCuadro = secuencia.get(numCuadro).duracion;
			}

			this.voltear = voltear;
			generarParametros();

			if (duracionCuadro != -1 && --duracionCuadro == 0) {
				numCuadro = (numCuadro + 1) % secuencia.size();
				duracionCuadro = secuencia.get(numCuadro).duracion;
				return numCuadro == 0;
			}

			return false;
		}

		private void generarParametros() {
			Sprite.Elemento e = secuencia.get(numCuadro).elemento;

			sx1 = e.x;
			sy1 = e.y;
			sx2 = sx1 + e.ancho;
			sy2 = sy1 + e.alto;

			if (voltear != HORIZONTAL && voltear != AMBOS) {
				dx1 = area.x - e.deltaX;
				dx2 = dx1 + e.ancho;
			}
			else {
				dx1 = area.x + area.width + e.deltaX;
				dx2 = dx1 - e.ancho;
			}

			if (voltear != VERTICAL && voltear != AMBOS) {
				dy1 = area.y - e.deltaY;
				dy2 = dy1 + e.alto;
			}
			else {
				dy1 = area.y + area.height + e.deltaY;
				dy2 = dy1 - e.alto;
			}
		}
	}


		private int dir = 1;

		Sprite sprite = new Sprite("Imagenes/MetalSlugT/marco.gif", null);
		int estado;
		private Rectangle area = new Rectangle(100, 311, 36, 71);
		private AdminSprite adm = new AdminSprite();

		public Marco() {


			sprite = new Sprite("Imagenes/MetalSlugT/marco.gif", null);

			Sprite.Elemento[] aux = new Sprite.Elemento[6];

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 82, 0, 82, 82, 13, 10);
			sprite.addFilaElementos(aux);

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 62, 82, 62, 96, 0, 0);
			sprite.addFilaElementos(aux);

			for (int i = 0; i < 6; i++)
				aux[i] = new Sprite.Elemento(i * 62, 178, 62, 120, 0, 0);
			sprite.addFilaElementos(aux);

			sprite.addFilaElementos(new Sprite.Elemento(0, 298, 102, 100, 0, 0));

			for (int i = 0; i < 5; i++)
				aux[i] = new Sprite.Elemento(i * 94, 398, 94, 88, 0, 0);
			sprite.addFilaElementos(aux);


			sprite.addCuadroSecuencia("parado",    0, 0, 1);
			sprite.addCuadroSecuencia("corriendo", 0, 1, 7);
			sprite.addCuadroSecuencia("corriendo", 0, 2, 7);
			sprite.addCuadroSecuencia("corriendo", 0, 3, 7);
			sprite.addCuadroSecuencia("corriendo", 0, 4, 7);
			sprite.addCuadroSecuencia("saltando",  0, 5, 1);

			sprite.addCuadroSecuencia("paradoDiag",    1, 0, 1);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 1, 7);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 2, 7);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 3, 7);
			sprite.addCuadroSecuencia("corriendoDiag", 1, 4, 7);
			sprite.addCuadroSecuencia("saltandoDiag",  1, 5, 1);

			sprite.addCuadroSecuencia("paradoArr",    2, 0, 1);
			sprite.addCuadroSecuencia("corriendoArr", 2, 1, 7);
			sprite.addCuadroSecuencia("corriendoArr", 2, 2, 7);
			sprite.addCuadroSecuencia("corriendoArr", 2, 3, 7);
			sprite.addCuadroSecuencia("corriendoArr", 2, 4, 7);
			sprite.addCuadroSecuencia("saltandoArr",  2, 5, 1);

			sprite.addCuadroSecuencia("granada", 3, 0, 20);

			sprite.addCuadroSecuencia("muriendo", 0, 0, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 0, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 1, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 2, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 3, 10);
			sprite.addCuadroSecuencia("muriendo", 4, 4, 10);

		}

		public void actualizar() {

			int random = (int) (200 * Math.random());

			if (random < 4)
				estado = random;


			if (estado == 1) {
				if (dir == -1)
					area.x -= 4;
				else
					dir = -1;

				if (!saltando)
					adm.setSecuencia("corriendo", AdminSprite.HORIZONTAL);
			}

			else if (estado == 2) {
				if (dir == 1)
					area.x += 4;
				else
					dir = 1;

				if (!saltando)
					adm.setSecuencia("corriendo", AdminSprite.NINGUNO);
			}
			else if (!saltando)
				adm.setSecuencia("parado", dir == 1 ? AdminSprite.NINGUNO : AdminSprite.HORIZONTAL);

			if (saltando) {

				area.y = yInicial + (int)(- impulso * cont + 0.5f * cont * cont / 2);
				adm.setSecuencia("saltando", dir == 1 ? AdminSprite.NINGUNO : AdminSprite.HORIZONTAL);
				cont++;

				if (area.y == yInicial ) {
					saltando = false;
					estado = 0;
				}
			}
			else if (estado == 3){
				cont = 1;
				saltando = true;
			}

			if (area.x <= 0)
				estado = 2;

		}
		int i =0;
		boolean saltando = false;
		int yInicial = 311;
		float impulso = 10.0f;
		int cont = 1;

		protected void pintar(Graphics2D g) {
			//g.translate(-camara, 0);
			//g.drawImage(fondo, 0, 0, null);

			//Sprite.Cuadro c = sec.get(numCuadro);
			//Sprite.Elemento e = c.elemento;
			//~System.out.println(adm.dx1 + " " + adm.dy1 + " " +
								//~adm.dx2 + " " + adm.dy2 + " " +
								//~adm.sx1 + " " + adm.sy1 + " " +
								//~adm.sx2 + " " + adm.sy2 );
			g.drawImage(sprite.getImagen(),
						adm.dx1, adm.dy1,
						adm.dx2, adm.dy2,
						adm.sx1, adm.sy1,
						adm.sx2, adm.sy2,
						null);

			//~if (cont == c.duracion - 1) {
				//~cont = 0;
				//~if (numCuadro == sec.size() - 1)
					//~numCuadro = 0;
				//~else
					//~numCuadro++;
			//~}
			//~else cont++;
		}

		protected void finalizar() {}
	}






	public static void main(String[] args) {
		try {
			new MetalSlugT4().correr(null);
		}
		catch (EjecucionException e) {}
	}



}

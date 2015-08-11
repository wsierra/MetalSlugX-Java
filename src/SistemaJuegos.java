import java.awt.*;
import java.awt.event.*;
import javax.swing.Box;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SistemaJuegos extends Frame implements ActionListener, ItemListener, KeyListener {

	private Map<String, Juego> juegos = new HashMap<String, Juego>();

	{
		juegos.put("Metal Slug T", new MetalSlugT());
		juegos.put("Mario Bros", new MarioBros());
		juegos.put("Pac-Man", new PacMan());
		juegos.put("Bomberman", new Bomberman());
		juegos.put("Islander", new JuegoTontoSinImagen());
		juegos.put("Tetris", new Tetris());
		juegos.put("Prince of Persia", new JuegoTontoSinImagen());
		juegos.put("Super Mario Bros", new SuperMarioBros());
		juegos.put("Metal Slug", new JuegoTontoSinImagen());
		juegos.put("Metal Slug 2", new JuegoTontoSinImagen());
		juegos.put("Metal Slug 3", new JuegoTontoSinImagen());
		juegos.put("JuegoTonto", new JuegoTonto());
		juegos.put("Juego vacio", new JuegoVacio());
		juegos.put("Juego sin tamaño", new JuegoSinTamanio());
	}

	private Button bJugar = new Button("Jugar");
	private List lista = new List();
	private ComponentImagen imagen;

	private static final Path IMG_DEFECTO = Paths.get("imagenes", "sin_imagen.png");
	private static final Path IMG_ERROR = Paths.get("imagenes", "error.png");

	private SistemaJuegos() {
		ArrayList<String> nombresJuegos = new ArrayList<String>(juegos.keySet());
		Collections.sort(nombresJuegos);
		for (String nombre : nombresJuegos)
			lista.add(nombre);

		lista.select(0);

		lista.addItemListener(this);
		lista.addKeyListener(this);
		bJugar.addActionListener(this);
		bJugar.addKeyListener(this);

		Panel pIzq = new Panel(new BorderLayout(0, 15));
		pIzq.add(lista, BorderLayout.CENTER);
		pIzq.add(bJugar, BorderLayout.SOUTH);
		pIzq.setPreferredSize(new Dimension(150, 0));

		Path path = juegos.get(lista.getSelectedItem()).getScreenshot();
		imagen = new ComponentImagen(path != null ? path : IMG_DEFECTO);

		Panel p = new Panel(new BorderLayout(25, 0));
		p.add(pIzq, BorderLayout.WEST);
		p.add(imagen, BorderLayout.CENTER);

		add(p, BorderLayout.CENTER);
		add(Box.createVerticalStrut(25), BorderLayout.NORTH);
		add(Box.createVerticalStrut(25), BorderLayout.SOUTH);
		add(Box.createHorizontalStrut(25), BorderLayout.WEST);
		add(Box.createHorizontalStrut(25), BorderLayout.EAST);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setMinimumSize(new Dimension(500, 300));
		setPreferredSize(new Dimension(900, 600));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		lista.requestFocus();
	}

	private void iniciarJuego() {
		try {
			juegos.get(lista.getSelectedItem()).correr(this);
			setVisible(false);
		}
		catch (Juego.EjecucionException e)
		{
			System.err.println(e);
			imagen.setImagen(IMG_ERROR);
		}
	}

	public void reanudar() {
		setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			iniciarJuego();
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		iniciarJuego();
		lista.requestFocus();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Path path = juegos.get(lista.getSelectedItem()).getScreenshot();
		imagen.setImagen(path != null ? path : IMG_DEFECTO);
	}

	public static void main(String[] a) {
		new SistemaJuegos();
	}
}


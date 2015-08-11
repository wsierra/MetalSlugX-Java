import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import java.nio.file.Paths;

public class PanelConfMST extends Panel implements ActionListener {
	
	private final MetalSlugT mst;
	
	private Configuracion confJuego;
	private Configuracion confTemp;

	private JTabbedPane tpPaneles = new JTabbedPane();
	private Video video = new Video();
	private Sonido sonido = new Sonido();
	private Controles controles = new Controles();
	private JButton bAceptar = new JButton("Aceptar");
	private JButton bCancelar = new JButton("Cancelar");
	private JButton bReset = new JButton("Reset");

	private static final int BORDES = 10;

	public PanelConfMST(MetalSlugT mst, Configuracion conf) {
		this.mst = mst;
		confJuego = conf;
		confTemp = new Configuracion(conf);
		cargarConf();
		
		setBackground(Color.WHITE);
		
		tpPaneles.add("Video", video);
		tpPaneles.add("Sonido", sonido);
		tpPaneles.add("Controles", controles);

		Panel pBotones = new Panel(new FlowLayout(FlowLayout.RIGHT));
		pBotones.add(bAceptar);
		pBotones.add(bCancelar);
		pBotones.add(bReset);

		setLayout(new BorderLayout());
		add(tpPaneles, BorderLayout.CENTER);
		add(pBotones, BorderLayout.SOUTH);
		add(Box.createVerticalStrut(5), BorderLayout.NORTH);
		add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		add(Box.createHorizontalStrut(5), BorderLayout.EAST);

		bAceptar.addActionListener(this);
		bCancelar.addActionListener(this);
		bReset.addActionListener(this);
	}

	private void cargarConf() {
		video.cargarConfiguracion();
		sonido.cargarConfiguracion();
		controles.cargarConfiguracion();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bReset) {
			confTemp = new ConfiguracionMST();
			cargarConf();
		}
		else {
			if (e.getSource() == bAceptar)
				confJuego.setConfiguracion(confTemp);
			mst.retomarBluce();
		}
	}

	private class Video extends Panel implements ItemListener {

		private Checkbox cbPantallaCompleta = new Checkbox("Pantalla completa");
		private Checkbox cbRelacionAspecto = new Checkbox("Mantenter relación de aspecto");

		public Video() {
			Panel panelCB = new Panel(new GridLayout(2, 1));
			panelCB.add(cbPantallaCompleta);
			panelCB.add(cbRelacionAspecto);

			setBackground(Color.WHITE);
			setLayout(new FlowLayout(FlowLayout.LEFT, BORDES, BORDES));
			add(panelCB);

			cbPantallaCompleta.addItemListener(this);
			cbRelacionAspecto.addItemListener(this);
		}

		public void cargarConfiguracion() {
			cbPantallaCompleta.setState(confTemp.getPantallaCompleta());
			cbRelacionAspecto.setState(confTemp.getMantenerRelacionAspecto());
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == cbPantallaCompleta)
				confTemp.setPantallaCompleta(cbPantallaCompleta.getState());
			else
				confTemp.setMantenerRelacionAspecto(cbRelacionAspecto.getState());
		}
	}

	private class Sonido extends Panel implements ItemListener {

		private Checkbox cbEstado = new Checkbox("Sonido activado");
		private Choice chPistas = new Choice();

		public Sonido() {
			cargarPistas();
			chPistas.setPreferredSize(new JButton("string largo 12345689 abc").getPreferredSize());

			Panel pPista = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			pPista.add(new Label("Pista: "));
			pPista.add(chPistas);

			Panel panel = new Panel(new GridLayout(2, 1));
			panel.add(cbEstado);
			panel.add(pPista);

			setBackground(Color.WHITE);
			setLayout(new FlowLayout(FlowLayout.LEFT, BORDES, BORDES));
			add(panel);

			cbEstado.addItemListener(this);
			chPistas.addItemListener(this);
		}

		public void cargarConfiguracion() {
			boolean estado = confTemp.getEstadoSonido();
			cbEstado.setState(estado);
			chPistas.setEnabled(estado);
			chPistas.select(confTemp.getPista());	
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == cbEstado) {
				boolean estado = cbEstado.getState();
				confTemp.setEstadoSonido(estado);
				chPistas.setEnabled(estado);
			}
			else
				confTemp.setPista(chPistas.getSelectedItem());
		}

		private void cargarPistas() {
			File ruta = MetalSlugT.DIR_PISTAS.toFile();
			File archivos[] = ruta.listFiles();

			if (archivos != null)
				for (File f : archivos)	{
					if( !f.isDirectory() && f.getName().endsWith(".wav") )
						chPistas.add( f.getName().substring(0, f.getName().length() - 4) );
				}
		}
	}

	private class Controles extends Panel implements ActionListener {

		private final String[] NOMBRES = {"Arriba", "Izquierda", "Derecha", "Saltar", "Disparar", "Granada"};
		private JToggleButton[] tbBotones = new JToggleButton[NOMBRES.length];
		private ButtonGroup grupoBotones = new ButtonGroup();
		
		private int jugador = 0;

		public Controles() {
			
			Panel panelRb = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			ButtonGroup grupoJugadores = new ButtonGroup();
			for (int i = 0; i < MetalSlugT.MAXJUGADORES; i++) {
				JRadioButton rb = new JRadioButton(String.valueOf(i + 1), i == jugador);
				rb.setFocusable(false);
				rb.setOpaque(false);
				rb.addActionListener(this);
				panelRb.add(rb);
				grupoJugadores.add(rb);
			}
			
			JSeparator sep = new JSeparator();
			sep.setPreferredSize(new Dimension(0,12));
			
			Panel panelJugadores = new Panel(new BorderLayout());
			panelJugadores.add(new Label("Jugador"), BorderLayout.NORTH);
			panelJugadores.add(panelRb, BorderLayout.CENTER);
			panelJugadores.add(sep, BorderLayout.SOUTH);
			
			Panel panelBotones = new Panel(new GridLayout(tbBotones.length, 1));
			JButton botonTamanio = new JButton("string largo 12345689");
			for (int i = 0; i < tbBotones.length; i++) {
				tbBotones[i] = new JToggleButton();
				tbBotones[i].setPreferredSize(botonTamanio.getPreferredSize());
				tbBotones[i].setRolloverEnabled(false);
				tbBotones[i].setFocusable(false);
				tbBotones[i].addActionListener(this);
				panelBotones.add(tbBotones[i]);
				grupoBotones.add(tbBotones[i]);
			}

			Panel panelNombres = new Panel(new GridLayout(NOMBRES.length, 1));
			for (String str : NOMBRES)
				panelNombres.add(new Label(str));

			Panel panel = new Panel(new BorderLayout());
			panel.add(panelJugadores, BorderLayout.NORTH);
			panel.add(panelNombres, BorderLayout.WEST);
			panel.add(panelBotones, BorderLayout.CENTER);
			
			setBackground(Color.WHITE);
			setLayout(new FlowLayout(FlowLayout.CENTER, BORDES, BORDES));
			add(panel);
			
			addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					limpiarSeleccion();
				}
			});

			addKeyAdapter();
		}

		public void cargarConfiguracion() {
			for (int i = 0; i < tbBotones.length; i++) {
				Integer tecla = confTemp.getControl(jugador, NOMBRES[i].toLowerCase());
				tbBotones[i].setText(tecla != null ? KeyEvent.getKeyText(tecla) : "");
			}
			limpiarSeleccion();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JRadioButton) {
				jugador = Integer.valueOf(e.getActionCommand()) - 1;
				cargarConfiguracion();
			}
			else {
				requestFocus();
				setFocusTraversalKeysEnabled(false);
			}
		}
		
		private void limpiarSeleccion() {
			grupoBotones.clearSelection();
			setFocusTraversalKeysEnabled(true);
		}

		private void addKeyAdapter() {
			addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					if (grupoBotones.getSelection() != null) {
						int tecla = e.getKeyCode();
						String texto = KeyEvent.getKeyText(tecla);
						e.consume();

						for (int i = 0; i < tbBotones.length; i++)
							if ( tbBotones[i].isSelected() ) {
								limpiarBoton(confTemp.setControl(jugador, NOMBRES[i].toLowerCase(), tecla));
								tbBotones[i].setText(texto);
								limpiarSeleccion();
							}
					}
				}

				private void limpiarBoton(Configuracion.Par par) {
					if (par != null && par.jugador == jugador) 
						for (int i = 0; i < NOMBRES.length; i++)
							if (NOMBRES[i].toLowerCase().equals(par.comando))
								tbBotones[i].setText("");
				}
			});
		}
	}
}

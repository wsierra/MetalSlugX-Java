import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class PanelConfMST extends Panel implements ActionListener {
	
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
	
	public PanelConfMST(Configuracion conf) {
		confJuego = conf;
		confTemp = new Configuracion(conf);
		cargarConf();
				
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
			SwingUtilities.windowForComponent(this).dispose();
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
			chPistas.add("Tema1");	//levantar nombres del directorio sonidos/pistas		
			chPistas.add("Tema2");			
			chPistas.add("Tema3");	
			chPistas.add("Tema4");			
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
			cbEstado.setState(confTemp.getEstadoSonido());
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
	}
	
	private class Controles extends Panel implements ActionListener {
		
		private final String[] NOMBRES = {"Arriba", "Izquierda", "Derecha", "Saltar", "Disparar", "Granada"};
		private JToggleButton[] botones;
		private ButtonGroup grupo = new ButtonGroup();

		public Controles() {
			
			botones = new JToggleButton[NOMBRES.length];		
			Panel panelBotones = new Panel(new GridLayout(botones.length, 1));
			JButton botonTamaño = new JButton("string largo 12345689");
			for (int i = 0; i < botones.length; i++) {
				botones[i] = new JToggleButton();
				botones[i].setPreferredSize(botonTamaño.getPreferredSize());
				botones[i].setRolloverEnabled(false);
				botones[i].addActionListener(this);
				panelBotones.add(botones[i]);
				grupo.add(botones[i]);
			}
			
			Panel panelNombres = new Panel(new GridLayout(NOMBRES.length, 1));
			for (String str : NOMBRES)
				panelNombres.add(new Label(str));

			setBackground(Color.WHITE);
			setLayout(new FlowLayout(FlowLayout.CENTER, BORDES, BORDES));
			add(panelNombres);
			add(panelBotones);
					
			tpPaneles.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (tpPaneles.getSelectedComponent() == controles) {
						grupo.clearSelection();
						requestFocus();
					}
				}
			});
			
			addKeyAdapter();
		}
		
		public void cargarConfiguracion() {
			for (int i = 0; i < botones.length; i++) {
				Integer tecla = confTemp.getControl(NOMBRES[i].toLowerCase());
				botones[i].setText(tecla != null ? KeyEvent.getKeyText(tecla) : "");
			}
			grupo.clearSelection();
		}	
				
		@Override
		public void actionPerformed(ActionEvent e) {
			requestFocus();
		}
		
		private void addKeyAdapter() {
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int tecla = e.getKeyCode();
					String texto = KeyEvent.getKeyText(tecla);
					e.consume();	
					
					for (int i = 0; i < botones.length; i++)
						if ( grupo.isSelected(botones[i].getModel()) ) {
							String eliminar = confTemp.setControl(NOMBRES[i].toLowerCase(), tecla);
							for (int j = 0; j < NOMBRES.length; j++)
								if (NOMBRES[j].toLowerCase().equals(eliminar))
									botones[j].setText("");
								
							botones[i].setText(texto);
							grupo.clearSelection();
						}
				}			
			});
		}
	}
	
	
	
	public static Configuracion conf = null;
	
	public static void main(String[] args) {
		
		
		new Frame() {
			
			Frame f = this;
			
			{
				Button b = new Button("configuracion");
				add(b, BorderLayout.CENTER);
				pack();
				setLocationRelativeTo(null);
				setVisible(true);
				
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} 
							catch (Exception ex) {
						}

						
						try {
							conf = new Configuracion("prueba.dat");
						}
						catch (java.io.IOException | ClassNotFoundException ex) {
							System.out.println(ex);
						}
						
						
						JDialog d = new JDialog(f);
						final PanelConfMST p = new PanelConfMST(conf);
						d.add(p);
						d.pack();
						d.setResizable(false);
						d.setLocationRelativeTo(null);
						d.setVisible(true);
						
						d.addWindowListener(new WindowAdapter() {
							public void windowClosed(WindowEvent e) {
								try {
									conf.guardar("prueba.dat");
								}
								catch (java.io.IOException ex){
									System.out.println(ex);
								}
							}
						});
					}	
				});
			}
		};
	
		
	}	

}

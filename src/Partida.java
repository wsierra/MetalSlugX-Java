import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Partida extends BloqueMST implements TempNotificador.TempListener {
	
	private static final float GRAVEDAD = 0.8f;
	private static final int MARGEN = 25;
	private static final int SEPARACION_SJ = 100;
	private static final int RAPIDEZ_CAMARA = 5;
	
	/*------------------------ubicacion info--------------------------*/
	private static final int SEPARACION = 10;
	private static final Point[] posicionesInfo= { 
		new Point(SEPARACION, SEPARACION), 
		new Point(MetalSlugT.getAncho() - LayoutInfoJugador.getAncho() - SEPARACION, SEPARACION),
		new Point(SEPARACION, SEPARACION + LayoutInfoJugador.getAlto() + SEPARACION), 
		new Point(MetalSlugT.getAncho() - LayoutInfoJugador.getAncho() - SEPARACION,
				  SEPARACION + LayoutInfoJugador.getAlto() + SEPARACION) 
	};
	/*----------------------------------------------------------------*/
	
	private MetalSlugT mst;
	private ArrayList<Nivel> niveles;
	private int nivelActual;
	private boolean nivelCompletado;
	
	private int posCamara;
	private int maximoCamara;
	private boolean avanceCamara = true;

	private TempNotificador temporizador;
	private boolean mostrarInfo;
	
	private ArrayList<SoldadoJugador> soldadosJugadores;
	private ArrayList<ControladorJugador> controladores;
	
	private ArrayList<SafeRemoveList> listasDeActivos;
	private SafeRemoveList<SoldadoJugador> soldadosJugadoresActivos;
	private SafeRemoveList<Efecto> efectosActivos;
	private SafeRemoveList<ObjetoCombate> enemigosActivos;
	private SafeRemoveList<Municion> municionesEnemigosActivas;
	private SafeRemoveList<Municion> municionesJugadoresActivas;
	private SafeRemoveList<Rehen> rehenesActivos;
	private SafeRemoveList<Bonus> bonusActivos;
	private SafeRemoveList<ObjetoDeJuego> otrosActivos;	
	
	public Partida(MetalSlugT mst, SoldadoJugador[] soldados) {
		
		this.mst = mst;
		
		soldadosJugadores = new ArrayList<SoldadoJugador>(MetalSlugT.MAXJUGADORES);
		soldadosJugadoresActivos = new SafeRemoveList<SoldadoJugador>(MetalSlugT.MAXJUGADORES);
		for (int i = 0; i < soldados.length; i++)
			if (soldados[i] != null) {
				soldados[i].setJugador(new Jugador(i));
				soldadosJugadores.add(soldados[i]);
			}		
	}

	@Override
	public void inicializar() {          
		
		TrucosMST.inicializar(mst, soldadosJugadores);
		
		listasDeActivos = new ArrayList<SafeRemoveList>();
		listasDeActivos.add(otrosActivos = new SafeRemoveList<ObjetoDeJuego>());
		listasDeActivos.add(enemigosActivos = new SafeRemoveList<ObjetoCombate>());
		listasDeActivos.add(rehenesActivos = new SafeRemoveList<Rehen>());
		listasDeActivos.add(bonusActivos = new SafeRemoveList<Bonus>());
		listasDeActivos.add(municionesEnemigosActivas = new SafeRemoveList<Municion>());
		listasDeActivos.add(municionesJugadoresActivas = new SafeRemoveList<Municion>());
		listasDeActivos.add(efectosActivos = new SafeRemoveList<Efecto>());
		listasDeActivos.add(soldadosJugadoresActivos);
		
		niveles = new ArrayList<Nivel>();
		niveles.add(new Nivel1());				
		
		nivelActual = 0;
		cargarNivel();
		
		controladores = new ArrayList<ControladorJugador>();
		for (SoldadoJugador s : soldadosJugadores) {
			ControladorJugador c = new ControladorJugador(s);
			controladores.add(c);
			mst.addKeyListener(c);
		}
		
		addAyuda("F5: Info partida on/off");
		mostrarInfo = true;
		mst.addKeyListener(new KeyAdapter() {
			@Override public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5)
					mostrarInfo = !mostrarInfo;
			}
		});
		
		MetalSlugT.musica.reproducirNuevo(MetalSlugT.DIR_PISTAS.resolve(mst.getConf().getPista() + ".wav"), mst.getConf().getLoopPista());
	}

	@Override
	public void actualizar() {
		DebugInfo.addLinea("Camara: " + posCamara);
		
		for (ControladorJugador c : controladores)
			c.actualizar();	
		
		actualizarObjetos();
		verificarColisiones();

		for (ControladorJugador c : controladores)
			c.limitarPosicion();
		
		if (avanceCamara)
			actualizarCamara();
			
		temporizador.actualizar();
		verificarEstado();
	}
	
	@Override
	public void pintarBloque(Graphics2D g) {
		
		g.translate(-posCamara, 0);
		g.drawImage(niveles.get(nivelActual).getFondo(), 0, 0, null);	
		
		for (SafeRemoveList<ObjetoDeJuego> la : listasDeActivos)
			for (ObjetoDeJuego o : la) {
				o.pintar(g);
				DebugInfo.addLinea(o.toString()); 						
			}
		
		g.translate(posCamara, 0);
		
		if (mostrarInfo) { 
			Textos.setBorde(java.awt.Color.BLACK);	
			Textos.setRelleno(java.awt.Color.WHITE);
			Textos.pintar(g, String.valueOf(temporizador.getTiempo()), mst.getAncho() / 2, 70, 80, Textos.CENTRO);
			for (int i = 0; i < soldadosJugadores.size(); i++)
				LayoutInfoJugador.pintar(g, soldadosJugadores.get(i), posicionesInfo[i]);
		}
		
		DebugInfo.pintar(g, 10);
	} 		
	
	@Override
	public void finalizar() {
		// TODO 
		// liberar recursos nivel
		// quitar listeners
		MetalSlugT.musica.finalizarTodo();
		MetalSlugT.sfx.finalizarTodo();
	}
	
	@Override
	public void notificarFin() {
		for (SoldadoJugador s : soldadosJugadoresActivos)
			s.morir();
	}

	private void actualizarCamara() {
		if (!soldadosJugadoresActivos.isEmpty()) {
			int ultimaPosCamara = posCamara;
			calcularPosCamara();
			if (posCamara != ultimaPosCamara)
				niveles.get(nivelActual).activar(ultimaPosCamara, posCamara, this);
		}
	}
	
	private void calcularPosCamara() {	
			
		int sumaX = 0;
		int minX = Integer.MAX_VALUE;	
		
		for (SoldadoJugador s : soldadosJugadoresActivos) {
			int izq = s.getLadoIzquierdo();
			sumaX += ( izq + s.getLadoDerecho() ) / 2;
			if (izq < minX)
				minX = izq;
		}

		int nuevaPosCamara = sumaX / soldadosJugadoresActivos.size() - MetalSlugT.getAncho() / 2;

		DebugInfo.addLinea("Camara promedio: " + nuevaPosCamara);
		
		if (nuevaPosCamara > posCamara) {		
			if (nuevaPosCamara > maximoCamara) 
				nuevaPosCamara = maximoCamara;
			if (nuevaPosCamara > minX - MARGEN)	
				nuevaPosCamara = minX - MARGEN;
				
			if (nuevaPosCamara - posCamara > RAPIDEZ_CAMARA)
				posCamara += RAPIDEZ_CAMARA;
			else 
				posCamara = nuevaPosCamara;	
		}
	}
	
	private void actualizarObjetos() {
		for (SafeRemoveList<ObjetoDeJuego> la : listasDeActivos) {
			for (int i = 0, tam = la.size(); i < tam; i++)
				la.get(i).actualizar();
			la.compactar();
		}
	}
	
	private void verificarColisiones() {
		
		for (Municion mj : municionesJugadoresActivas) {
			boolean continuar = true;
			for (int i = 0; continuar && i < enemigosActivos.size(); i++)
				continuar = mj.colisionar(enemigosActivos.get(i));
			for (int i = 0; continuar && i < municionesEnemigosActivas.size(); i++)
				continuar = mj.colisionar(municionesEnemigosActivas.get(i));
			for (int i = 0; continuar && i < rehenesActivos.size(); i++)
				continuar = mj.colisionar(rehenesActivos.get(i));
		}

		for (Municion me : municionesEnemigosActivas) {
			for (SoldadoJugador sj : soldadosJugadoresActivos)
				if (!me.colisionar(sj))
					break;
		}
		
		for (SoldadoJugador sj : soldadosJugadoresActivos) {
			for (Bonus b : bonusActivos)
				sj.colisionar(b);
			for (Rehen r : rehenesActivos) {
				sj.colisionar(r);}
		}					
	}
	
	
	private void verificarEstado() {
		if (nivelCompletado) {
			if (nivelActual + 1 < niveles.size())
				avanzarNivel();
			else
				finPartida();
		}
		else if (soldadosJugadoresActivos.isEmpty()) {
			boolean continuar = false;
			
			for (SoldadoJugador s : soldadosJugadores)
				if (s.quedanVidas())
					continuar = true;
					
			if (continuar) 
				reiniciarNivel();
			else 
				finPartida();
		}
	}
	
	public void nivelFinalizado() {
		nivelCompletado = true;
	}
	
	private void finPartida() {
		List<Jugador> jugadores = new ArrayList<Jugador>();
		for (SoldadoJugador s : soldadosJugadores)
			jugadores.add(s.getJugador());
			
		mst.getEjecucion().cambiarBloque(new Ranking(mst, jugadores));		
	}
	
	private void cargarNivel() {
		niveles.get(nivelActual).cargar();
		posCamara = 0;
		maximoCamara = niveles.get(nivelActual).getAncho() - MetalSlugT.getAncho();
		temporizador = new TempNotificador(niveles.get(nivelActual).getTiempoMaximo(), this);
		for (SoldadoJugador s : soldadosJugadores)
			if (s.quedanVidas() && !soldadosJugadoresActivos.contains(s))
				s.activar(this);
	}
	
	private void reiniciarNivel() {
		limpiarListasActivos();
		niveles.get(nivelActual).liberarRecursos();
		cargarNivel();
		nivelCompletado = false;
		setAvanceCamara(true);
		
		int posX = 0;
		for (SoldadoJugador s : soldadosJugadoresActivos) {
			posX += SEPARACION_SJ;
			s.desplazar(posX - s.getLadoIzquierdo(), -s.getLadoInferior());
		}
	}
	
	private void avanzarNivel() {
		nivelActual++;
		List<SoldadoJugador> sjVivos = (List<SoldadoJugador>)soldadosJugadoresActivos.clone();
		reiniciarNivel();
		for (SoldadoJugador s : sjVivos)
			s.reset();
	}
	
	private void limpiarListasActivos() {
		for (List lista : listasDeActivos)
			if (lista != soldadosJugadoresActivos)
				lista.clear();
	}
	
	public void aumentarTiempo(int tiempo) {
		temporizador = new TempNotificador(temporizador.getTiempo() + tiempo, this);
	}
	
	public void setAvanceCamara(boolean avance) {
		avanceCamara = avance;
	}
	
	public int getCantSoldadosJugActivos() {
		return soldadosJugadoresActivos.size(); 	
	}
	
	public boolean entreDosJugadores(Point posicion) {
		
		Point extremoD = null;
		Point extremoI = null;
		
		for(SoldadoJugador s : soldadosJugadoresActivos) {
			Point soldado = s.getPosicion();
				if(soldado.x <= posicion.x)
					extremoI = soldado;
				else 
					extremoD = soldado;
		}
		return (extremoD != null && extremoI != null);
	}
	
	public Point getPosicionSJMasCercano(Point posicion) {
		Point masCercano = posicion;
		int min = Integer.MAX_VALUE;
		
		for (SoldadoJugador s : soldadosJugadoresActivos) {
			int distancia = (int)Math.abs(s.getPosicion().x - posicion.x);
			if (distancia < min) {
				min = distancia;
				masCercano = s.getPosicion();
			}
		}
		return masCercano;
	}
	
	public int getSuelo(int x) {
		return niveles.get(nivelActual).getSuelo(x);
	}
	
	public Rectangle getCamara() {
		return new Rectangle(posCamara, 0, MetalSlugT.getAncho(), MetalSlugT.getAlto());
	}
	
	public float getGravedad() {
		return GRAVEDAD;
	}
	
	public void notificacionPausa(int evento) {
		if(evento == PAUSADO)
			temporizador.setPausa(true);
		else if(evento == REANUDADO)
			temporizador.setPausa(false);
	}			
	
	public void addActivo(ObjetoDeJuego o) {
		if (o instanceof SoldadoJugador)
			soldadosJugadoresActivos.add((SoldadoJugador)o);
		else if (o instanceof Enemigo)
			enemigosActivos.add((ObjetoCombate)o);
		else if (o instanceof Municion) {
			if ( ((Municion)o).esEnemiga() )
				municionesEnemigosActivas.add((Municion)o);
			else
				municionesJugadoresActivas.add((Municion)o);
		}	
		else if (o instanceof Rehen)			
			rehenesActivos.add((Rehen)o);
		else if (o instanceof Bonus)
			bonusActivos.add((Bonus)o);
		else if (o instanceof Efecto)
			efectosActivos.add((Efecto)o);
		else
			otrosActivos.add(o);	
	}
	
	public void removeActivo(ObjetoDeJuego o) {
		if (o instanceof SoldadoJugador)
			soldadosJugadoresActivos.remove(o);
		else if (o instanceof Enemigo)
			enemigosActivos.remove(o);
		else if (o instanceof Municion) {
			if ( ((Municion)o).esEnemiga() )
				municionesEnemigosActivas.remove(o);
			else
				municionesJugadoresActivas.remove(o);
		}	
		else if (o instanceof Rehen)			
			rehenesActivos.remove(o);
		else if (o instanceof Bonus)
			bonusActivos.remove(o);
		else if (o instanceof Efecto)
			efectosActivos.remove(o);
		else
			otrosActivos.remove(o);
	}

	private class ControladorJugador extends KeyAdapter {
		
		private final SoldadoJugador soldado;
		private final int numJug;
		
		public ControladorJugador(SoldadoJugador soldado) {
			this.soldado = soldado;
			numJug = soldado.getJugador().getNumJugador();
		}
		
		public void actualizar() {
			if ( mst.isKeyPressed( mst.getConf().getControl(numJug, "derecha") ) &&
				 mst.isKeyPressed( mst.getConf().getControl(numJug, "izquierda") ) )
				soldado.detener();
		}
		
		public void limitarPosicion() {
			final int topeIzq = posCamara + MARGEN;
			final int topeDer = posCamara + MetalSlugT.getAncho() - MARGEN;
			
			if (soldado.getLadoIzquierdo() < topeIzq)
				soldado.desplazar(topeIzq - soldado.getLadoIzquierdo(), 0);
			else if (soldado.getLadoDerecho() > topeDer)
				soldado.desplazar(topeDer - soldado.getLadoDerecho(), 0);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			final Integer kcp = e.getKeyCode();

			if ( kcp.equals( mst.getConf().getControl(numJug, "saltar") ) )
				soldado.saltar();
			else if ( kcp.equals( mst.getConf().getControl(numJug, "disparar") ) )
				soldado.disparar();
			else if ( kcp.equals( mst.getConf().getControl(numJug, "granada") ) )
				soldado.lanzarGranada();
			else if ( kcp.equals( mst.getConf().getControl(numJug, "arriba") ) )
				soldado.apuntarArriba(true);
			else {
				final Integer der = mst.getConf().getControl(numJug, "derecha");
				final Integer izq = mst.getConf().getControl(numJug, "izquierda");
				
				if ( kcp.equals(der) && !mst.isKeyPressed(izq) )
					soldado.avanzarDerecha();
				else if ( kcp.equals(izq) && !mst.isKeyPressed(der) )
					soldado.avanzarIzquierda();
			}			
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			final Integer kcr = e.getKeyCode();
						
			if ( kcr.equals( mst.getConf().getControl(numJug, "arriba") ) )
				soldado.apuntarArriba(false);
			else {
				final Integer der = mst.getConf().getControl(numJug, "derecha");
				final Integer izq = mst.getConf().getControl(numJug, "izquierda");
						
				if ( kcr.equals(der) && mst.isKeyPressed(izq) )
					soldado.avanzarIzquierda();
				else if ( kcr.equals(izq) && mst.isKeyPressed(der) )
					soldado.avanzarDerecha();
				else if ( kcr.equals(der) || kcr.equals(izq) )
					soldado.detener();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new MetalSlugT().correr(null);
		}
		catch (Juego.EjecucionException e) {}
	}
}

import java.awt.Point;

public abstract class Soldado extends ObjetoCombate {

	private boolean caidaIniciada;
	private boolean apuntandoArriba;
	private boolean lanzandoGranada;
	private boolean avanzando;
	private int voltear;
	private Point ultimaMira;

	private Arma arma;
	private int cantidadGranadas;

	private Point miraHor;
	private Point miraVert;
	private Point miraDiag;
	
	private float rapidez;
	private float impulsoSalto;
	private float anguloGranada;
	private float impulsoGranada;
	
	protected static final String PARADO = "parado";
	protected static final String AVANZANDO = "avanzando";
	protected static final String SALTANDO = "saltando";
	protected static final String DIAGONAL = "diagonal";
	protected static final String ARRIBA = "arriba";
	protected static final String GRANADA = "granada";
	protected static final String MURIENDO = "muriendo";

	public Soldado(int energia, Point posicion, Sprite sprite) {
		super(energia, posicion, sprite);
		voltear = AdminSprite.NINGUNO;
		setVulnerable(true);
		arma = new ArmaBase( new Point( (getLadoIzquierdo() + getLadoDerecho()) / 2, getLadoSuperior() ) );
		arma.setPortador(this);
		ultimaMira = new Point(0, 0);
	}

	@Override
	public void actualizar() {
		mover();

		boolean aire = enAire();
		if (!aire && caidaIniciada) {
			caidaIniciada = false;
			iniciarMovimientoY(null);
			if (!enSuelo())
				posicionarEnSuelo();
		}
		else if (aire && !caidaIniciada) {
			caidaIniciada = true;
			iniciarMovimientoY(new Caida(0.0f));
		}

		if (isVivo()) {
			if (lanzandoGranada) {
				if (actualizarSecuencia(GRANADA, voltear)) {
					lanzandoGranada = false;
					crearGranada();
				}
			}
			else {
				String sec;
				if (caidaIniciada)
					sec = SALTANDO;
				else if (avanzando)
					sec = AVANZANDO;
				else
					sec = PARADO;

				if (apuntandoArriba)
					sec += (arma.evaluarDireccion(Arma.DIAGONAL) && avanzando ? DIAGONAL : ARRIBA);

				actualizarSecuencia(sec, voltear);
			}
			
			if (arma.isDescargada())
				setArma(new ArmaBase());
				
			actualizarMira();
		}
		else if (actualizarSecuencia(MURIENDO, voltear))
			desactivar();

		verificarDesactivacion();
	}

	@Override
	public void desplazar(int despX, int despY) {
		super.desplazar(despX, despY);
		arma.desplazar(despX, despY);
	}

	@Override
	public void activar(Partida mundo) {
		super.activar(mundo);
		arma.activar(mundo);
	}

	@Override
	public void desactivar() {
		super.desactivar();
		arma.desactivar();
	}

	@Override
	protected void notificacionMuerte() {
		detener();
		apuntandoArriba = false;
		lanzandoGranada = false;
	}
	
	public int getCantGranadas() {
		return cantidadGranadas;
	}
	
	public void setCantGranadas(int cantGranadas) {
		cantidadGranadas = cantGranadas;
	}
	
	public void aumentarCantGranadas(int cantGranadas) {
		cantidadGranadas += cantGranadas;
	}
	
	public void recargarArma(int cantMunicionesRecarga) {
		arma.recargar(cantMunicionesRecarga);
	}
	
	protected Arma getArma() {
		return arma;
	}
	
	public Class getClassArma() { 
		return arma.getClass();
	}
	
	protected void saltar() {
		if (isVivo() && !caidaIniciada && !enAire()) {
			caidaIniciada = true;
			iniciarMovimientoY(new Caida(impulsoSalto));
		}
	}
	
	protected void apuntarArriba(boolean arriba) {
		if (isVivo())
			apuntandoArriba = arriba;
	}

	protected void mirarDerecha() {
		if (isVivo())
			voltear = AdminSprite.NINGUNO;
	}

	protected void avanzarDerecha() {
		if (isVivo()) {
			setVelocidadX(rapidez);
			avanzando = true;
			voltear = AdminSprite.NINGUNO;
		}
	}

	protected void avanzarIzquierda() {
		if(isVivo()) {
			setVelocidadX(-rapidez);
			avanzando = true;
			voltear = AdminSprite.HORIZONTAL;
		}
	}

	protected void detener() {
		setVelocidadX(0.0f);
		avanzando = false;
	}

	protected void disparar() {
		if (isVivo() && !lanzandoGranada)
			arma.disparar();
	}

	protected void lanzarGranada() {
		if (isVivo() && cantidadGranadas != 0)
			lanzandoGranada = true;
	}

	protected void setMiras(Point horizontal, Point vertical, Point diagonal) {
		miraHor = horizontal;
		miraVert = vertical;
		miraDiag = diagonal;
	}

	protected void setImpulsoSalto(float impulso) {
		impulsoSalto = impulso;
	}

	protected float getImpulsoSalto() {
		return impulsoSalto;
	}

	protected void setRapidez(float rapidez) {
		this.rapidez = rapidez;
	}

	protected float getRapidez() {
		return rapidez;
	}

	protected void setAnguloGranada(float angulo) {
		anguloGranada = angulo;
	}
	
	protected void setImpulsoGranada(float impulso) {
		impulsoGranada = impulso;
	}

	private void actualizarMira() {

		int direccion = 0;

		if (apuntandoArriba)
			direccion = Municion.ARRIBA;
		if ( !apuntandoArriba || avanzando && arma.evaluarDireccion(Arma.DIAGONAL) )
			direccion |= voltear == AdminSprite.NINGUNO ? Municion.DERECHA : Municion.IZQUIERDA;

		Point nuevaMira = calcularMira(direccion);

		if ( !ultimaMira.equals(nuevaMira) ) {
			arma.setDireccionDisparo(direccion);
			arma.desplazar(nuevaMira.x - ultimaMira.x, nuevaMira.y - ultimaMira.y);
			ultimaMira = nuevaMira;
		}
	}
	
	public void setArma(Arma nuevaArma) {
		arma.desactivar();
		arma = nuevaArma;
		arma.desplazar( (getLadoIzquierdo() + getLadoDerecho()) / 2 - arma.getPosicion().x,
						 getLadoSuperior() - arma.getPosicion().y );
		arma.setPortador(this);
		arma.activar(mundo);
		ultimaMira.move(0, 0);
	}
	
	private void crearGranada() {
		Point pos = new Point(getLadoDerecho(), getLadoSuperior());
		float angulo = anguloGranada;
		if (voltear == AdminSprite.HORIZONTAL) {
			angulo = 180.0f - angulo;
			pos.x = getLadoIzquierdo();
		}
		Granada g = new Granada(pos, angulo, impulsoGranada, this, mundo);
		g.desplazar(-g.getAncho() / 2, 0);
		cantidadGranadas--;
	}
	
	private Point calcularMira(int direccion) {

		int despX = 0;
		int despY = 0;

		switch (direccion) {
			case Municion.DERECHA :
				despX = miraHor.x;
				despY = miraHor.y;
				break;
			case Municion.IZQUIERDA :
				despX = -miraHor.x;
				despY = miraHor.y;
				break;
			case Municion.ARRIBA :
				despX = voltear == AdminSprite.NINGUNO ? miraVert.x : -miraVert.x;
				despY = miraVert.y;
				break;
			case Municion.DIAG_DERECHA :
				despX = miraDiag.x;
				despY = miraDiag.y;
				break;
			case Municion.DIAG_IZQUIERDA :
				despX = -miraDiag.x;
				despY = miraDiag.y;
				break;
		}

		return new Point(despX, despY);
	}
}

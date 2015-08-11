import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class SoldadoJugador extends Soldado {

	private static final int ENERGIA = 30;
	private static final float RAPIDEZ = 3.0f;
	private static final float IMPULSO = -15.0f;
	private static final int CANT_GRANADAS = 10;
	private static final float ANGULO_GRANADA = 35.0f;
	private static final float IMPULSO_GRANADA = 15.0f;
	
	private int vidas = 3;
	private Jugador jugador;
	
	public SoldadoJugador(Point posicion, Sprite sprite) {
		super(0, posicion, sprite);
		setRapidez(RAPIDEZ);
		setImpulsoSalto(IMPULSO);
		setAnguloGranada(ANGULO_GRANADA);
		setImpulsoGranada(IMPULSO_GRANADA);
	}

	@Override
	public void actualizar() {
		if (mundo != null)
			super.actualizar();
		else
			actualizarSecuencia(AVANZANDO, AdminSprite.NINGUNO);
	}

	@Override
	public void avanzarDerecha() {
		super.avanzarDerecha();
	}

	@Override
	public void avanzarIzquierda() {
		super.avanzarIzquierda();
	}

	@Override
	public void saltar() {
		super.saltar();
	}

	@Override
	public void disparar() {
		super.disparar();
	}

	@Override
	public void detener() {
		super.detener();
	}

	@Override
	public void lanzarGranada() {
		super.lanzarGranada();
	}

	@Override
	public void apuntarArriba(boolean arriba) {
		super.apuntarArriba(arriba);
	}
	
	@Override
	public boolean recibirDanio(int danio) {
		return super.recibirDanio(1);
	}
	
	@Override
	public void activar(Partida mundo) {
		super.activar(mundo);
		vidas--;
		reset();
	}
	
	public void reset() {
		recibirCura(ENERGIA - getEnergia());
		setArma(new ArmaBase());
		setCantGranadas(CANT_GRANADAS);
		mirarDerecha();
	}
	
	public void morir() {
		super.recibirDanio(getEnergia());
	}
	
	public int getCantMuniciones() {
		return getArma().getCantMuniciones();
	}
	
	public int getVidas() {
		return vidas;
	}
	
	public boolean quedanVidas() {
		return vidas != 0;
	}
	
	public void colisionar(Rehen r) {
		if (intersecar(r))
			r.soltarBonus();
	}
	
	public void colisionar(Bonus b) {
		if (intersecar(b))
			b.consumirBonus(this);
	}

	public void setJugador(Jugador j) {
		jugador = j;
	}

	public Jugador getJugador() {
		return jugador;
	}
}

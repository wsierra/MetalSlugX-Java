import java.awt.Point;

public class GrupoHelicopteros extends Grupo {
	
	private static final int CENTRO_Y = 100;
	
	private final Trigger triggerActivacion = new Trigger(40);
	private final Trigger triggerDisparo = new Trigger(30);
	
	private int indiceActivacion;
	private int indiceFrente;
	
	public GrupoHelicopteros(Point posicionCamara, int cantidad) {
		super(posicionCamara);
		for (int i = 0; i < cantidad; i++) {
			Point centro = getPosicion();
			centro.translate(MetalSlugT.getAncho() / 2, CENTRO_Y);
			miembros.add(new Helicoptero2(centro));
		}
		indiceActivacion = 1;
	}
	
	@Override
	public void actualizar() {
		
		if (indiceActivacion < miembros.size() && triggerActivacion.test())
			miembros.get(indiceActivacion++).activar(mundo);
		
		while ( indiceFrente < miembros.size() && 
				!((ObjetoCombate)miembros.get(indiceFrente)).isVivo() )
			indiceFrente++;
		
		if (indiceFrente < miembros.size()) {
			if (triggerDisparo.test())
				((Helicoptero2)miembros.get(indiceFrente)).disparar();
		}
		else {
			mundo.setAvanceCamara(true);
			desactivar();
		}
	}
	
	@Override
	public void activar(Partida mundo) {
		super.activar(mundo);
		miembros.get(0).activar(mundo);
		mundo.setAvanceCamara(false);
	}
}

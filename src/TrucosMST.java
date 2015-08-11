import java.util.ArrayList;

public class TrucosMST {
	
	private static ArrayList<Truco> trucos = new ArrayList<Truco>();
		
	public static void inicializar(MetalSlugT mst, ArrayList<SoldadoJugador> soldados) {
		
		for (SoldadoJugador s : soldados) {
			trucos.add(new CambiarArma("p", ArmaBase.class, s));
			trucos.add(new CambiarArma("h", ArmaH.class, s));
			trucos.add(new CambiarArma("r", ArmaR.class, s));
			trucos.add(new CambiarArma("s", ArmaS.class, s));
			trucos.add(new Recargar(s));
			trucos.add(new RecargarGranadas(s));
			trucos.add(new Energia(s));
		}
		
		for (Truco t : trucos)
			mst.addKeyListener(t);	
	}
	
	public static void finalizar(MetalSlugT mst) {
		for (Truco t : trucos)
			mst.removeKeyListener(t);	
		trucos.clear();
	}
	
	
	/*-------------------trucos-------------------*/
	
	private static class CambiarArma extends TrucoPorJugador {
				
		private Class<Arma> clase;
		 
		public CambiarArma(String clave, Class claseArma, SoldadoJugador soldado) {  
			super(clave, soldado);
			clase = claseArma;
		}
		
		@Override 
		protected void realizarAccion(SoldadoJugador soldado) {
			try {
				soldado.setArma(clase.newInstance());
			}
			catch (InstantiationException | IllegalAccessException ex) {} 
		}
	}
	
	private static class Recargar extends TrucoPorJugador {
		public Recargar(SoldadoJugador soldado) { super("m", soldado); }
		@Override protected void realizarAccion(SoldadoJugador soldado) { soldado.getArma().recargar(1000); }
	}
	
	private static class RecargarGranadas extends TrucoPorJugador {
		public RecargarGranadas(SoldadoJugador soldado) { super("g", soldado); }
		@Override protected void realizarAccion(SoldadoJugador soldado) { soldado.aumentarCantGranadas(100); }
	}
	
	private static class Energia extends TrucoPorJugador {
		public Energia(SoldadoJugador soldado) { super("e", soldado); }
		@Override protected void realizarAccion(SoldadoJugador soldado) { soldado.recibirCura(10 - soldado.getEnergia()); }
	}
	
	//~private static class Pistola extends TrucoPorJugador {
		//~public Pistola(SoldadoJugador soldado) { super("pistola", soldado); }
		//~@Override protected void realizarAccion(SoldadoJugador soldado) { soldado.setArma(new ArmaBase()); }
	//~}
	//~
	//~private static class HeavyMachineGun extends TrucoPorJugador {
		//~public HeavyMachineGun(SoldadoJugador soldado) { super("heavymachinegun", soldado); }
		//~@Override protected void realizarAccion(SoldadoJugador soldado) { soldado.setArma(new ArmaH()); }
	//~}
	//~
	//~private static class RocketLauncher extends TrucoPorJugador {
		//~public RocketLauncher(SoldadoJugador soldado) { super("rocketlauncher", soldado); }
		//~@Override protected void realizarAccion(SoldadoJugador soldado) { soldado.setArma(new ArmaR()); }
	//~}
	//~
	//~private static class Shotgun extends TrucoPorJugador {
		//~public Shotgun(SoldadoJugador soldado) { super("pistola", soldado); }
		//~@Override protected void realizarAccion(SoldadoJugador soldado) { soldado.setArma(new ArmaS()); }
	//~}
}

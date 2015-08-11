public class TempNotificador extends Temporizador {
	
	private TempListener listener;

	public TempNotificador(int tiempo, TempListener listener) {
		super(tiempo);
		this.listener = listener;
	}
	
	public void actualizar() {
		if (listener != null && finalizo()) {
			listener.notificarFin();
			listener = null;
		}
	}
	
	public interface TempListener {
		void notificarFin();		
	}
}

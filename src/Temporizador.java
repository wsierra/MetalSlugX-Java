public class Temporizador {
	
	private long fin;
	private Long pausa;
	
	public Temporizador(int tiempo) {
		fin = System.currentTimeMillis() + tiempo * 1000L;
	}
	
	public int getTiempo() {
		int dif = (int)Math.ceil( (fin - System.currentTimeMillis()) / 1000.0 );
		return dif > 0 ? dif : 0;
	}
	
	public void setPausa(boolean valorPausa) {
		if (valorPausa && pausa == null)
			pausa = System.currentTimeMillis();
		else if (!valorPausa && pausa != null) {
			fin += System.currentTimeMillis() - pausa;
			pausa = null;
		}
	}
	
	public boolean finalizo() {
		return getTiempo() == 0;
	}
}

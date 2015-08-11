import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class BloqueMST implements Juego.Bloque {

	private static final int FUENTE = 14;
	private static final int MARGEN = 3;
	
	private static boolean ayudaVisible;
	private static ArrayList<String> ayudasMST = new ArrayList<String>();
	private ArrayList<String> ayudasBloque = new ArrayList<String>();
	
	public static void setAyudaVisible(boolean visible) {
		ayudaVisible = visible;
	}
	
	public static void addAyudaGeneral(String ayuda) {
		ayudasMST.add(ayuda);
	}
	
	public static void borrarAyudaGeneral() {
		ayudasMST.clear();
	}
	
	protected void addAyuda(String ayuda) {
		ayudasBloque.add(ayuda);
	}
	
	@Override
	public final void pintar(Graphics2D g) {
		
		Graphics2D gBloque = (Graphics2D)g.create();
		pintarBloque(gBloque);
		gBloque.dispose();
		
		if (ayudaVisible) {
			final int altura = FUENTE * (ayudasMST.size() + ayudasBloque.size()) + MARGEN;
			int y = MetalSlugT.getAlto() - altura;
			g.setColor(Color.BLACK);
			g.fillRect(0, y, MetalSlugT.getAncho(), altura);
			
			g.setColor(Color.GREEN);
			g.setFont(g.getFont().deriveFont((float)FUENTE));
			for (String str : ayudasMST) {
				y += FUENTE;
				g.drawString(str, MARGEN, y);
			}
			for (String str : ayudasBloque) {
				y += FUENTE;
				g.drawString(str, MARGEN, y);
			}
		}
	}
	
	public void notificacionPausa(int evento) {}		
	
	protected abstract void pintarBloque(Graphics2D g);
}

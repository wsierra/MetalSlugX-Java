import java.awt.geom.AffineTransform;
import java.awt.font.TextLayout;
import java.awt.*;

public class Textos {

	public static final int IZQUIERDA = 0;
	public static final int DERECHA = 1;
	public static final int CENTRO = 2;
	
	private static Color relleno;
	private static Color borde;
	
	private Textos() {}
	
	public static void setRelleno(Color color) {
		relleno = color;
	}
	
	public static void setBorde(Color color) {
		borde = color;
	}
	
	public static void pintar(Graphics2D g, String str, int x, int y, int tamanio, int alineacion) {	
		
		g.setFont( g.getFont().deriveFont((float)tamanio) );
		
		AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
		if (alineacion == CENTRO)
			transform.translate(-g.getFontMetrics().stringWidth(str) / 2, 0);
		else if (alineacion == DERECHA)
			transform.translate(-g.getFontMetrics().stringWidth(str), 0);
			
		TextLayout layout = new TextLayout(str, g.getFont(), g.getFontRenderContext());
		Shape shape = transform.createTransformedShape(layout.getOutline(null));		
		
		Stroke backupTrazo = g.getStroke();
		g.setStroke(new BasicStroke(tamanio >= 40 ? tamanio / 20 : 1));	
		g.setColor(borde);
		g.draw(shape);
		g.setStroke(backupTrazo);
		
		g.setColor(relleno);
		g.fill(shape);		
	}
}

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;

public class DebugInfo {
	
	private static boolean on;  
	
	private static ArrayList<String> buffer = new ArrayList<String>();
	
	private DebugInfo() {}
	
	public static void setOn(boolean on) {
		DebugInfo.on = on;
	}
	
	public static boolean isOn() {
		return on;
	}
	
	public static void addLinea(String str) {
		if (on) 
			buffer.add(str);
	}
	
	public static void addLinea(int str) {
		addLinea(String.valueOf(str));
	}
	
	public static void pintar(Graphics2D g, int tamanio) {
		
		if (on) {
			g.setColor(Color.BLACK);
			g.setFont( g.getFont().deriveFont((float)tamanio) );
			
			final FontMetrics fm = g.getFontMetrics();
			int maxLong = 0;
			int x = -10;
			int cantLineas = g.getClipBounds().height / tamanio;
			
			for (int i = 0; i < buffer.size(); i++) {
				
				int y = (i % cantLineas + 1) * tamanio;
				
				if (y == tamanio) {
					x += maxLong + 10;
					maxLong = 0;
				}
				
				g.drawString(buffer.get(i), x, y);
				
				int longitud = fm.stringWidth(buffer.get(i));
				if (longitud > maxLong)
					maxLong = longitud;
			}
			
			buffer.clear();
		}
	}
}

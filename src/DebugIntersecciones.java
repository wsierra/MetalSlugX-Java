import java.awt.*;

public class DebugIntersecciones extends Frame {

	Rectangle r1;
	Rectangle r2;
	Color c;
	Partida mundo;
	
	public DebugIntersecciones(Partida mundo) {
		this.mundo = mundo;
		//~ can.setPreferredSize(new Dimension(640, 480));
		//~ add(can);
		//~ pack();
		setSize(640, 480);
		setVisible(true);
	}
	
	public void cargar(Rectangle r1, Rectangle r2) {
		c = new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random()));
		this.r1 = r1;
		this.r2 = r2;
		repaint();
	}
	
		@Override
		public void paint(Graphics g) {
			System.out.println("dfsgh");
			if (mundo != null) {
				
				g.translate(-mundo.getCamara().x,0);
				g.drawString(String.valueOf(-mundo.getCamara().x), 100,100);
				g.setColor(Color.BLACK);
				g.drawRect(50, 50, 100, 100);
			}
			
			if (r1 != null) {
				
				g.setColor(c);
				g.drawRect(r1.x, r1.y, r1.width, r1.height);
				g.drawRect(r2.x, r2.y, r2.width, r2.height);
			}
		}
}

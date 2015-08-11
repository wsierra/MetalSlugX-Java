import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ComponentImagen extends Component {

	private BufferedImage imagen;

	public ComponentImagen(BufferedImage imagen) {
		setImagen(imagen);
	}
	
	public ComponentImagen(Path path) {
		setImagen(path);
	}
	
	public ComponentImagen(String path) {
		setImagen(path);
	}
	
	public void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
		repaint();
	}
	
	public void setImagen(Path path) {
		setImagen(path.toString());
	}
	
	public void setImagen(String path) { 	
		try {                
			imagen = ImageIO.read(new File(path));
		} 
		catch (IOException ex) {
			imagen = null;
		}
		repaint();			
	}
	
	private Dimension calcularDefaultPreferredSize() {
		return imagen != null ? new Dimension(imagen.getWidth(), imagen.getHeight()) : new Dimension(180, 120);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return isPreferredSizeSet() ? super.getPreferredSize() : calcularDefaultPreferredSize();
	}
	
	@Override
	public void paint(Graphics g) {
		if (imagen != null) {
			float escala = Math.min( (float)getWidth() / imagen.getWidth(), 
									 (float)getHeight() / imagen.getHeight() );			 
			Image im = imagen.getScaledInstance( (int)( escala * imagen.getWidth() ), 
												 (int)( escala * imagen.getHeight() ), 
												 Image.SCALE_DEFAULT );
			g.drawImage(im, (getWidth() - im.getWidth(null)) / 2, (getHeight() - im.getHeight(null)) / 2, null);
		}
		else
			g.drawString("No se pudo cargar la imagen", getWidth() / 2 - 80, getHeight() / 2);
	}
}

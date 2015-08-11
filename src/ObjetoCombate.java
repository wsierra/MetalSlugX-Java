import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ObjetoCombate extends ObjetoMovil {
	
	private static final float OPACIDAD_IMPACTO = 0.4f;
	private static final int DURACION_IMPACTO = 5;
	
	private int energiaRestante;
	private boolean vulnerable;
	private int contDurImpacto;
	
	public ObjetoCombate(int energia, Point posicion) {
		this(energia, posicion, null);
	}
	
	public ObjetoCombate(int energia, Point posicion, Sprite sprite) {
		super(posicion, sprite);
		energiaRestante = energia;
	}
	
	public boolean recibirDanio(int danio) {
		if (vulnerable && isVivo()) {
			energiaRestante -= danio;
			contDurImpacto = DURACION_IMPACTO;
			
			if (!isVivo()) {
				energiaRestante = 0;
				notificacionMuerte();
			}
			
			return true;
		}
		else
			return false;
	}
	
	public void recibirCura(int energia) {
		energiaRestante += energia;
	} 
	
	protected void setVulnerable(boolean vulnerable) {
		this.vulnerable = vulnerable;
	}
		
	public int getEnergia() {
		return energiaRestante;
	} 
	
	@Override
	public void desactivar() {
		super.desactivar();
		energiaRestante = 0;
	}
	
	public boolean isVivo() {
		return energiaRestante > 0;
	}
		
	protected abstract void notificacionMuerte();
	
	@Override
	public String toString() {
		return super.toString() + ". Vuln: " + vulnerable + ". Ener: " + energiaRestante;
	}
	
	@Override
	public void pintar(Graphics2D g) {
		if (mundo != null && contDurImpacto != 0) {
			contDurImpacto--;
			
			Rectangle camara = mundo.getCamara();
			BufferedImage buffer = new BufferedImage(camara.width, camara.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D gBuffer = buffer.createGraphics();
			
			gBuffer.translate(-camara.x, -camara.y);
			super.pintar(gBuffer);
			
			gBuffer.setComposite(AlphaComposite.SrcAtop.derive(OPACIDAD_IMPACTO));
			gBuffer.setColor(Color.RED);
			gBuffer.fillRect(camara.x, camara.y, camara.width, camara.height);
			
			g.drawImage(buffer, camara.x, camara.y, null);
		}
		else
			super.pintar(g);
	}
}

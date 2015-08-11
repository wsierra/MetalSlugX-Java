import java.awt.event.KeyEvent;

public class ConfiguracionMST extends Configuracion {		

	public ConfiguracionMST() {
		setPantallaCompleta(false);
		setMantenerRelacionAspecto(true);
		setEstadoSonido(false);
		setPista("Tema original");
		
		setControl(0, "arriba", KeyEvent.VK_UP);
		setControl(0, "izquierda", KeyEvent.VK_LEFT);
		setControl(0, "derecha", KeyEvent.VK_RIGHT);
		setControl(0, "saltar", KeyEvent.VK_SPACE);
		setControl(0, "disparar", KeyEvent.VK_X);
		setControl(0, "granada", KeyEvent.VK_Z);
		
		setControl(1, "arriba", KeyEvent.VK_NUMPAD5);
		setControl(1, "izquierda", KeyEvent.VK_NUMPAD1);
		setControl(1, "derecha", KeyEvent.VK_NUMPAD3);
		setControl(1, "saltar", KeyEvent.VK_NUMPAD4);
		setControl(1, "disparar", KeyEvent.VK_NUMPAD6);
		setControl(1, "granada", KeyEvent.VK_NUMPAD2);
		
		setControl(2, "arriba", KeyEvent.VK_I);
		setControl(2, "izquierda", KeyEvent.VK_J);
		setControl(2, "derecha", KeyEvent.VK_L);
		setControl(2, "saltar", KeyEvent.VK_U);
		setControl(2, "disparar", KeyEvent.VK_O);
		setControl(2, "granada", KeyEvent.VK_K);
		
		setControl(3, "arriba", KeyEvent.VK_W);
		setControl(3, "izquierda", KeyEvent.VK_A);
		setControl(3, "derecha", KeyEvent.VK_D);
		setControl(3, "saltar", KeyEvent.VK_Q);
		setControl(3, "disparar", KeyEvent.VK_E);
		setControl(3, "granada", KeyEvent.VK_S);
	}
}

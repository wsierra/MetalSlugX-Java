import javax.sound.sampled.*;
import java.io.IOException;

public class PruebaJavaSound {


	public static void main(String[] args) {
		
		try {
			//~Thread.sleep(10000);
			//~playSound();
			//~playSound();
			//~Thread.sleep(1000000);
			AudioInputStream ais = AudioSystem.getAudioInputStream(MetalSlugT.DIR_PISTAS.resolve("mslug-243.wav").toFile());
			Mixer mix = AudioSystem.getMixer(null);
			System.out.println(AudioSystem.getMixer(null));
			System.out.println(AudioSystem.getMixer(null));
			System.out.println(AudioSystem.getMixer(null));
			System.out.println(AudioSystem.getMixer(null));
			System.out.println(AudioSystem.getMixer(null));
			System.out.println(AudioSystem.getMixer(null));
			System.out.println(AudioSystem.getMixer(null));
			System.out.println(AudioSystem.getMixer(null));
			
			
			for (int i = 0 ; i < 30; i++) {
				//Thread.sleep(1000);
				Clip clip2 = AudioSystem.getClip(mix.getMixerInfo());
				System.out.println(clip2);
				clip2.open(ais);
				((BooleanControl)clip2.getControl(BooleanControl.Type.MUTE)).setValue(false);
				
	
				for (Control c : clip2.getControls()) 
					System.out.println("      " + c);
				clip2.start();
			}
			
			for (Control c : mix.getControls()) 
					System.out.println("      " + c);
			
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();	
			Thread.sleep(1000000);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	
	static long tiempo;
	public static  void playSound() {
    try {
		//Thread.sleep(1000);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
			new java.io.File("MetalSlugT/archivos/sonidos/pistas/prueba2.wav"));
        Clip clip = AudioSystem.getClip();
        clip.addLineListener(new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.START){}
					//~System.out.println((System.nanoTime()- tiempo)/1000000);
				if (event.getType() == LineEvent.Type.STOP){
					event.getLine().close();System.out.println("closed");
				}
				if (event.getType() == LineEvent.Type.CLOSE) {
				}
			}
			
			});
			
        tiempo = System.nanoTime();
        clip.open(audioInputStream);
		//~System.out.println((System.nanoTime()- tiempo)/1000000);
		//clip.setLoopPoints((int)(6.420 * audioInputStream.getFormat().getFrameRate()), -1);
		clip.setFramePosition((int)(1020.5 * audioInputStream.getFormat().getFrameRate()));
        clip.start();
       // playSound();
       
       //~new Thread(new Runnable(){public void run(){playSound();}}).start();
    } catch(Exception ex) {
        System.out.println("Error with playing sound.");
        ex.printStackTrace();
    }
}




}

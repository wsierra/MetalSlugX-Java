import javax.sound.sampled.*;
import java.io.*;
import javax.swing.*;

public class PruebaLoop {

	public static void main(String[] a)throws Exception {
		AudioInputStream as1 = AudioSystem.getAudioInputStream(new java.io.FileInputStream("MetalSlugT/archivos/sonidos/pistas/Tema original.loop.wav"));
		AudioFormat af = as1.getFormat();
		Clip clip1 = AudioSystem.getClip();
		DataLine.Info info = new DataLine.Info(Clip.class, af);

		Line line1 = AudioSystem.getLine(info);

		if ( ! line1.isOpen() )
		{
		clip1.open(as1);
		clip1.loop(Clip.LOOP_CONTINUOUSLY);
		clip1.start();
		}
	}
}

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.nio.file.Path;

public class Configuracion implements Serializable {

	private static final long serialVersionUID = 1234L;
	
	private boolean pantallaCompleta;
	private boolean relacionAspecto;
	private boolean estadoSonido;
	private String pista;
	private HashMap<Par, Integer> controles;
	
	public Configuracion() {
		controles = new HashMap<Par, Integer>();
	}
	
	public Configuracion(Configuracion conf) {
		setConfiguracion(conf);
	}

	public Configuracion(Path rutaArchivo) throws IOException, ClassNotFoundException {
		cargar(rutaArchivo);
	}

	public void cargar(Path rutaArchivo) throws IOException, ClassNotFoundException {
		
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream( new FileInputStream(rutaArchivo.toFile()) );
			Configuracion conf = (Configuracion)ois.readObject();
			pantallaCompleta = conf.pantallaCompleta;
			relacionAspecto = conf.relacionAspecto;
			estadoSonido = conf.estadoSonido;
			pista = conf.pista;
			controles = conf.controles;
		}
		finally {
			if (ois != null)
				ois.close();
		}
	}

	public void guardar(Path rutaArchivo) throws IOException {
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream( new FileOutputStream(rutaArchivo.toFile()) );
			oos.writeObject(this);
		}
		finally {
			if (oos != null)
				oos.close();
		}
	}

	public void setConfiguracion(Configuracion conf) {
		pantallaCompleta = conf.pantallaCompleta;
		relacionAspecto = conf.relacionAspecto;
		estadoSonido = conf.estadoSonido;
		pista = conf.pista;
		controles = new HashMap<Par, Integer>(conf.controles);
	}
	
	public void setPantallaCompleta(boolean pantallaCompleta) {
		this.pantallaCompleta = pantallaCompleta;
	}
	
	public void setMantenerRelacionAspecto(boolean mantener) {
		relacionAspecto = mantener;
	}
	
	public void setEstadoSonido(boolean estadoSonido) {
		this.estadoSonido = estadoSonido;
	}

	public void setPista(String pista) {
		this.pista = pista;
	}
	
	public Par setControl(int jugador, String comando, int tecla) {
		
		Iterator<Par> it = controles.keySet().iterator();
		Par parAnterior = null;
		while (it.hasNext() && parAnterior == null) {
			Par par = it.next();
			if ( controles.get(par).equals(tecla) ) {
				it.remove();
				parAnterior = par;
			}
		}
		
		controles.put(new Par(jugador, comando), tecla);
				
		return parAnterior;
	}

	public boolean getPantallaCompleta() {
		return pantallaCompleta;
	}
	
	public boolean getMantenerRelacionAspecto() {
		return relacionAspecto;
	}
	
	public boolean getEstadoSonido() {
		return estadoSonido;
	}
	
	public Integer getControl(int jugador, String comando) {
		return controles.get(new Par(jugador, comando));
	}
	
	public String getPista() {
		return pista != null ? pista : "";	
	}
	
	public int getLoopPista() {
		
	   File archivo = null;
	   FileReader fr = null;
	   BufferedReader br = null;
	   int loop = 0;
	   try {
		   archivo = MetalSlugT.DIR_PISTAS.resolve("loopinfo.txt").toFile();
		   fr = new FileReader (archivo);
		   br = new BufferedReader(fr);
	 
		   String nuevaLinea;
		   while ((nuevaLinea=br.readLine())!=null) {
			   int indiceCorte = nuevaLinea.indexOf(':');
			   if (getPista().equals(nuevaLinea.substring(0, indiceCorte)))
				   loop = Integer.parseInt(nuevaLinea.substring(indiceCorte + 1, nuevaLinea.length()));
		   }
		} 
		catch(NumberFormatException | IOException e ) {
			e.printStackTrace();
		}
		finally {
			try {                    
				if( null != fr )  
					fr.close(); 
			} 
			catch (IOException e) { 
				e.printStackTrace();
			}
		}
		
		return loop > 0 ? loop : 0;
	}
	
	class Par implements Serializable {
		
		private static final long serialVersionUID = 1234L;
		
		public final int jugador;
		public final String comando;
		public final int hashcode;
		
		public Par(int jugador, String comando) {
			this.jugador = jugador;
			this.comando = comando != null ? comando : "";
			
			int hash = 1;
			hash = hash * 17 + jugador;
			hash = hash * 31 + comando.hashCode();
			hashcode = hash;
		}
		
		@Override
		public int hashCode() {
			return hashcode;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || getClass() != obj.getClass())
				return false;
			if (obj == this)
				return true;

			Par otro = (Par)obj;
			return comando.equals(otro.comando) && jugador == otro.jugador; 
		}
	}
}


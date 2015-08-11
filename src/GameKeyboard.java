import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.ArrayList;

public class GameKeyboard extends EventQueue<KeyEvent> implements KeyListener {
	
    private TreeMap<Integer, Boolean> keys = new TreeMap<Integer, Boolean>();
    private LinkedList<KeyEvent> filteredEvents = new LinkedList<KeyEvent>();
    private ArrayList<KeyListener> listeners = new ArrayList<KeyListener>();

    public boolean isKeyPressed(int keyCode) {
		Boolean pressed = keys.get(keyCode);
		if (pressed != null)
			return pressed;
		return false;
    }
    
    public void addKeyListener(KeyListener listener) {
		if (listener != null)
			listeners.add(listener);
	}
    
    public void removeKeyListener(KeyListener listener) {
		if (listener != null)
			listeners.remove(listener);
	}
    
    public void clear() {
		keys = new TreeMap<Integer, Boolean>();
		filteredEvents = new LinkedList<KeyEvent>();	
	}
    
    @Override
    public synchronized void update() {
		super.update();
		
		ArrayList<KeyListener> clonL = (ArrayList<KeyListener>)listeners.clone();
		
		for (KeyEvent event : filteredEvents)
			for (KeyListener l : clonL)
				if (event.getID() == KeyEvent.KEY_PRESSED)
					l.keyPressed(event);
				else
					l.keyReleased(event);
		
		filteredEvents = new LinkedList<KeyEvent>();		
	}

    @Override
	protected void processEvent(KeyEvent event) {
		boolean isKPEvent = event.getID() == KeyEvent.KEY_PRESSED;
		if ( isKPEvent != isKeyPressed(event.getKeyCode()) ) {
			filteredEvents.add(event);
			keys.put(event.getKeyCode(), isKPEvent);
		}
	} 
	
	@Override
    public void keyPressed(KeyEvent event) {
        addEvent(event);
    }
    	
	@Override
    public void keyReleased(KeyEvent event) {
        addEvent(event);
    }
    
    @Override
    public void keyTyped(KeyEvent event) {}
    
    
   
    
    
    //~
    //~static boolean fin = false;
    //~static TreeMap<Integer, javax.swing.JToggleButton> teclillas = new TreeMap<Integer, javax.swing.JToggleButton>();
    //~
	//~public static void main(String[] args) {
		//~
		//~java.awt.Frame f = new java.awt.Frame();
		//~java.awt.Panel p = new java.awt.Panel(new java.awt.GridLayout(0,13));
		//~GameKeyboard  gkb = new GameKeyboard();
		//~p.setFocusTraversalKeysEnabled(false);
		//~f.add(p);
		//~f.setVisible(true);
//~
		//~f.setSize(200,200);
		//~
		//~KeyListener l = new java.awt.event.KeyAdapter() {
			//~public void keyPressed(KeyEvent e) {
				//~if (!teclillas.containsKey(e.getKeyCode())) {
					//~javax.swing.JToggleButton b = new javax.swing.JToggleButton(KeyEvent.getKeyText(e.getKeyCode()));
					//~b.setFocusable(false);
					//~b.setPreferredSize(new java.awt.Dimension(90,20));
					//~teclillas.put(e.getKeyCode(), b);
					//~p.add(b);
					//~f.pack();
				//~}
				//~if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					//~fin = true;
			//~}
			//~
		//~};
		//~
		//~p.addKeyListener(l);
		//~
		//~while (!fin) {
			//~try {Thread.sleep(20);} catch(InterruptedException e){System.out.println(e);}
		//~}
		//~
		//~p.removeKeyListener(l);
		//~p.addKeyListener(gkb);
		//~gkb.addKeyListener(new java.awt.event.KeyAdapter() {
			//~@Override
			//~public void keyPressed(KeyEvent e) {
				//~System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
			//~}
			//~
			//~@Override
			//~public void keyReleased(KeyEvent e) {
				//~System.out.println("\t\t" + KeyEvent.getKeyText(e.getKeyCode()));
			//~}
		//~});
		//~
		//~System.out.println("Pressed\t\tReleased");
		//~
		//~while (true) {
			//~gkb.update();
			//~for (java.util.Map.Entry<Integer,javax.swing.JToggleButton> par : teclillas.entrySet()) {
				//~par.getValue().setSelected(gkb.isKeyPressed(par.getKey()));
			//~}
				//~
			//~try {Thread.sleep(20);} catch(InterruptedException e){System.out.println(e);}
		//~}
	//~}
    
}

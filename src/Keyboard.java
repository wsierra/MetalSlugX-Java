import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles key input events.
 *
 * The only commonly needed public method is {@link #isKeyPressed(int)}.
 * This simply returns if a key is currently pressed (i.e. the key has been
 * pressed, but not yet released).
 *
 * @author Eric
 */
public class Keyboard extends EventQueue<KeyEvent> implements KeyListener
{
    private final int KEY_CODE_MAX = 256;
    private boolean[] keys = new boolean[KEY_CODE_MAX];

    /**
     * Returns whether a given key is currently pressed or not.
     *
     * @param keyCode the code for the given key
     * @return true if the key is pressed, false otherwise
     */
    public boolean isKeyPressed(int keyCode)
    {
        // check if the key a fast key
        if((keyCode >= 0) && (keyCode < KEY_CODE_MAX))
        {
            return keys[keyCode];
        }
        return false;
    }

    /**
     * Process a key event.
     *
     * @param event
     */
    @Override
    protected void processEvent(KeyEvent event)
    {
        if(event.getID() == KeyEvent.KEY_PRESSED)
        {
            if((event.getKeyCode() >= 0) &&
                    (event.getKeyCode() < KEY_CODE_MAX))
            {
                keys[event.getKeyCode()] = true;
            }
        }
        else if(event.getID() == KeyEvent.KEY_RELEASED)
        {
            if((event.getKeyCode() >= 0) &&
                    (event.getKeyCode() < KEY_CODE_MAX))
            {
                keys[event.getKeyCode()] = false;
            }
        }
    }

    /**
     * Adds a key pressed event to the queue.
     *
     * @param event
     */
    public void keyPressed(KeyEvent event)
    {
        addEvent(event);
    }

    /**
     * Adds a key released event to the queue.
     *
     * @param event
     */
    public void keyReleased(KeyEvent event)
    {
        addEvent(event);
    }

    /**
     * Adds a key typed event to the queue.
     *
     * @param event
     */
    public void keyTyped(KeyEvent event)
    {
        addEvent(event);
    }
}
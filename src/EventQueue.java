/**
 * @(#)Text4.java
 *
 *
 * @author
 * @version 1.00 2013/4/8
 */


import java.util.Collection;
import java.util.LinkedList;

/**
 * Provides basic thread-safe and deterministic event handling for input.
 *
 * Events that occur between update calls are placed into the event queue.
 * When update is called, each event is processed, then the event queue is
 * moved to the publicly accessible events.
 * <p>
 * The motivation for this design is three fold.
 * First, events are handled in a thread-safe way (i.e. the list of events wont
 * be modified while the game thread is using it.
 * Second, the program will act more deterministically.
 * While an event may happen at any time, changes to the queue will only occur
 * during the call to {@link #update()}.
 * Finally, through use of the {@link #addEvents(java.util.Collection)} method,
 * user input can be exactly reproduced.
 * Simply add new events in the frame before they are needed, and
 * {@link #update()} will process them as if they had been the result of a user
 * action.
 *
 * @author Eric
 */
public abstract class EventQueue<T>{
    private LinkedList<T> events = new LinkedList<T>();
    private LinkedList<T> eventQueue = new LinkedList<T>();

    /**
     * Returns a list of events that occurred before the last update.
     *
     * @return list of events
     */
    public LinkedList<T> getEvents()
    {
        return events;
    }

    /**
     * Processes the event queue.
     *
     * {@link #processEvent(java.lang.Object)} is called for each event in the
     * event queue.
     * Then events is set to the event queue and a new event queue is created.
     * As events is no longer needed, the user is free to modify it however
     * they chose.
     * If a reference is to events is not saved before the next update, its
     * contents will be lost.
     */
    public synchronized void update()
    {
        for(T event : eventQueue)
        {
            processEvent(event);
        }
        events = eventQueue;
        eventQueue = new LinkedList<T>();
    }

    /**
     * Override to perform any custom processing for events.
     *
     * When {@link #update()} this will be called for each event in the queue.
     *
     * @param event
     */
    protected void processEvent(T event)
    {
        return;
    }

    /**
     * Adds an event to the queue
     *
     * @param newEvent the event to be added
     */
    public synchronized void addEvent(T newEvent)
    {
            eventQueue.add(newEvent);
    }

    /**
     * Adds events to the event queue.
     *
     * The events will be processed at the next update.
     *
     * @param newEvents
     */
    public synchronized void addEvents(Collection<T> newEvents)
    {
        eventQueue.addAll(newEvents);
    }
}
/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * This file is part of MouseFeed.
 *
 * MouseFeed is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MouseFeed is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MouseFeed.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mousefeed.client.collector;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collects user activity data.
 * @author Andriy Palamarchuk
 */
public class Collector {
    /**
     * @see #getLastAction()
     */
    private ActionEvent lastAction;
    
    /**
     * The event-type specific listeners.
     * The array is indexed by the corresponding enumeration values indexes.
     */
    private List<?>[] typedEventListeners = new List[EventType.values().length];

    /**
     * The event listeners listening to all the types of events.
     * The array is indexed by the corresponding enumeration values indexes.
     */
    private List<IEventListener> nontypedEventListeners =
            new ArrayList<IEventListener>();

    /**
     * Creates a new collector.
     */
    public Collector() {
        initTypedListeners();
    }

    /**
     * Initializes {@link #typedEventListeners} field.
     */
    private void initTypedListeners() {
        for (int i = 0; i < typedEventListeners.length; i++) {
            typedEventListeners[i] = new ArrayList<IEventListener>();
        }
    }

    /**
     * Must be called on user activity event. 
     * @param event the event. Not be <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public void onEvent(Event event) {
        notNull(event);
        if (event.getType().equals(EventType.ACTION)) {
            lastAction = (ActionEvent) event;
        }
        final List eventTypeListeners =
            typedEventListeners[event.getType().ordinal()]; 
        notifyListeners(eventTypeListeners, event);
        notifyListeners(nontypedEventListeners, event);
    }

    /**
     * Notifies the provided collection of listeners with the provided event.
     * @param listeners the listeners to notify. Assumed not <code>null</code>.
     * Can be empty.
     * @param event the event to notify the listeners with.
     * Assumed not <code>null</code>.
     */
    private void notifyListeners(Collection<IEventListener> listeners,
            Event event) {
        for (IEventListener listener : listeners) {
            listener.event(event);
        }
    }

    /**
     * Adds an event type listener.
     * @param listener the listener to add. Not <code>null</code>.
     * @param eventType the event type to listen for. If <code>null</code>,
     * listen to all the events.
     */
    @SuppressWarnings("unchecked")
    public void addEventListener(IEventListener listener, EventType eventType) {
        notNull(listener);
        if (eventType == null) {
            nontypedEventListeners.add(listener);
        } else {
            final List listeners = typedEventListeners[eventType.ordinal()];
            listeners.add(listener);
        }
    }

    /**
     * The listener to remove.
     * @param listener the listener to remove. Not <code>null</code>.
     * Throws <code>IllegalArgumentException</code>
     * if such listener can't be found.
     */
    public void removeEventListener(IEventListener listener) {
        notNull(listener);
        boolean removed = false;
        for (List<?> listeners : typedEventListeners) {
            removed |= listeners.remove(listener);
        }
        removed |= nontypedEventListeners.remove(listener);
        if (!removed) {
            throw new IllegalArgumentException(
                    "Listener " + listener + " is not registered");
        }
    }

    /**
     * The last non-internal action provided to the collector.
     * @return the last action provided to the method
     * {@link #onEvent(ActionEvent)},
     * <code>null</code> if there were no calls to that method yet.
     * @see #onEvent(ActionEvent)
     */
    public ActionEvent getLastAction() {
        return lastAction;
    }
}

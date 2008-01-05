/*
 * Copyright (C) Heavy Lifting Software 2007-2008.
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

/**
 * The user activity event.
 *
 * @author Andriy Palamarchuk
 */
public abstract class Event {

    /**
     * @see #getClientEvent()
     */
    private Object clientEvent;

    /**
     * The event type.
     * @return the event type. Never <code>null</code>.
     */
    public abstract EventType getType();

    /**
     * The underlying MouseFeed client event the user activity event
     * was generated from.
     * For example, in Eclipse MouseFeed client this will be the Eclipse event.
     * @return the MouseFeed client event.
     * Must be initialized before the event is sent to data collector.
     * Never <code>null</code> after initialization.   
     */
    public Object getClientEvent() {
        return clientEvent;
    }

    /**
     * @param clientEvent the new client event value. Not <code>null</code>
     */
    public void setClientEvent(Object clientEvent) {
        notNull(clientEvent);
        this.clientEvent = clientEvent;
    }
}

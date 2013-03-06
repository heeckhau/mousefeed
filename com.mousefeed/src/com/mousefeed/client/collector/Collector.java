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

/**
 * Collects user activity data.
 * @author Andriy Palamarchuk
 */
public class Collector {

    /**
     * @see #getLastAction()
     */
    private AbstractActionDesc lastAction;

    /**
     * Default constructor. Does nothing.
     */
    public Collector() {
    }
    
    /**
     * The last non-internal action provided to the collector.
     * @return the last action provided to the method
     * {@link #onAction(AbstractActionDesc)},
     * <code>null</code> if there were no calls to that method yet.
     * @see #onAction(AbstractActionDesc)
     */
    public AbstractActionDesc getLastAction() {
        return lastAction;
    }

    /**
     * Must be called on user action. 
     * @param action the action. Not be <code>null</code>.
     */
    public void onAction(final AbstractActionDesc action) {
        notNull(action);
        lastAction = action;
    }
}

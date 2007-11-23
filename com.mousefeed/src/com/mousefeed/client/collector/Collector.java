/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
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
    private ActionDesc lastAction;

    /**
     * The last non-internal action provided to the collector.
     * @return the last action provided to the method
     * {@link #onAction(ActionDesc)},
     * <code>null</code> if there were no calls to that method yet.
     * @see #onAction(ActionDesc)
     */
    public ActionDesc getLastAction() {
        return lastAction;
    }

    /**
     * Must be called on user action. 
     * @param action the action. Not be <code>null</code>.
     */
    public void onAction(ActionDesc action) {
        notNull(action);
        lastAction = action;
    }
}

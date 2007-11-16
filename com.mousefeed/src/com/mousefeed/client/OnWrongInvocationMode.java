/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.client;

/**
 * What to do if an action is called with a wrong mode.
 * E.g. in a situation when an action must be called with a keyboard,
 * but is called with a mouse.
 * 
 * @author Andriy Palamarchuk
 */
public enum OnWrongInvocationMode {
    /**
     * Allow user to proceed.
     */
    DO_NOTHING,

    /**
     * Remind the user that he uses incorrect access mode. 
     */
    REMIND,
    
    /**
     * Prevent the action from running, remind the user.
     */
    ENFORCE;
    
    /**
     * The default access mode.
     */
    public static final OnWrongInvocationMode DEFAULT = REMIND;

    /**
     * Provides label text. 
     */
    private static final Messages MESSAGES =
            new Messages(OnWrongInvocationMode.class);

    /**
     * Human-readable label text.
     * @return the label text. Never <code>null</code>.
     */
    public String getLabel() {
        return MESSAGES.get(name());
    }
}

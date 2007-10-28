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
public enum OnWrongAccessMode {
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
     * Provides label text. 
     */
    private static final Messages MESSAGES =
            new Messages(OnWrongAccessMode.class);

    /**
     * Human-readable label text.
     * @return the label text. Never <code>null</code>.
     */
    public String getLabel() {
        return MESSAGES.get(name());
    }

    /**
     * The modes labels and names.
     * @return the enumeration labels and names as a 2-dimensional array
     * arranged as: { {label1, name1}, {label2, name2}, ...}.
     * The first dimension of the array has the same length as number of the
     * element enumerations.
     */
    public static String[][] getLabelsAndNames() {
        final String[][] result = new String[values().length][2];
        final OnWrongAccessMode[] values = OnWrongAccessMode.values();
        for (int i = 0; i < values.length; i++) {
            result[i][0] = values[i].getLabel(); 
            result[i][1] = values[i].name(); 
        }
        return result;
    }
}

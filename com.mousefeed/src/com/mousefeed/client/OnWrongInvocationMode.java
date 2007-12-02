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

    /**
     * Labels of the enumeration values.
     * @return the labels of the enumeration values.
     * Array of the same size as {@link #values()}. 
     */
    public static String[] getLabels() {
        final String[] labels = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            labels[i] = values()[i].getLabel();
        }
        return labels;
    }
}

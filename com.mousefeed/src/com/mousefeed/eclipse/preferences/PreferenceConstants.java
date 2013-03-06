/*
 * Copyright (C) Heavy Lifting Software 2007, Robert Wloch 2012.
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
package com.mousefeed.eclipse.preferences;

/**
 * Constant definitions for plug-in preferences.
 *
 * @author Andriy Palamarchuk
 * @author Robert Wloch
 */
public final class PreferenceConstants {

    /**
     * Whether invocation control is enabled preference.
     * The preference indicates whether to help to learn the desired way to
     * invoke actions.
     */
    public static final String P_INVOCATION_CONTROL_ENABLED =
            "InvocationControlEnabled";
    
    /**
     * The default value for the setting {@link #P_INVOCATION_CONTROL_ENABLED}
     * setting.
     */
    public static final boolean INVOCATION_CONTROL_ENABLED_DEFAULT = true;
    
    /**
     * Whether keyboard shortcut configuration is enabled preference.
     * The preference indicates whether to show the Keys preference page
     * for often used actions without a shortcut.
     */
    public static final String P_CONFIGURE_KEYBOARD_SHORTCUT_ENABLED =
            "ConfigureKeyboardShortcutEnabled";

    /**
     * The default value for the setting {@link #P_CONFIGURE_KEYBOARD_SHORTCUT_ENABLED}
     * setting.
     */
    public static final boolean CONFIGURE_KEYBOARD_SHORTCUT_ENABLED_DEFAULT = true;

    /**
     * Threshold for action invocation counter above which keyboard shortcut
     * configuration is enabled preference.
     * The preference indicates whether to show the Keys preference page
     * for often used actions without a shortcut.
     */
    public static final String P_CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD =
            "ConfigureKeyboardShortcutThreshold";
    
    /**
     * The default value for the setting {@link #P_CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD}
     * setting.
     */
    public static final int CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD_DEFAULT = 2;
    
    /**
     * Indicates how to deal with actions called with wrong action invocation
     * mode if there is no specific handling defined.
     */
    public static final String P_DEFAULT_ON_WRONG_INVOCATION_MODE =
            "DefaultOnWrongInvocationMode";
    
    private PreferenceConstants() {
    }
}

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
package com.mousefeed.eclipse.preferences;

/**
 * Constant definitions for plug-in preferences.
 *
 * @author Andriy Palamarchuk
 */
public class PreferenceConstants {

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
     * Indicates how to deal with actions called with wrong action invocation
     * mode if there is no specific handling defined.
     */
    public static final String P_DEFAULT_ON_WRONG_INVOCATION_MODE =
            "DefaultOnWrongInvocationMode";
}

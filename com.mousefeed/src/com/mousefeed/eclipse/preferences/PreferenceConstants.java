/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
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

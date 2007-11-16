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

import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_DEFAULT_ON_WRONG_INVOCATION_MODE;
import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_INVOCATION_CONTROL_ENABLED;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.Activator;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provides access to the plugin preferences.
 * 
 * @author Andriy Palamarchuk
 */
public class PreferenceAccessor {
    /**
     * Whether invocation control is enabled preference.
     * The preference indicates whether to help to learn the desired way to
     * invoke actions
     * @return current preference value whether invocation control is enabled.
     */
    public boolean isInvocationControlEnabled() {
        return getPreferenceStore().getBoolean(
                P_INVOCATION_CONTROL_ENABLED);
    }

    /**
     * @param invocationControlEnabled the new value for the setting returned by
     * {@link #isInvocationControlEnabled()}.
     * @see #isInvocationControlEnabled()
     */
    public void storeInvocationControlEnabled(
            boolean invocationControlEnabled) {
        getPreferenceStore().setValue(
                P_INVOCATION_CONTROL_ENABLED, invocationControlEnabled);
    }

    /**
     * The default preference what to do by default on wrong invocation mode.
     * @return the global invocation mode preference.
     * @see PreferenceConstants#P_DEFAULT_ON_WRONG_INVOCATION_MODE
     */
    public OnWrongInvocationMode getOnWrongInvocationMode() {
        final String stored = getPreferenceStore().getString(
                P_DEFAULT_ON_WRONG_INVOCATION_MODE);
        return stored == null
                ? OnWrongInvocationMode.DEFAULT
                : OnWrongInvocationMode.valueOf(stored);
    }
    
    /**
     * Stores the provided preference.
     * @param value the new value. Not <code>null</code>.
     */
    public void storeOnWrongInvocationMode(OnWrongInvocationMode value) {
        notNull(value);
        getPreferenceStore().setValue(
                P_DEFAULT_ON_WRONG_INVOCATION_MODE, value.name());
    }

    /**
     * The plugin preference store.
     * @return never <code>null</code>.
     */
    private IPreferenceStore getPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }
}

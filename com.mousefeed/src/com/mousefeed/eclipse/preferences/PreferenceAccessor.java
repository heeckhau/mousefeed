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

import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_PROMOTE_KEYS;

import com.mousefeed.eclipse.Activator;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provides access to the plugin preferences.
 * 
 * @author Andriy Palamarchuk
 */
public class PreferenceAccessor {
    /**
     * @return <code>true</code> if the preference is on
     * to show reminders when an action having a keyboard shortcut associated
     * with is called by mouse click.
     */
    public boolean getPromoteKeys() {
        return getPreferenceStore().getBoolean(P_PROMOTE_KEYS);
    }

    /**
     * The plugin preference store.
     * @return never <code>null</code>.
     */
    private IPreferenceStore getPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }
}

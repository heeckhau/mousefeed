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

import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_DEFAULT_ON_WRONG_ACCESS_MODE;

import com.mousefeed.client.OnWrongAccessMode;
import com.mousefeed.eclipse.Activator;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provides access to the plugin preferences.
 * 
 * @author Andriy Palamarchuk
 */
public class PreferenceAccessor {
    /**
     * The default preference what to do by default on wrong access mode.
     * @return the global access mode preference.
     * @see PreferenceConstants#P_DEFAULT_ON_WRONG_ACCESS_MODE
     */
    public OnWrongAccessMode getPromoteKeys() {
        final String stored =
                getPreferenceStore().getString(P_DEFAULT_ON_WRONG_ACCESS_MODE);
        // TODO if null?
        return OnWrongAccessMode.valueOf(stored);
    }

    /**
     * The plugin preference store.
     * @return never <code>null</code>.
     */
    private IPreferenceStore getPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }
}

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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.Activator;

/**
 * Initializes default preference values.
 *
 * @author Andriy Palamarchuk
 * @author Robert Wloch
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /**
     * Default constructor does nothing.
     */
    public PreferenceInitializer() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore store =
               Activator.getDefault().getPreferenceStore();
        store.setDefault(
                PreferenceConstants.P_DEFAULT_ON_WRONG_INVOCATION_MODE,
                OnWrongInvocationMode.DEFAULT.name());
        store.setDefault(
                PreferenceConstants.P_INVOCATION_CONTROL_ENABLED,
                PreferenceConstants.INVOCATION_CONTROL_ENABLED_DEFAULT);
        store.setDefault(
                PreferenceConstants.P_CONFIGURE_KEYBOARD_SHORTCUT_ENABLED,
                PreferenceConstants.CONFIGURE_KEYBOARD_SHORTCUT_ENABLED_DEFAULT);
        store.setDefault(
                PreferenceConstants.P_CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD,
                PreferenceConstants.CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD_DEFAULT);
    }
}

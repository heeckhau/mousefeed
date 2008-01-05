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
package com.mousefeed.eclipse;

import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.eclipse.preferences.PreferenceAccessor;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provides {@link IPreferenceStore} of the MouseFeed plugin.
 * Is used to break dependency of {@link PreferenceAccessor} on
 * the plugin class.
 * Singleton.
 * 
 * @author Andriy Palamarchuk
 */
public class PreferenceStoreProvider {
    /**
     * @see #getInstance()
     */
    private static final PreferenceStoreProvider INSTANCE =
            new PreferenceStoreProvider();
    
    /**
     * @see #getPreferenceStore()
     */
    private IPreferenceStore preferenceStore;

    /**
     * Constructor. Should not be used from outside of the class except
     * for unit testing.
     */
    PreferenceStoreProvider() {
    }

    /**
     * The singleton instance.
     * @return the singleton instance. Never <code>null</code>.
     */
    public static PreferenceStoreProvider getInstance() {
        return INSTANCE;
    }

    /**
     * The preference store of the MouseFeed plugin.
     * Throws <code>IllegalStateException</code> if not initialized with
     * {@link #setPreferenceStore(IPreferenceStore)}.
     * @return the preference store. Never <code>null</code>
     */
    public IPreferenceStore getPreferenceStore() {
        if (preferenceStore == null) {
            throw new IllegalStateException(
                    "The preference store has not been initialized yet.");
        }
        return preferenceStore;
    }

    /**
     * The method should be called only once.
     * @param preferenceStore the preference store to set.
     * Not <code>null</code>.
     * @see #getPreferenceStore()
     */
    public void setPreferenceStore(IPreferenceStore preferenceStore) {
        notNull(preferenceStore);
        if (this.preferenceStore != null) {
            throw new IllegalStateException(
                    "Preference store is already initialized");
        }
        this.preferenceStore = preferenceStore;
    }
}

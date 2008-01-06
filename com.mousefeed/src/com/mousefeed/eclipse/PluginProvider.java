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
package com.mousefeed.eclipse;

import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.eclipse.preferences.PreferenceAccessor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Provides access to the MouseFeed plugin.
 * Is used to break dependency of {@link PreferenceAccessor} on
 * the plugin class.
 * Singleton.
 * 
 * @author Andriy Palamarchuk
 */
public class PluginProvider {
    /**
     * @see #getInstance()
     */
    private static final PluginProvider INSTANCE =
            new PluginProvider();
    
    /**
     * @see #getPlugin()
     */
    private AbstractUIPlugin plugin;

    /**
     * Constructor. Should not be used from outside of the class except
     * for unit testing.
     */
    PluginProvider() {
    }

    /**
     * The singleton instance.
     * @return the singleton instance. Never <code>null</code>.
     */
    public static PluginProvider getInstance() {
        return INSTANCE;
    }

    /**
     * The MouseFeed plugin.
     * Throws <code>IllegalStateException</code> if not initialized with
     * {@link #setPlugin(AbstractUIPlugin)}.
     * @return the plugin. Never <code>null</code>
     */
    public AbstractUIPlugin getPlugin() {
        if (plugin == null) {
            throw new IllegalStateException(
                    "The plugin has not been initialized yet.");
        }
        return plugin;
    }

    /**
     * The method should be called only once.
     * @param plugin the plugin to set. Not <code>null</code>.
     * @see #getPlugin()
     */
    public void setPlugin(AbstractUIPlugin plugin) {
        notNull(plugin);
        if (this.plugin != null) {
            throw new IllegalStateException(
                    "The plugin is already initialized");
        }
        this.plugin = plugin;
    }
}
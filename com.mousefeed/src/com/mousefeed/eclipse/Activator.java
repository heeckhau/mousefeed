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

import static org.apache.commons.lang.Validate.isTrue;

import com.mousefeed.client.collector.Collector;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle.
 * @author Andriy Palamarchuk
 */
public class Activator extends AbstractUIPlugin {

    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "com.mousefeed";

    /**
     * The shared instance.
     */
    private static Activator plugin;
    
    /**
     * @see #getCollector()
     */
    private final Collector collector = new Collector();
    
    /**
     * The constructor.
     */
    public Activator() {
        isTrue(plugin == null);
        plugin = this;
    }

    /**
     * Returns the shared instance.
     *
     * @return the shared instance.
     * <code>null</code> before an activator is created or after it is stopped.
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
     * Gathers user activity data.
     * @return the data collector. Not <code>null</code>.
     */
    public Collector getCollector() {
        return collector;
    }
}

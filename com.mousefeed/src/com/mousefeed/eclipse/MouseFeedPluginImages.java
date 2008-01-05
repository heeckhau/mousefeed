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

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;

/**
 * The images provided by the MouseFeed plugin.
 * Singleton.
 *
 * @author Andriy Palamarchuk
 */
public final class MouseFeedPluginImages {
    /**
     * Image for the user activity view action to toggle user activity logging.
     */
    public static final String IMG_TOGGLE_LOGGING = "IMG_TOGGLE_LOGGING"; 
    
    /**
     * @see #getInstance() 
     */
    private static final MouseFeedPluginImages INSTANCE =
            new MouseFeedPluginImages();
    
    /**
     * @see #createImageRegistry()
     */
    private final ImageRegistry imageRegistry;

    /**
     * The singleton constructor.
     */
    private MouseFeedPluginImages() {
        imageRegistry = createImageRegistry();
    }

    /**
     * Creates the image registry. 
     * @return the image registry. Not <code>null</code>.
     */
    private ImageRegistry createImageRegistry() {
        final ImageRegistry registry = new ImageRegistry(Display.getCurrent());
        declareImages(registry);
        return registry;
    }

    /**
     * Declares the plugin images.
     * @param registry the registry to declare the images in.
     * Assumed not <code>null</code>. 
     */
    private void declareImages(ImageRegistry registry) {
        registry.put(IMG_TOGGLE_LOGGING, declareImage("run.gif"));
    }


    /**
     * Declare an image descriptor for the image registry.
     * @param path the path where the image can be found.
     * This path is relative to the plugin icon directory.
     * Assumed not blank.
     */
    private ImageDescriptor declareImage(String path) {
        ImageDescriptor desc = ImageDescriptor.getMissingImageDescriptor();
        try {
            desc = ImageDescriptor.createFromURL(getIconUrl(path));
        } catch (MalformedURLException e) {
            Activator.getDefault().log(e);
        }
        return desc;
    }

    /**
     * Returns the <code>ImageDescriptor</code> identified by the given key,
     * or <code>null</code> if it does not exist.
     * @param key the key of the image provided by the class,
     * one of the <code>IMG_XXX</code> constants.
     * @return the image corresponding to the key. <code>null</code> if the key
     * does not exist.
     */
    public ImageDescriptor getImageDescriptor(String key) {
        return imageRegistry.getDescriptor(key);
    }
    
    /**
     * The singleton instance.
     * @return the singleton instance. Never <code>null</code>.
     */
    public static MouseFeedPluginImages getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the URL of the provided icon path.
     * @param iconPath the icon path relatively to the icon directory.
     * Assumed not <code>null</code>.
     * @return the icon file URL. Never <code>null</code>.
     * @throws MalformedURLException on wrong URL.
     */
    private URL getIconUrl(String iconPath) throws MalformedURLException {
        return new URL(getIconBaseUrl(), iconPath);
    }

    /**
     * The URL of the icon base.
     * @return the url of the icon directory. Never <code>null</code>.
     */
    private URL getIconBaseUrl() {
        final String pathSuffix = "icons/";
        return Activator.getDefault().getBundle().getEntry(pathSuffix);
    }
}

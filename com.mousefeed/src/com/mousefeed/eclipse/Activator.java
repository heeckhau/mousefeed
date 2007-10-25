/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.eclipse;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle.
 * Implements {@link IStartup} to hook up the plugin listeners.
 * @author Andriy Palamarchuk
 */
public class Activator extends AbstractUIPlugin implements IStartup {

    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "com.mousefeed";

    /**
     * The shared instance.
     */
    private static Activator plugin;
    
    /**
     * The constructor.
     */
    public Activator() {
        assert plugin != null;
        plugin = this;
    }

    /**
     * On startup hooks up event listeners.
     */
    public void earlyStartup() {
        getDisplay().asyncExec(new Runnable() {
            public void run() {
                getDisplay().addFilter(
                      SWT.Selection, new GlobalSelectionListener());
            }
        });
    }

    /**
     * Current workbench display.
     * Not <code>null</code>.
     */
    private Display getDisplay() {
        return PlatformUI.getWorkbench().getDisplay();
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
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

/**
 * Hooks up the plugin listeners.
 * @author Andriy Palamarchuk
 */
public class Startup implements IStartup {

    /**
     * Default constructor does nothing.
     */
    public Startup() {
    }
    
    /**
     * Hooks up event listeners.
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
     * @return the workbench display.
     */
    public Display getDisplay() {
        return PlatformUI.getWorkbench().getDisplay();
    }
}

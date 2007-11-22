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

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;

/**
 * Disposes the provided font when the widget it listens to is disposed.
 * @author Andriy Palamarchuk 
 */
class DestroyFontDisposeListener implements DisposeListener {
    /**
     * The font to destroy.
     */
    private final Font newFont;

    public DestroyFontDisposeListener(Font newFont) {
        this.newFont = newFont;
    }

    @SuppressWarnings("unused")
    public void widgetDisposed(DisposeEvent e) {
        newFont.dispose();
    }
}
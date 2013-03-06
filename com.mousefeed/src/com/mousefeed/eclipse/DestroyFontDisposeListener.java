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

    public DestroyFontDisposeListener(final Font newFont) {
        this.newFont = newFont;
    }

    public void widgetDisposed(final DisposeEvent e) {
        newFont.dispose();
    }
}
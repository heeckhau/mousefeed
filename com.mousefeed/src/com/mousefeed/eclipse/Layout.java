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
import static org.apache.commons.lang.Validate.notNull;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;

/**
 * A layout helper.
 * Provides routines and constant for layout out the plugin UI.
 * Is not intended to be instantiated or sub-classed.
 *
 * @author Andriy Palamarchuk
 */
public final class Layout {
    /**
     * Distance between controls placed one above another. 
     */
    public static final int STACKED_V_OFFSET = 10;
    
    /**
     * Horizontal distance between two different consecutive controls.
     */
    public static final int H_OFFSET = 10;
    
    /**
     * Distance between a label and its control below.
     */
    public static final int STACKED_LABEL_V_OFFSET = 0;

    /**
     * Margin around the borders of a window.
     */
    public static final int WINDOW_MARGIN = 10;
    
    /**
     * Numerator of 100% when denominator is 100.
     */
    public static final int WHOLE_SIZE = 100;

    /**
     * Private constructor to insure no instances are ever created.
     */
    private Layout() { }

    /**
     * Places <code>control</code> right under <code>aboveControl</code>.
     * The controls' container must have <code>FormLayout</code>.  
     * @param control the control to place. Must have the same parent as
     * <code>aboveControl</code>. Not <code>null</code>.
     * @param aboveControl the control to place under.
     * If <code>null</code>, <code>control</code> is placed
     * {@link #WINDOW_MARGIN} pixels from the left side of the container,
     * and <code>gap</code> is interpreted as 0.
     * This is done to eliminate a special treatment of the topmost controls
     * in the client code. 
     * @param gap distance in pixels between the bottom of
     * <code>aboveControl</code> and top of <code>control</code>.
     * Greater than 0.
     * @return the layout data of the control.
     */
    public static FormData placeUnder(final Control control, final Control aboveControl,
            final int gap) {
        notNull(control);
        isTrue(aboveControl == null
                || control.getParent().equals(aboveControl.getParent()));
        isTrue(control.getParent().getLayout() instanceof FormLayout);
        isTrue(gap >= 0);

        final FormData formData = new FormData();
        formData.left = aboveControl == null
                ? getLeftAttachment()
                : new FormAttachment(aboveControl, 0, SWT.LEFT);
        final int offset = aboveControl == null ? WINDOW_MARGIN : gap; 
        formData.top = new FormAttachment(aboveControl, offset);
        control.setLayoutData(formData);
        return formData;
    }

    /**
     * Form attachment placing the control {@link #WINDOW_MARGIN} pixels from
     * the left border. 
     * @return form attachment. Never <code>null</code>.
     */
    private static FormAttachment getLeftAttachment() {
        return new FormAttachment(0, WINDOW_MARGIN);
    }
}
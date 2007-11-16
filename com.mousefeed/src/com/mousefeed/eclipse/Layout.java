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
     * Distance between a label and its control below.
     */
    public static final int STACKED_LABEL_V_OFFSET = 0;

    /**
     * Margin around the borders of a window.
     */
    public static final int WINDOW_MARGIN = 10;
    
    /**
     * Numerator of 100% of width when denominator is 100.
     */
    public static final int FULL_WIDTH = 100;

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
     */
    public static void placeUnder(Control control, Control aboveControl,
            int gap) {
        notNull(control);
        isTrue(aboveControl == null
                || control.getParent().equals(aboveControl.getParent()));
        isTrue(control.getParent().getLayout() instanceof FormLayout);
        isTrue(gap >= 0);

        final FormData formData = new FormData();
        formData.left = aboveControl == null
                ? getLeftAttachment()
                : new FormAttachment(aboveControl, 0, SWT.LEFT);
        final int offset = aboveControl == null ? 0 : gap; 
        formData.top = new FormAttachment(aboveControl, offset);
        control.setLayoutData(formData);
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
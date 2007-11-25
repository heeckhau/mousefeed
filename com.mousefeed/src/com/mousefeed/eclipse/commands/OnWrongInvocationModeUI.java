/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.eclipse.commands;

import static com.mousefeed.eclipse.Layout.STACKED_LABEL_V_OFFSET;
import static com.mousefeed.eclipse.Layout.STACKED_V_OFFSET;
import static com.mousefeed.eclipse.Layout.WHOLE_SIZE;
import static com.mousefeed.eclipse.Layout.WINDOW_MARGIN;
import static com.mousefeed.eclipse.Layout.placeUnder;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.OnWrongInvocationMode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Helper class to generate on wrong invocation mode action handling UI -
 * a label and a dropdown.
 * 
 * @author Andriy Palamarchuk
 */
public class OnWrongInvocationModeUI {
    /**
     * Creates the label.
     * @param container the container to create this control in.
     * Not <code>null</code>.
     * @param aboveControl the control above. <code>null</code> if this
     * is the topmost control in the container.
     * @param labelText the label text. Not <code>null</code>.
     * @return the label control. Never <code>null</code>.
     */
    public Label createLabel(Composite container, Control aboveControl,
            String labelText) {
        notNull(container);
        notNull(labelText);
        final Label label = new Label(container, SWT.NULL);
        label.setText(labelText);
        placeUnder(label, aboveControl, STACKED_V_OFFSET);
        return label;
    }

    /**
     * Creates the on wrong invocation mode handling dropdown.
     * @param container the container to create this control in.
     * Not <code>null</code>.
     * @param aboveControl the control above. <code>null</code> if this
     * is the topmost control in the container.
     * @return the combo control. Never <code>null</code>.
     */
    public Combo createCombo(Composite container, Control aboveControl) {
        notNull(container);
        isTrue(aboveControl == null || aboveControl instanceof Label);

        final Combo combo = new Combo(container, SWT.READ_ONLY);
        for (OnWrongInvocationMode mode : OnWrongInvocationMode.values()) {
            combo.add(mode.getLabel());
        }
        
        placeUnder(combo, aboveControl, STACKED_LABEL_V_OFFSET);
        final FormData formData = (FormData) combo.getLayoutData();
        formData.right = new FormAttachment(WHOLE_SIZE, -WINDOW_MARGIN);
        return combo;
    }
}

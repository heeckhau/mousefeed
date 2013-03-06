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
     * Default constructor does nothing.
     */
    public OnWrongInvocationModeUI() {
    }
    
    /**
     * Creates the label.
     * @param container the container to create this control in.
     * Not <code>null</code>.
     * @param aboveControl the control above. <code>null</code> if this
     * is the topmost control in the container.
     * @param labelText the label text. Not <code>null</code>.
     * @return the label control. Never <code>null</code>.
     */
    public Label createLabel(final Composite container, final Control aboveControl,
            final String labelText) {
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
    public Combo createCombo(final Composite container, final Control aboveControl) {
        notNull(container);
        isTrue(aboveControl == null || aboveControl instanceof Label);

        final Combo combo = new Combo(container, SWT.READ_ONLY);
        combo.setItems(OnWrongInvocationMode.getLabels());
        final FormData formData =
                placeUnder(combo, aboveControl, STACKED_LABEL_V_OFFSET);
        formData.right = new FormAttachment(WHOLE_SIZE, -WINDOW_MARGIN);
        return combo;
    }
}

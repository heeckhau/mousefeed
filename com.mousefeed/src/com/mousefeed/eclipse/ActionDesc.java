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

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.Validate.isTrue;
/**
 * Information about an action invoked by the user.
 * 
 * @author Andriy Palamarchuk
 */
public class ActionDesc {
    /**
     * @see #getLabel()
     */
    private String label;
    
    /**
     * @see #getAccelerator()
     */
    private String accelerator;

    /**
     * The action human-readable label.
     * @return the label. Should not be blank after initialized.
     * The '&' characters are removed from the original value.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the new label. Not blank.
     * @see #getLabel()
     */
    public void setLabel(String label) {
        isTrue(isNotBlank(label));
        this.label = label.replace("&", "");
    }
    
    /**
     * Indicates whether the action has a keyboard shortcut.
     * @return <code>true</code> if the action description has
     * keyboard shortcut.
     */
    public boolean hasAccelerator() {
        return getAccelerator() != null;
    }

    /**
     * The keyboard combination to call the action.
     * @return a string describing the action keyboard combination.
     * Can be <code>null</code> if there is no keyboard combination for the
     * action.
     */
    public String getAccelerator() {
        return accelerator;
    }

    /**
     * @param accelerator the new shortcut value. Can be <code>null</code>.
     * @see #getAccelerator()
     */
    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
    }
}

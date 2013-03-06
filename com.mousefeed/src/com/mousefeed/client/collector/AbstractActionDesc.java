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
package com.mousefeed.client.collector;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.Validate.isTrue;
/**
 * Information about an action invoked by the user.
 * 
 * @author Andriy Palamarchuk
 */
public abstract class AbstractActionDesc {
    /**
     * @see #getLabel()
     */
    private String label;
    
    /**
     * @see #getAccelerator()
     */
    private String accelerator;

    /**
     * The id of the user action.
     * @return the string identifying the action invoked by the user.
     * The default behavior is to return {@link #getLabel()}.
     * Subclasses should provide better values.
     * Never blank.
     */
    public String getId() {
        return getLabel();
    }

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
    public void setLabel(final String label) {
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
    public void setAccelerator(final String accelerator) {
        this.accelerator = accelerator;
    }
}

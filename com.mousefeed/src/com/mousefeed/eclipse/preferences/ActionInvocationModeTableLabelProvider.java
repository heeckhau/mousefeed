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
package com.mousefeed.eclipse.preferences;

import com.mousefeed.eclipse.preferences.ActionInvocationModeControl.Column;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Andriy Palamarchuk
 */
class ActionInvocationModeTableLabelProvider extends BaseLabelProvider 
        implements ITableLabelProvider {

    /**
     * Default constructor does nothing.
     */
    public ActionInvocationModeTableLabelProvider() {
    }
    
    // see base
    public boolean isLabelProperty(final Object element, final String property) {
        if (property.equals(Column.LABEL.name())) {
            return false;
        } else if (property.equals(Column.MODE.name())) {
            return true;
        } else {
            throw new IllegalArgumentException(
                    "Unrecognized property " + property);
        }
    }

    /**
     * Returns <code>null</code>.
     */
    public Image getColumnImage(final Object element, final int columnIndex) {
        return null;
    }

    // see base
    public String getColumnText(final Object element, final int columnIndex) {
        final ActionOnWrongInvocationMode mode =
                (ActionOnWrongInvocationMode) element;
        if (columnIndex == Column.LABEL.ordinal()) {
            return mode.getLabel();
        } else if (columnIndex == Column.MODE.ordinal()) {
            return mode.getOnWrongInvocationMode().getLabel();
        } else {
            throw new IllegalArgumentException(
                    "Unrecognized column index: " + columnIndex);
        }
    }
}

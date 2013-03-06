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


import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.preferences.ActionInvocationModeControl.Column;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Item;

/**
 * Cell modifier for the {@link ActionInvocationModeControl} table.
 *
 * @author Andriy Palamarchuk
 */
public class ActionInvocationModeTableCellModifier implements ICellModifier {
    /**
     * @see #ActionInvocationModeTableCellModifier(TableViewer)
     */
    private final TableViewer tableViewer;

    /**
     * Creates new table cell modifier.
     * @param tableViewer the table viewer backing the modifier table.
     * Not <code>null</code>.
     */
    public ActionInvocationModeTableCellModifier(final TableViewer tableViewer) {
        validateTableViewer(tableViewer);
        this.tableViewer = tableViewer;
    }

    /**
     * Validates table viewer constructor parameter.
     * @param viewer the object to validate.
     */
    void validateTableViewer(final TableViewer viewer) {
        notNull(viewer);
    }

    // see base
    public boolean canModify(final Object element, final String property) {
        // non-existing properties are not modifiable
        // do this instead of throwing an exception because other
        // methods don't throw an exception in this situation
        return property.equals(Column.MODE.name());
    }

    // see base
    public Object getValue(final Object element, final String property) {
        final ActionOnWrongInvocationMode mode =
                (ActionOnWrongInvocationMode) element;
        if (property.equals(Column.LABEL.name())) {
            return mode.getLabel();
        } else if (property.equals(Column.MODE.name())) {
            return mode.getOnWrongInvocationMode().ordinal();
        } else {
            // by the method contract
            return null;
        }
    }

    // see base
    public void modify(final Object element, final String property, final Object value) {
        final Object actionModeObject = element instanceof Item
                ? ((Item) element).getData()
                : element;   
        final ActionOnWrongInvocationMode actionMode =
                (ActionOnWrongInvocationMode) actionModeObject;
        if (property.equals(Column.MODE.name())) {
            final OnWrongInvocationMode mode =
                    OnWrongInvocationMode.values()[(Integer) value]; 
            actionMode.setOnWrongInvocationMode(mode);
            updateTableViewer(actionModeObject, property);
        } else {
            // by the method contract
            return;
        }
    }

    /**
     * Updates table viewer for the provided element and a property. 
     * @param element the element to update. Assumed not null.
     * @param property the property to update. Assumed not null.
     */
    void updateTableViewer(final Object element, final String property) {
        tableViewer.update(element, new String[] {property});
    }
}

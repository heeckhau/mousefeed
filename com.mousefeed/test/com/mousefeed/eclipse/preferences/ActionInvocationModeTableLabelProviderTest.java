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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.preferences.ActionInvocationModeControl.Column;
import org.junit.Test;


/**
 * @author Andriy Palamarchuk
 */
public class ActionInvocationModeTableLabelProviderTest {
    // sample data
    private static final String ID = "id1";
    private static final String LABEL = "Label 1";
    private static final OnWrongInvocationMode MODE =
            OnWrongInvocationMode.DO_NOTHING;

    @Test public void isLabelProperty() {
        final ActionInvocationModeTableLabelProvider labelProvider =
                new ActionInvocationModeTableLabelProvider();
        assertFalse(labelProvider.isLabelProperty(null, Column.LABEL.name()));
        assertTrue(labelProvider.isLabelProperty(null, Column.MODE.name()));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void isLabelProperty_unknownProperty() {
        final ActionInvocationModeTableLabelProvider labelProvider =
                new ActionInvocationModeTableLabelProvider();
        labelProvider.isLabelProperty(null, "unknown property");
    }

    @Test public void getColumnImage() {
        final ActionInvocationModeTableLabelProvider labelProvider =
                new ActionInvocationModeTableLabelProvider();
        labelProvider.getColumnImage(null, Column.LABEL.ordinal());
        labelProvider.getColumnImage(null, Column.MODE.ordinal());
        labelProvider.getColumnImage(null, -100);
    }
    
    @Test public void getColumnText() {
        final ActionInvocationModeTableLabelProvider labelProvider =
                new ActionInvocationModeTableLabelProvider();
        assertEquals(LABEL,
                labelProvider.getColumnText(getMode(), Column.LABEL.ordinal()));
        assertEquals(MODE.getLabel(),
                labelProvider.getColumnText(getMode(), Column.MODE.ordinal()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getColumnText_unknownColumnIdx() {
        final ActionInvocationModeTableLabelProvider labelProvider =
                new ActionInvocationModeTableLabelProvider();
        labelProvider.getColumnText(getMode(), -100);
    }

    /**
     * The sample mode.
     * @return never <code>null</code>.
     */
    private ActionOnWrongInvocationMode getMode() {
        final ActionOnWrongInvocationMode mode =
                new ActionOnWrongInvocationMode();
        mode.setId(ID);
        mode.setLabel(LABEL);
        mode.setOnWrongInvocationMode(MODE);
        return mode;
    }
}

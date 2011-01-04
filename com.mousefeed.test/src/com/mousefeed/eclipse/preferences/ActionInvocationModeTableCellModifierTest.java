/*
 * Copyright (C) Heavy Lifting Software 2007-2008.
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;
import org.junit.Test;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.preferences.ActionInvocationModeControl.Column;

/**
 * @author Andriy Palamarchuk
 */
public class ActionInvocationModeTableCellModifierTest {
    // sample data
    private static final String ID = "id1";
    private static final String LABEL = "Label 1";
    private static final OnWrongInvocationMode MODE =
            OnWrongInvocationMode.DO_NOTHING;
    private static final OnWrongInvocationMode MODE2 =
            OnWrongInvocationMode.ENFORCE;
    
    private static final TableViewer dummyTableViewer = new TableViewer(PlatformUI.getWorkbench().getDisplay().getActiveShell());

    @Test (expected = IllegalArgumentException.class)
    public void constructor() {
        new ActionInvocationModeControl(null);
    }

    @Test public void canModify() {
        final ActionInvocationModeTableCellModifier m =
                new TestModifier(dummyTableViewer);
        assertFalse(m.canModify(null, Column.LABEL.name()));
        assertTrue(m.canModify(new Object(), Column.MODE.name()));
    }

    @Test public void canModify_unknownProperty() {
        final ActionInvocationModeTableCellModifier m =  new TestModifier(dummyTableViewer);
        assertFalse(m.canModify(null, "unknown property"));
    }

    @Test public void getValue() {
        final ActionInvocationModeTableCellModifier m = new TestModifier(dummyTableViewer);
        assertEquals(LABEL, m.getValue(getMode(), Column.LABEL.name()));
        assertEquals(MODE.ordinal(), m.getValue(getMode(), Column.MODE.name()));
    }

    @Test public void getValue_unknownProperty() {
        final ActionInvocationModeTableCellModifier m = new TestModifier(dummyTableViewer);
        assertNull(m.getValue(getMode(), "unknown property"));
    }

    @Test public void modify_mode() {
        final ActionInvocationModeTableCellModifier m = new TestModifier(dummyTableViewer);
        final ActionOnWrongInvocationMode mode = getMode();
        
        assertFalse(mode.getOnWrongInvocationMode().equals(MODE2));
        m.modify(mode, Column.MODE.name(), MODE2.ordinal());
        assertEquals(MODE2, mode.getOnWrongInvocationMode());
    }

    @Test public void modify_label() {
        final ActionInvocationModeTableCellModifier m = new TestModifier(dummyTableViewer);
        m.modify(getMode(), Column.LABEL.name(), "some value");
    }

    @Test public void modify_unknownProperty() {
        final ActionInvocationModeTableCellModifier m = new TestModifier(dummyTableViewer);
        m.modify(getMode(), "unknown property", "some value");
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

    private static final class TestModifier extends
            ActionInvocationModeTableCellModifier {
        private TestModifier(TableViewer tableViewer) {
            super(tableViewer);
        }

        // see base
        @Override
        void validateTableViewer(TableViewer tableViewer) {
            // does nothing
        }

        // see base
        @Override
        void updateTableViewer(Object element, String property) {
            // does nothing
        }
    }
}

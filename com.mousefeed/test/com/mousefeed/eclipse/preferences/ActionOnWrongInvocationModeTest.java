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
import static org.junit.Assert.assertNull;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.client.collector.AbstractActionDesc;
import com.mousefeed.eclipse.ActionDescImpl;
import com.mousefeed.eclipse.preferences.ActionOnWrongInvocationMode.LabelComparator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Test;


/**
 * @author Andriy Palamarchuk
 */
public class ActionOnWrongInvocationModeTest {
    // sample data
    private static final String ID = "Id 1";
    private static final String LABEL = "Label 1";

    @Test(expected = IllegalArgumentException.class) 
    public void constructor_null() {
        create(null);
    }

    @Test public void constructor() {
        final ActionDescImpl actionDesc = new ActionDescImpl();
        actionDesc.setLabel(LABEL);
        actionDesc.setDef(ID);
        final ActionOnWrongInvocationMode mode = create(actionDesc);
        assertEquals(LABEL, mode.getLabel());
        assertEquals(ID, mode.getId());
        assertNull(mode.getOnWrongInvocationMode());
    }

    @Test public void testClone() throws CloneNotSupportedException {
        final ActionOnWrongInvocationMode mode =
                new ActionOnWrongInvocationMode();
        mode.setId(ID);
        mode.setLabel(LABEL);
        mode.setOnWrongInvocationMode(OnWrongInvocationMode.ENFORCE);
        
        final ActionOnWrongInvocationMode mode2 =
                (ActionOnWrongInvocationMode) mode.clone();
        EqualsBuilder.reflectionEquals(mode, mode2);
    }
    
    @Test public void testLabelComparator() throws CloneNotSupportedException {
        final ActionOnWrongInvocationMode mode1 =
                new ActionOnWrongInvocationMode();
        mode1.setId(ID);
        mode1.setLabel(LABEL);
        mode1.setOnWrongInvocationMode(OnWrongInvocationMode.ENFORCE);
        
        final LabelComparator comparator = new LabelComparator();
        final ActionOnWrongInvocationMode mode2 =
                (ActionOnWrongInvocationMode) mode1.clone();
        assertEquals(0, comparator.compare(mode1, mode2));
        
        mode1.setLabel(LABEL + "a");
        mode2.setLabel(LABEL + "b");
        assertEquals(-1, comparator.compare(mode1, mode2));

        mode1.setLabel(LABEL + "b");
        mode2.setLabel(LABEL + "a");
        assertEquals(1, comparator.compare(mode1, mode2));
    }

    /**
     * Creates new object.
     * @param actionDesc the action description to create the new object with.
     * Can be <code>null</code>.
     * @return the new object.
     */
    private ActionOnWrongInvocationMode create(AbstractActionDesc actionDesc) {
        return new ActionOnWrongInvocationMode(actionDesc);
    }
}

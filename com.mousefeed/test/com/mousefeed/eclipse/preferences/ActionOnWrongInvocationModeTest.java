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

import com.mousefeed.client.collector.ActionDesc;
import com.mousefeed.eclipse.ActionDescImpl;
import org.junit.Test;


/**
 * @author Andriy Palamarchuk
 */
public class ActionOnWrongInvocationModeTest {
    @Test(expected = IllegalArgumentException.class) 
    public void constructor_null() {
        create(null);
    }

    @Test public void constructor() {
        final String label = "Label 1";
        final String id = "Id 1";
        
        final ActionDescImpl actionDesc = new ActionDescImpl();
        actionDesc.setLabel(label);
        actionDesc.setDef(id);
        final ActionOnWrongInvocationMode mode = create(actionDesc);
        assertEquals(label, mode.getLabel());
        assertEquals(id, mode.getId());
        assertNull(mode.getOnWrongInvocationMode());
    }

    /**
     * Creates new object.
     * @param actionDesc the action description to create the new object with.
     * Can be <code>null</code>.
     * @return the new object.
     */
    private ActionOnWrongInvocationMode create(ActionDesc actionDesc) {
        return new ActionOnWrongInvocationMode(actionDesc);
    }
}

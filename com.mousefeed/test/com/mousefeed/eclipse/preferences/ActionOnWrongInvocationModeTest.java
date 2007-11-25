/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
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

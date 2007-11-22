/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.client.collector;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class CollectorTest {
    // sample data
    private static final String LABEL = "Label 1";
    private static final String ACCELERATOR = "Ctrl+Alt+F1";
    
    @Test(expected = IllegalArgumentException.class)
    public void onAction_null() {
        new Collector().onAction(null);
    }

    @Test public void getLastAction() {
        final Collector c = new Collector();
        assertNull(c.getLastAction());

        final ActionDescBase action = new TestActionDesc();
        action.setLabel(LABEL);
        action.setAccelerator(ACCELERATOR);
        c.onAction(action);
        assertEquals(action, c.getLastAction());
    }

    private static class TestActionDesc extends ActionDescBase {}
}

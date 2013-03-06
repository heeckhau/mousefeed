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

        final AbstractActionDesc action = new TestActionDesc();
        action.setLabel(LABEL);
        action.setAccelerator(ACCELERATOR);
        c.onAction(action);
        assertEquals(action, c.getLastAction());
    }

    private static class TestActionDesc extends AbstractActionDesc {}
}

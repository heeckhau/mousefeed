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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class ActionDescTest {

    @Test(expected = IllegalArgumentException.class)
    public void setLabel_blank() {
        new TestActionDesc().setLabel(" \n\t");
    }

    @Test public void setLabel() {
        final AbstractActionDesc d = new TestActionDesc();
        final String s1 = "abc";
        d.setLabel(s1);
        assertEquals(s1, d.getLabel());
        
        d.setLabel("a&b");
        assertEquals("ab", d.getLabel());
        assertEquals("ab", d.getId());
    }

    @Test public void setAccelerator() {
        final AbstractActionDesc d = new TestActionDesc();
        assertFalse(d.hasAccelerator());
        d.setAccelerator(null);
        assertFalse(d.hasAccelerator());
        assertNull(d.getAccelerator());
        d.setAccelerator("something");
        assertTrue(d.hasAccelerator());
    }
    
    private static class TestActionDesc extends AbstractActionDesc {}
}

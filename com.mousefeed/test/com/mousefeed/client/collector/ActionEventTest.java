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
package com.mousefeed.client.collector;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class ActionEventTest {
    @Test public void getType() {
        assertEquals(EventType.ACTION, new TestActionEvent().getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLabel_blank() {
        new TestActionEvent().setLabel(" \n\t");
    }

    @Test public void setLabel() {
        final ActionEvent d = new TestActionEvent();
        final String s1 = "abc";
        d.setLabel(s1);
        assertEquals(s1, d.getLabel());
        
        d.setLabel("a&b");
        assertEquals("ab", d.getLabel());
        assertEquals("ab", d.getId());
    }

    @Test public void setAccelerator() {
        final ActionEvent d = new TestActionEvent();
        assertFalse(d.hasAccelerator());
        d.setAccelerator(null);
        assertFalse(d.hasAccelerator());
        assertNull(d.getAccelerator());
        d.setAccelerator("something");
        assertTrue(d.hasAccelerator());
    }
    
    @Test public void testToString() {
        final String accelerator = "Accelerator 1";
        final String clientEvent = "Client Event 1";
        final String label = "Label 1";
        final String id = "Id 1";
        
        final ActionEvent d = new TestActionEvent() {
            @Override
            public String getId() {
                return id;
            }
        };
        d.setAccelerator(accelerator);
        d.setClientEvent(clientEvent);
        d.setLabel(label);
        
        final String s = d.toString();
        assertContains(s, EventType.ACTION.name());
        assertContains(s, id);
        assertNotContains(s, accelerator);
        assertNotContains(s, clientEvent);
        assertContains(s, label);
    }

    /**
     * Asserts that the string <code>substring</code> is contained inside of
     * <code>s</code>.
     */
    private void assertContains(String s, String substring) {
        assertTrue(s.contains(substring));
    }
    
    /**
     * Asserts that the string <code>substring</code> is not contained inside of
     * <code>s</code>.
     */
    private void assertNotContains(String s, String substring) {
        assertFalse(s.contains(substring));
    }

    private static class TestActionEvent extends ActionEvent {}
}

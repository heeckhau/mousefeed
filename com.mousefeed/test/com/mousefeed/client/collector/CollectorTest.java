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
public class CollectorTest {
    // sample data
    private static final String LABEL = "Label 1";
    private static final String ACCELERATOR = "Ctrl+Alt+F1";
    
    @Test(expected = IllegalArgumentException.class)
    public void onAction_null() {
        new Collector().onEvent(null);
    }

    @Test public void getLastAction() {
        final Collector c = new Collector();
        assertNull(c.getLastAction());

        final ActionEvent actionEvent = new TestActionEvent();
        actionEvent.setLabel(LABEL);
        actionEvent.setAccelerator(ACCELERATOR);
        c.onEvent(actionEvent);
        assertEquals(actionEvent, c.getLastAction());
    }
    
    @Test public void addEventListener_EventSpecific() {
        final Collector c = new Collector();
        final TestListener l = new TestListener();
        
        c.addEventListener(l, EventType.ACTION);

        assertFalse(l.wasCalled());
        c.onEvent(new TestActionEvent());
        assertTrue(l.wasCalled());
        
        l.reset();
        assertFalse(l.wasCalled());
        // TODO implement when other event type is implemented
//        c.onAction(new MouseEvent());
//        assertFalse(l.wasCalled());

        c.removeEventListener(l);
        assertFalse(l.wasCalled());
        c.onEvent(new TestActionEvent());
        assertFalse(l.wasCalled());
    }

    @Test public void addEventListener_General() {
        final Collector c = new Collector();
        final TestListener l = new TestListener();
        
        c.addEventListener(l, null);

        assertFalse(l.wasCalled());
        c.onEvent(new TestActionEvent());
        assertTrue(l.wasCalled());
        
        l.reset();
        assertFalse(l.wasCalled());
        // TODO implement when other event type is implemented
//        c.onAction(new MouseEvent());
//        assertTrue(l.wasCalled());
        
        c.removeEventListener(l);
        assertFalse(l.wasCalled());
        c.onEvent(new TestActionEvent());
        assertFalse(l.wasCalled());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEventListener_null() {
        new Collector().addEventListener(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEventListener_null() {
        new Collector().removeEventListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEventListener_nonExisting() {
        new Collector().removeEventListener(new TestListener());
    }

    private static class TestActionEvent extends ActionEvent {}
    
    private static class TestListener implements IEventListener {

        /**
         * Used to access the test listener events.
         */
        private Event event;

        public void event(Event event) {
            this.event = event;
        }
        
        public boolean wasCalled() {
            return event != null;
        }
        
        public void reset() {
            event = null;
            assertFalse(wasCalled());
        }
    }
}

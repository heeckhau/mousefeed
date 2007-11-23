/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */package com.mousefeed.client.collector;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class ActionDescImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void setLabel_blank() {
        new TestActionDesc().setLabel(" \n\t");
    }

    @Test public void setLabel() {
        final ActionDesc d = new TestActionDesc();
        final String s1 = "abc";
        d.setLabel(s1);
        assertEquals(s1, d.getLabel());
        
        d.setLabel("a&b");
        assertEquals("ab", d.getLabel());
        assertEquals("ab", d.getId());
    }

    @Test public void setAccelerator() {
        final ActionDesc d = new TestActionDesc();
        assertFalse(d.hasAccelerator());
        d.setAccelerator(null);
        assertFalse(d.hasAccelerator());
        assertNull(d.getAccelerator());
        d.setAccelerator("something");
        assertTrue(d.hasAccelerator());
    }
    
    private static class TestActionDesc extends ActionDesc {}
}

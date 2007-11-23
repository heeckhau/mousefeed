/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.eclipse;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class ActionDescImplTest {
    final static String label = "label 1";
    final static String className = "class 1";
    final static String def = "def 1";
    
    @Test public void setClassName() {
        final ActionDescImpl desc = new ActionDescImpl();
        assertNull(desc.getClassName());
        desc.setClassName(className);
        assertEquals(className, desc.getClassName());
        desc.setClassName(" \n\t");
        assertEquals(className, desc.getClassName());
    }
    
    @Test public void setDef() {
        final ActionDescImpl desc = new ActionDescImpl();
        assertNull(desc.getDef());
        desc.setDef(def);
        assertEquals(def, desc.getDef());
        desc.setDef(" \n\t");
        assertEquals(def, desc.getDef());
    }

    @Test public void  getId() {
        final ActionDescImpl desc = new ActionDescImpl();
        assertNull(desc.getId());
        desc.setLabel(label);
        assertEquals(label, desc.getId());
        
        // action or command class name
        desc.setClassName(className);
        assertEquals(className, desc.getClassName());
        assertEquals(className, desc.getId());
        
        // action or command def string
        desc.setDef(def);
        assertEquals(def, desc.getDef());
        assertEquals(def, desc.getId());
    }
}

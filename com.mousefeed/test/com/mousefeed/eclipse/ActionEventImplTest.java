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
package com.mousefeed.eclipse;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class ActionEventImplTest {
    final static String label = "label 1";
    final static String className = "class 1";
    final static String def = "def 1";
    
    @Test public void setClassName() {
        final ActionEventImpl desc = new ActionEventImpl();
        assertNull(desc.getClassName());
        desc.setClassName(className);
        assertEquals(className, desc.getClassName());
        desc.setClassName(" \n\t");
        assertEquals(className, desc.getClassName());
    }
    
    @Test public void setDef() {
        final ActionEventImpl desc = new ActionEventImpl();
        assertNull(desc.getDef());
        desc.setDef(def);
        assertEquals(def, desc.getDef());
        desc.setDef(" \n\t");
        assertEquals(def, desc.getDef());
    }

    @Test public void  getId() {
        final ActionEventImpl desc = new ActionEventImpl();
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

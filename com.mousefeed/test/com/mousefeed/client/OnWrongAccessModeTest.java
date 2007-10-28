/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.client;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class OnWrongAccessModeTest {
    @Test public void general() {
        assert values().length > 1;
    }

    @Test public void getLabel() {
        for (OnWrongAccessMode e : values()) {
            assert isNotBlank(e.getLabel());
            assert !e.name().equals(e.getLabel());
        }
    }
    
    @Test public void getLabelsAndNames() {
        final String[][] arr = OnWrongAccessMode.getLabelsAndNames();
        assertEquals(values().length, arr.length);
        assertEquals(2, arr[0].length);
        for (int i = 0; i < arr.length; i++) {
            assertEquals(values()[i].getLabel(), arr[i][0]);
            assertEquals(values()[i].name(), arr[i][1]);
        }
    }

    /**
     * Same as call to OnWrongAccessMode.values().
     */
    private OnWrongAccessMode[] values() {
        return OnWrongAccessMode.values();
    }
}

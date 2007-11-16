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

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class OnWrongInvocationModeTest {
    @Test public void general() {
        assert values().length > 1;
    }

    @Test public void getLabel() {
        for (OnWrongInvocationMode e : values()) {
            assert isNotBlank(e.getLabel());
            assert !e.name().equals(e.getLabel());
        }
    }
    
    /**
     * Same as call to {@link OnWrongInvocationMode#values()}.
     */
    private OnWrongInvocationMode[] values() {
        return OnWrongInvocationMode.values();
    }
}

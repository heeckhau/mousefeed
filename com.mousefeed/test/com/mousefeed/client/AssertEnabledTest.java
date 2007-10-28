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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Insures that tests run with assert enabled.
 * This check is done because "assert" is used to check some conditions
 * instead of using <code>org.junit.Assert.assertTrue</code>.
 * 
 * @author Andriy Palamarchuk
 */
public class AssertEnabledTest {
    @Test public void assertsEnabled() {
        boolean assertsEnabled = false;
        // intentional side effect!
        assert assertsEnabled = true; 
        assertTrue("Asserts must be enabled!", assertsEnabled);
    }
}

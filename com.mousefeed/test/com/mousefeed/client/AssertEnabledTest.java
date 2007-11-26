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

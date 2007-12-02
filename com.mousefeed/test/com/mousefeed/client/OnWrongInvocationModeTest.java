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

import static com.mousefeed.client.OnWrongInvocationMode.getLabels;
import static com.mousefeed.client.OnWrongInvocationMode.values;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.Validate;
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

    @Test public void getLabelsTest() {
        assertEquals(getLabels().length, values().length);
        assertEquals(getLabels()[0], values()[0].getLabel());
        Validate.noNullElements(getLabels());
    }
}

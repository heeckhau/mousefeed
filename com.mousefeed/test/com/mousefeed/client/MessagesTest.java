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

import java.util.MissingResourceException;

import org.junit.Test;

/**
 * @author Andriy Palamarchuk
 */
public class MessagesTest {
    /**
     * A short key example. 
     */
    private static final String SHORT_KEY = "message";

    /**
     * A full key example. 
     */
    private static final String FULL_KEY =
            MessagesTest.class.getSimpleName() + "." + SHORT_KEY;

    @Test public void get_common() {
        assertEquals("OK", new Messages().get("common.ok"));
    }

    @Test public void get_class() {
        final Messages messages = new Messages(MessagesTest.class);
        assert isNotBlank(messages.get(SHORT_KEY));
        assertEquals(messages.get(SHORT_KEY), messages.get(FULL_KEY));
        assert isNotBlank(new Messages().get(FULL_KEY));
        assert isNotBlank(new Messages(null).get(FULL_KEY));
    }

    @Test(expected = MissingResourceException.class)
    public void get_nonexisting() {
        new Messages().get(SHORT_KEY);
    }
    
    @Test(expected = MissingResourceException.class)
    public void get_nonexisting2() {
        new Messages(null).get(SHORT_KEY);
    }

    @Test(expected = NullPointerException.class)
    public void get_nullKey() {
        new Messages().get(null);
    }

    @Test(expected = NullPointerException.class)
    public void get_emptyKey() {
        new Messages().get("");
    }

    @Test(expected = NullPointerException.class)
    public void get_blankKey() {
        new Messages().get(" \t\n");
    }

    @Test public void get_withParameters() {
        final Messages m = new Messages(MessagesTest.class);
        final String p1 = "Parameter 1";
        final String p2 = "Parameter 2";
        final String p3 = "Parameter 3";
        
        // the test value has only 2 parameters
        assert m.get(SHORT_KEY).contains("{0}") : "Template returned as is";
        {
            final String s = m.get(SHORT_KEY, p1);
            assert s.contains(p1);
            assert !s.contains(p2);
        }
        {
            final String s = m.get(SHORT_KEY, p1, p2);
            assert s.contains(p1);
            assert s.contains(p2);
            assert !s.contains(p3);
        }
        {
            final String s = m.get(SHORT_KEY, p1, p2, p3);
            assert s.contains(p1);
            assert s.contains(p2);
            assert !s.contains(p3);
        }
    }
}

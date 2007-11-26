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
package com.mousefeed.eclipse.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.ActionDescImpl;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Andriy Palamarchuk
 */
public class PreferenceAccessorTest {
    // sample data
    private static final String ID1 = "action <> &id \n\t\"1!";
    private static final String ID2 = "action <> &id 1@ &amp;s";
    private File FILE;
    
    @Before public void initFile() throws IOException {
        FILE = File.createTempFile("test", null);
    }
    
    @After public void removeFile() {
        FILE.delete();
    }
    
    @Test public void getActionsWrongInvocationModeFile() throws IOException {
        final PreferenceAccessor preferences = new TestPreferenceAccessor();
        
        assertNull(preferences.getOnWrongInvocationMode(ID1));
        
        {
            final ActionOnWrongInvocationMode mode =
                    new ActionOnWrongInvocationMode();
            mode.setLabel("Label");
            mode.setId(ID1);
            
            assertEquals(0, FILE.length());
            mode.setOnWrongInvocationMode(OnWrongInvocationMode.ENFORCE);
            // is saved
            preferences.setOnWrongInvocationMode(mode);
        }
        assertTrue(FILE.length() > 0);
        assertEquals(OnWrongInvocationMode.ENFORCE,
                preferences.getOnWrongInvocationMode(ID1));
        
        {
            final ActionDescImpl actionDesc = new ActionDescImpl();
            actionDesc.setDef(ID2);
            actionDesc.setLabel("Label");
            
            final ActionOnWrongInvocationMode mode =
                    new ActionOnWrongInvocationMode(actionDesc);
            mode.setOnWrongInvocationMode(OnWrongInvocationMode.DO_NOTHING);
            preferences.setOnWrongInvocationMode(mode);
        }
        
        // the saved value is correctly retrieved
        {
            final PreferenceAccessor p = new TestPreferenceAccessor();
            assertEquals(OnWrongInvocationMode.ENFORCE,
                    p.getOnWrongInvocationMode(ID1));
            assertEquals(OnWrongInvocationMode.DO_NOTHING,
                    p.getOnWrongInvocationMode(ID2));
        }
        
        // removing action-specific handling
        assertEquals(OnWrongInvocationMode.ENFORCE,
                preferences.getOnWrongInvocationMode(ID1));
        preferences.removeOnWrongInvocaitonMode(ID1);
        assertNull(preferences.getOnWrongInvocationMode(ID1));
        
        // the saved value is correctly retrieved
        {
            final PreferenceAccessor p = new TestPreferenceAccessor();
            assertNull(p.getOnWrongInvocationMode(ID1));
            assertEquals(OnWrongInvocationMode.DO_NOTHING,
                    p.getOnWrongInvocationMode(ID2));
        }
    }

    /**
     * Provides the provided file as the actions invocation mode file.
     */
    private class TestPreferenceAccessor extends PreferenceAccessor {
        // see base
        @Override
        File getActionsWrongInvocationModeFile() {
            return FILE;
        }
    }
}

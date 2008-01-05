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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.eclipse.jface.preference.PreferenceStore;
import org.junit.Test;


/**
 * @author Andriy Palamarchuk
 */
public class PreferenceStoreProviderTest {
    @Test
    public void getInstance() {
        assertNotNull(PreferenceStoreProvider.getInstance());
    }
    
    @Test(expected = IllegalStateException.class)
    public void getPreferenceStore_not_initialized() {
        new PreferenceStoreProvider().getPreferenceStore();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setPreferenceStore_null() {
        new PreferenceStoreProvider().setPreferenceStore(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void setPreferenceStore_twice() {
        final PreferenceStoreProvider p = new PreferenceStoreProvider();
        p.setPreferenceStore(new PreferenceStore());
        p.setPreferenceStore(new PreferenceStore());
    }

    @Test
    public void getPreferenceStore() {
        final PreferenceStoreProvider p = new PreferenceStoreProvider();
        final PreferenceStore preferenceStore = new PreferenceStore();
        p.setPreferenceStore(preferenceStore);
        assertEquals(preferenceStore, p.getPreferenceStore());
    }
}

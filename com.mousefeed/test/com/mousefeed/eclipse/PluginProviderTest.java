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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.junit.Test;


/**
 * @author Andriy Palamarchuk
 */
public class PluginProviderTest {
    @Test
    public void getInstance() {
        assertNotNull(PluginProvider.getInstance());
    }
    
    @Test(expected = IllegalStateException.class)
    public void getPlugin_not_initialized() {
        new PluginProvider().getPlugin();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setPlugin_null() {
        new PluginProvider().setPlugin(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void setPlugin_twice() {
        final PluginProvider p = new PluginProvider();
        p.setPlugin(new TestPlugin());
        p.setPlugin(new TestPlugin());
    }

    @Test
    public void getPlugin() {
        final PluginProvider p = new PluginProvider();
        final TestPlugin plugin = new TestPlugin();
        p.setPlugin(plugin);
        assertEquals(plugin, p.getPlugin());
    }

    private final static class TestPlugin extends AbstractUIPlugin {
    }
}

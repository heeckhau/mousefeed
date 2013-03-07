/*
 * Copyright (C) Heavy Lifting Software, Robert Wloch 2012.
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

import com.mousefeed.client.collector.ActionDescTest;
import com.mousefeed.client.collector.CollectorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
@RunWith(Suite.class)
@SuiteClasses({ AssertEnabledTest.class, MessagesTest.class,
        OnWrongInvocationModeTest.class, ActionDescTest.class,
        CollectorTest.class })
public class AllClientTests {

}

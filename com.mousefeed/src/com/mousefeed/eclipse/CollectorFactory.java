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
package com.mousefeed.eclipse;

import com.mousefeed.client.collector.Collector;
import com.mousefeed.client.collector.EventType;

/**
 * Creates {@link Collector} and integrates it with Eclipse client.
 * 
 * @author Andriy Palamarchuk
 */
public class CollectorFactory {
    /**
     * Creates the collector.
     * @return new collector. Never <code>null</code>.
     */
    public Collector create() {
       final Collector collector = new Collector();
       collector.addEventListener(
               new FeedbackEventListener(), EventType.ACTION);
       collector.addEventListener(
               new RefreshActionInvocationCommandListener(), EventType.ACTION);
       return collector;
    }
}

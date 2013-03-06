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

import java.util.Collection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author Andriy Palamarchuk
 */
public class ActionInvocationModeTableContentProvider implements
        IStructuredContentProvider {

    /**
     * Default constructor does nothing.
     */
    public ActionInvocationModeTableContentProvider() {
    }
    
    public Object[] getElements(final Object inputElement) {
        return ((Collection<?>) inputElement).toArray();
    }

    /**
     * Does nothing.
     */
    public void dispose() {}

    // see base
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    }
}

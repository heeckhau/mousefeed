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

import com.mousefeed.client.Messages;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

/**
 * Generates text for the reminder to configure the last action invocation.
 *
 * @author Andriy Palamarchuk
 */
public class LastActionInvocationRemiderFactory {
    /**
     * The id of the action to configure action invocation.
     */
    private static final String CONFIGURE_ACTION_INVOCATION_ACTION_ID =
            "com.mousefeed.commands.configureActionInvocation";

    /**
     * Provides messages text.
     */
    private static final Messages MESSAGES = new Messages(
            LastActionInvocationRemiderFactory.class);

    /**
     * Default constructor does nothing.
     */
    public LastActionInvocationRemiderFactory() {
    }
    
    /**
     * The reminder text.
     * @return the reminder text. Not <code>null</code>.
     */
    public String getText() {
        final TriggerSequence[] bindings =
            getBindingService().getActiveBindingsFor(
                    CONFIGURE_ACTION_INVOCATION_ACTION_ID);
        final String binding = bindings.length == 0
                ? MESSAGES.get("configureActionInvocation-noBinding")
                : bindings[0].format();
        return MESSAGES.get("text", binding);
    }

    /**
     * The workbench binding service.
     * @return the binding service. Not null.
     */
    
    private IBindingService getBindingService() {
        return (IBindingService) getWorkbench().getAdapter(
                IBindingService.class);
    }

    /**
     * Current workbench. Not <code>null</code>.
     */
    private IWorkbench getWorkbench() {
        return PlatformUI.getWorkbench();
    }
}

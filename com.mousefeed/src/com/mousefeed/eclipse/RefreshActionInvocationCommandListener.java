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

import static com.mousefeed.eclipse.commands.ConfigureActionInvocationHandler.CONFIGURE_ACTION_INVOCATION_DEF;
import com.mousefeed.client.collector.Event;
import com.mousefeed.client.collector.IEventListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

/**
 * Refreshes the label of the command to configure action invocation.
 *
 * @author Andriy Palamarchuk
 */
public class RefreshActionInvocationCommandListener implements IEventListener {
    /**
     * The workbench command service.
     */
    private final ICommandService commandService =
        (ICommandService) getWorkbench().getService(ICommandService.class);

    // see base
    public void event(Event event) {
        commandService.refreshElements(CONFIGURE_ACTION_INVOCATION_DEF, null);
    }

    /**
     * Current workbench. Not <code>null</code>.
     */
    private IWorkbench getWorkbench() {
        return PlatformUI.getWorkbench();
    }
}

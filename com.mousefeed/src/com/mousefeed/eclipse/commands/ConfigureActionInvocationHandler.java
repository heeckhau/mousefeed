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
package com.mousefeed.eclipse.commands;

import com.mousefeed.client.collector.ActionDesc;
import com.mousefeed.eclipse.Activator;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Configures invocation mode for the last called action.
 * @author Andriy Palamarchuk
 */
public class ConfigureActionInvocationHandler extends AbstractHandler {

    // see base
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (getLastAction() == null) {
            return null;
        }
        final IWorkbenchWindow window =
                HandlerUtil.getActiveWorkbenchWindowChecked(event);
        final ConfigureActionInvocationDialog dlg =
                new ConfigureActionInvocationDialog(
                        window.getShell(), getLastAction());
        dlg.open();
        return null;
    }

    /**
     * The last action called by the user.
     * @return the last action. <code>null</code> if there was no action
     * called before since Eclipse started.
     */
    private ActionDesc getLastAction() {
        return Activator.getDefault().getCollector().getLastAction();
    }
}

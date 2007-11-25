/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
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

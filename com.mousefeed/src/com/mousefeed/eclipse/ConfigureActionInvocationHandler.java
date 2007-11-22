/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.eclipse;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Configures invocation mode for the last called action.
 * @author Andriy Palamarchuk
 */
public class ConfigureActionInvocationHandler extends AbstractHandler {

    // see base
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window =
                HandlerUtil.getActiveWorkbenchWindowChecked(event);
        MessageDialog.openInformation(
                window.getShell(),
                "TestPreferencesPlugin Plug-in",
                "Hello, Eclipse world");
        return null;
    }
}

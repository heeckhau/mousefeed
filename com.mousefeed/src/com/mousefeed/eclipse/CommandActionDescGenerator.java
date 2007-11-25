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

import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.collector.ActionDesc;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.menus.CommandContributionItem;

/**
/**
 * Generates {@link ActionDescImpl} from {@link CommandContributionItem}.
 * Pure strategy.
 *
 * @author Andriy Palamarchuk
 */
class CommandActionDescGenerator {
    /**
     * Retrieves a command from a command contribution item. 
     */
    private final CommandContributionItemCommandLocator locator =
            new CommandContributionItemCommandLocator();

    /**
     * The binding service used to retrieve bindings data.
     */
    private final IBindingService bindingService;
    
    /**
     * Constructor.
     */
    public CommandActionDescGenerator() {
        bindingService =
            (IBindingService) PlatformUI.getWorkbench().getAdapter(
                    IBindingService.class); 
    }

    /**
     * Generates action description from the command contribution item.
     * @param commandContributionItem the contribution item to generate
     * an action description for.
     * Not <code>null</code>.
     * @return the action description for the provided action.
     * Never <code>null</code>.
     */
    public ActionDesc generate(
            CommandContributionItem commandContributionItem) {
        notNull(commandContributionItem);

        final ActionDescImpl actionDesc = new ActionDescImpl();
        final Command command = locator.get(commandContributionItem);
        if (command == null) {
            return null;
        }
        try {
            actionDesc.setLabel(command.getName());
        } catch (NotDefinedException e) {
            // should never happen
            throw new RuntimeException(e);
        }
        final String commandId = command.getId();
        actionDesc.setDef(commandId);
        final TriggerSequence binding =
                bindingService.getBestActiveBindingFor(commandId);
        if (binding != null) {
            actionDesc.setAccelerator(binding.format());
        }
        return actionDesc;
    }
}

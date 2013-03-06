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

import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.collector.AbstractActionDesc;
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
    public AbstractActionDesc generate(
            final CommandContributionItem commandContributionItem) {
        notNull(commandContributionItem);

        final ActionDescImpl actionDesc = new ActionDescImpl();
        final Command command = locator.get(commandContributionItem);
        if (command == null) {
            return null;
        }
        try {
            actionDesc.setLabel(command.getName());
        } catch (final NotDefinedException e) {
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

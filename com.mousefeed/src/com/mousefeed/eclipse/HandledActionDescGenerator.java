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
import org.eclipse.e4.ui.workbench.renderers.swt.HandledContributionItem;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

/**
 * /** Generates {@link ActionDescImpl} from {@link HandledContributionItem}.
 * Pure strategy.
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
@SuppressWarnings("restriction")
class HandledActionDescGenerator {
    /**
     * Retrieves a command from a handled contribution item.
     */
    private final HandledContributionItemCommandLocator locator = new HandledContributionItemCommandLocator();

    /**
     * The binding service used to retrieve bindings data.
     */
    private final IBindingService bindingService;

    /**
     * Constructor.
     */
    public HandledActionDescGenerator() {
        bindingService = (IBindingService) PlatformUI.getWorkbench()
                .getAdapter(IBindingService.class);
    }

    /**
     * Generates action description from the handled contribution item.
     * 
     * @param handledContributionItem
     *            the contribution item to generate an action description for.
     *            Not <code>null</code>.
     * @return the action description for the provided action.
     */
    public AbstractActionDesc generate(
            final HandledContributionItem handledContributionItem) {
        notNull(handledContributionItem);

        final ActionDescImpl actionDesc = new ActionDescImpl();
        final Command command = locator.get(handledContributionItem);
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
        final TriggerSequence binding = bindingService
                .getBestActiveBindingFor(commandId);
        if (binding != null) {
            actionDesc.setAccelerator(binding.format());
        }
        return actionDesc;
    }

}

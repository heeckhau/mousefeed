/*
 * Copyright (C) Heavy Lifting Software 2007-2008.
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

import com.mousefeed.client.collector.ActionEvent;
import com.mousefeed.client.collector.Collector;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.menus.CommandContributionItem;

/**
 * Globally listens for the selection events.
 * 
 * @author Andriy Palamarchuk
 */
public class GlobalSelectionListener implements Listener {
    /**
     * Finds keyboard shortcut for an action.
     */
    private final ActionActionDescGenerator actionActionDescGenerator =
            new ActionActionDescGenerator();
    
    /**
     * Finds keyboard shortcut for a command.
     */
    private final CommandActionEventGenerator commandActionEventGenerator =
            new CommandActionEventGenerator(); 
    
    /**
     * Collects user activity data.
     */
    private final Collector collector = Activator.getDefault().getCollector();

    /**
     * Processes an event.  
     * @param event the event. Not <code>null</code>.
     */
    public void handleEvent(Event event) {
        final Widget widget = event.widget;
        if (widget instanceof ToolItem || widget instanceof MenuItem) {
            final Object data = widget.getData();
            if (data instanceof IContributionItem) {
                processContributionItem((IContributionItem) data, event);
            }
        } else {
            // do not handle these types of actions
        }
    }

    private void processContributionItem(IContributionItem contributionItem,
            Event event) {
        if (contributionItem instanceof SubContributionItem) {
            final SubContributionItem subCI =
                (SubContributionItem) contributionItem;
            processContributionItem(subCI.getInnerItem(), event);
        } else if (contributionItem instanceof ActionContributionItem) {
            final ActionContributionItem item =
                    (ActionContributionItem) contributionItem;
            final ActionEvent actionDesc =
                    actionActionDescGenerator.generate(item.getAction());
            processActionEvent(actionDesc);
        } else if (contributionItem instanceof CommandContributionItem) {
            final ActionEvent actionEvent =
                    commandActionEventGenerator.generate(
                            (CommandContributionItem) contributionItem); 
            actionEvent.setClientEvent(event);
            processActionEvent(actionEvent);
        } else {
            // no action contribution item on the widget data
        }
    }

    /**
     * Processes the prepared action description.
     * @param actionDesc the action description to process.
     * Assumed not <code>null</code>.
     */
    private void processActionEvent(ActionEvent actionDesc) {
        // skips the configure action invocation action
        if (CONFIGURE_ACTION_INVOCATION_DEF.equals(actionDesc.getId())) {
            return;
        }
        collector.onEvent(actionDesc);
    }
}

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

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.collector.ActionDescBase;
import com.mousefeed.client.collector.Collector;
import com.mousefeed.eclipse.preferences.PreferenceAccessor;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.menus.CommandContributionItem;

/**
 * Globally listens for the selection events.
 * 
 * @author Andriy Palamarchuk
 */
public class GlobalSelectionListener implements Listener {
    /**
     * The id of the command/action to configure invocation mode for other
     * actions.
     */
    private static final String CONFIGURE_ACTION_INVOCATION_DEF =
            "com.mousefeed.commands.configureActionInvocation";

    /**
     * Provides access to the plugin preferences.
     */
    private final PreferenceAccessor preferences = new PreferenceAccessor();
    
    /**
     * Finds keyboard shortcut for an action.
     */
    private final ActionAcceleratorFinder acceleratorFinder =
            new ActionAcceleratorFinder();
    
    /**
     * Collects user activity data.
     */
    private final Collector collector = Activator.getDefault().getCollector();
    
    /**
     * Retrieves a command from a command contribution item. 
     */
    private final CommandContributionItemCommandLocator locator =
            new CommandContributionItemCommandLocator();

    /**
     * Processes an event.  
     * @param event the event. Not <code>null</code>.
     */
    public void handleEvent(Event event) {
        final Widget widget = event.widget;
        if (widget instanceof ToolItem || widget instanceof MenuItem) {
            if (widget.getData() instanceof ActionContributionItem) {
                final ActionContributionItem item =
                        (ActionContributionItem) widget.getData();
                final ActionDescBase actionDesc =
                        generateActionDesc(item.getAction());
                processActionDesc(actionDesc, event);
            } else if (widget.getData() instanceof CommandContributionItem) {
                final ActionDescBase actionDesc = generateActionDesc(
                        (CommandContributionItem) widget.getData());
                processActionDesc(actionDesc, event);
            } else {
                // no action contribution item on the widget data
            }
        } else {
            // do not handle these types of actions
        }
    }

    /**
     * Processes the prepared action description.
     * @param actionDesc the action description to process.
     * Assumed not <code>null</code>.
     * @param event the original event. Assumed not <code>null</code>.
     */
    private void processActionDesc(ActionDescBase actionDesc, Event event) {
        // skips the configure action invocation action
        if (CONFIGURE_ACTION_INVOCATION_DEF.equals(actionDesc.getId())) {
            return;
        }
        giveActionFeedback(actionDesc, event);
        logUserAction(actionDesc);
    }

    /**
     * Generates {@link ActionDesc} for the provided command contribution item.
     * @param commandContributionItem the contribution item to generate
     * an action description for. Assumed not <code>null</code>.
     * @return
     */
    private ActionDescBase generateActionDesc(
            final CommandContributionItem commandContributionItem) {
        final IBindingService bindingService =
            (IBindingService) PlatformUI.getWorkbench().getAdapter(
                    IBindingService.class);
        final ActionDesc actionDesc = new ActionDesc();
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

    /**
     * Sends action information to {@link #collector}.
     * @param actionDesc the action data to send.
     * Assumed not <code>null</code>.
     */
    private void logUserAction(ActionDescBase actionDesc) {
        collector.onAction(actionDesc);
    }

    /**
     * Generates action description from the action.
     * @param action
     * @return
     */
    private ActionDescBase generateActionDesc(IAction action) {
        notNull(action);

        final ActionDescBase actionDesc = new ActionDesc();
        actionDesc.setLabel(action.getText());
        actionDesc.setAccelerator(acceleratorFinder.find(action));
        return actionDesc;
    }

    /**
     * Depending on the settings reports to the user that action can be called
     * by the action accelerator, cancels the action.
     *
     * @param actionDesc the populated action description.
     * Must have a keyboard shortcut defined. Not <code>null</code>.
     */
    private void giveActionFeedback(ActionDescBase actionDesc, Event event) {
        notNull(actionDesc);
        isTrue(StringUtils.isNotBlank(actionDesc.getLabel()));

        if (!actionDesc.hasAccelerator()) {
            return;
        }
        if (!preferences.isInvocationControlEnabled()) {
            return;
        }

        switch (preferences.getOnWrongInvocationMode()) {
        case DO_NOTHING:
            // go on
            break;
        case REMIND:
            new NagPopUp(actionDesc.getLabel(),
                    actionDesc.getAccelerator(), false).open();
            break;
        case ENFORCE:
            cancelEvent(event);
            new NagPopUp(actionDesc.getLabel(),
                    actionDesc.getAccelerator() + "", true).open();
            break;
        default:
            throw new AssertionError();
        }
    }

    /**
     * Stops further processing of the specified event.
     * @param event the event to disable. Assumed not <code>null</code>.
     */
    private void cancelEvent(Event event) {
        event.type = SWT.None;
        event.doit = false;
    }
}

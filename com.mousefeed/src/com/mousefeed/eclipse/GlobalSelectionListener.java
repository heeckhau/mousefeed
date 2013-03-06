/*
 * Copyright (C) Heavy Lifting Software 2007, Robert Wloch 2012.
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

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.client.collector.AbstractActionDesc;
import com.mousefeed.client.collector.Collector;
import com.mousefeed.eclipse.preferences.PreferenceAccessor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.ui.workbench.renderers.swt.HandledContributionItem;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;

/**
 * Globally listens for the selection events.
 * 
 * @author Andriy Palamarchuk
 * @author Robert Wloch
 */
@SuppressWarnings("restriction")
public class GlobalSelectionListener implements Listener {
    /**
     * The id of the command/action to configure invocation mode for other
     * actions.
     */
    private static final String CONFIGURE_ACTION_INVOCATION_DEF = "com.mousefeed.commands.configureActionInvocation";

    /**
     * Provides access to the plugin preferences.
     */
    private final PreferenceAccessor preferences = PreferenceAccessor
            .getInstance();

    /**
     * Finds keyboard shortcut for an action.
     */
    private final ActionActionDescGenerator actionActionDescGenerator = new ActionActionDescGenerator();

    /**
     * Finds keyboard shortcut for a command.
     */
    private final CommandActionDescGenerator commandActionDescGenerator = new CommandActionDescGenerator();

    /**
     * Finds keyboard shortcut for a command.
     */
    private final HandledActionDescGenerator handledActionDescGenerator = new HandledActionDescGenerator();

    /**
     * Collects user activity data.
     */
    private final Collector collector = Activator.getDefault().getCollector();

    /**
     * The workbench command service.
     */
    private final ICommandService commandService = (ICommandService) getWorkbench()
            .getService(ICommandService.class);

    /**
     * Counts the number of times an action or command is invoked.
     */
    private final Map<String, Integer> actionUsageMonitor = new HashMap<String, Integer>();

    /**
     * Default constructor does nothing.
     */
    public GlobalSelectionListener() {
    }

    /**
     * Processes an event.
     * 
     * @param event
     *            the event. Not <code>null</code>.
     */
    @Override
    public void handleEvent(final Event event) {
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

    private void processContributionItem(
            final IContributionItem contributionItem, final Event event) {
        if (contributionItem instanceof SubContributionItem) {
            final SubContributionItem subCI = (SubContributionItem) contributionItem;
            processContributionItem(subCI.getInnerItem(), event);
        } else if (contributionItem instanceof ActionContributionItem) {
            final ActionContributionItem item = (ActionContributionItem) contributionItem;
            final AbstractActionDesc actionDesc = actionActionDescGenerator
                    .generate(item.getAction());
            processActionDesc(actionDesc, event);
        } else if (contributionItem instanceof CommandContributionItem) {
            final AbstractActionDesc actionDesc = commandActionDescGenerator
                    .generate((CommandContributionItem) contributionItem);
            processActionDesc(actionDesc, event);
        } else if (contributionItem instanceof HandledContributionItem) {
            final AbstractActionDesc actionDesc = handledActionDescGenerator
                    .generate((HandledContributionItem) contributionItem);
            processActionDesc(actionDesc, event);
        } else {
            // no action contribution item on the widget data
        }
    }

    /**
     * Processes the prepared action description.
     * 
     * @param actionDesc
     *            the action description to process. Assumed not
     *            <code>null</code>.
     * @param event
     *            the original event. Assumed not <code>null</code>.
     */
    private void processActionDesc(final AbstractActionDesc actionDesc,
            final Event event) {
        // skips the configure action invocation action
        if (CONFIGURE_ACTION_INVOCATION_DEF.equals(actionDesc.getId())) {
            return;
        }
        giveActionFeedback(actionDesc, event);
        logUserAction(actionDesc);
        commandService.refreshElements(CONFIGURE_ACTION_INVOCATION_DEF, null);
    }

    /**
     * Current workbench. Not <code>null</code>.
     */
    private IWorkbench getWorkbench() {
        return PlatformUI.getWorkbench();
    }

    /**
     * Sends action information to {@link #collector}.
     * 
     * @param actionDesc
     *            the action data to send. Assumed not <code>null</code>.
     */
    private void logUserAction(final AbstractActionDesc actionDesc) {
        collector.onAction(actionDesc);
    }

    /**
     * Depending on the settings reports to the user that action can be called
     * by the action accelerator, cancels the action.
     * 
     * @param actionDesc
     *            the populated action description. Must have a keyboard
     *            shortcut defined. Not <code>null</code>.
     */
    private void giveActionFeedback(final AbstractActionDesc actionDesc,
            final Event event) {
        notNull(actionDesc);
        isTrue(StringUtils.isNotBlank(actionDesc.getLabel()));

        if (!preferences.isInvocationControlEnabled()) {
            return;
        }
        final String id = actionDesc.getId();
        if (!actionDesc.hasAccelerator()) {
            Integer currentCount = actionUsageMonitor.get(id);
            if (currentCount == null) {
                currentCount = Integer.valueOf(0);
            }
            currentCount = currentCount + 1;
            actionUsageMonitor.put(id, currentCount);
            if (isConfigureKeyboardShortcutEnabled(currentCount.intValue())
                    && isConfigurableAction(actionDesc)) {
                new NagPopUp(actionDesc.getLabel(), actionDesc.getId()).open();
            }
            return;
        }

        switch (getOnWrongInvocationMode(id)) {
        case DO_NOTHING:
            // go on
            break;
        case REMIND:
            new NagPopUp(actionDesc.getLabel(), actionDesc.getAccelerator(),
                    false).open();
            break;
        case ENFORCE:
            cancelEvent(event);
            new NagPopUp(actionDesc.getLabel(), actionDesc.getAccelerator()
                    + "", true).open();
            break;
        default:
            throw new AssertionError();
        }
    }

    /**
     * Checks if for the current action a keyboard shortcut can be configured.
     * 
     * @param actionDesc
     *            ActionDesc for the current action. Not null.
     * @return true, if the current action has at least one ParameterizedCommand
     *         (only those are listed in the keys preference page), false else.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected boolean isConfigurableAction(final AbstractActionDesc actionDesc) {
        final String actionId = actionDesc.getId();

        final Command command = commandService.getCommand(actionId);
        if (command != null) {
            final HashSet allParameterizedCommands = new HashSet();
            try {
                allParameterizedCommands.addAll(ParameterizedCommand
                        .generateCombinations(command));
            } catch (final NotDefinedException e) {
                // It is safe to just ignore undefined commands.
            }
            return !allParameterizedCommands.isEmpty();
        }
        return false;
    }

    /**
     * Checks, if keyboard shortcut configuration should be activated.
     * 
     * @param currentCount
     *            current counter for an action invocation
     * @return true, if the configure keyboard shortcut preference is enabled
     *         and currentCount exceeds the value of the action invocation
     *         threshold property.
     */
    public boolean isConfigureKeyboardShortcutEnabled(final int currentCount) {
        final boolean isConfigureKeyboardShortcutEnabled = preferences
                .isConfigureKeyboardShortcutEnabled();
        final boolean isCounterAboveThreshold = currentCount > preferences
                .getConfigureKeyboardShortcutThreshold();
        return isConfigureKeyboardShortcutEnabled && isCounterAboveThreshold;
    }

    /**
     * Returns the wrong invocation mode handling for the action with the
     * specified id.
     * 
     * @param id
     *            the action id. Assumed not <code>null</code>.
     * @return the mode. Not <code>null</code>.
     */
    private OnWrongInvocationMode getOnWrongInvocationMode(final String id) {
        final OnWrongInvocationMode mode = preferences
                .getOnWrongInvocationMode(id);
        return mode == null ? preferences.getOnWrongInvocationMode() : mode;
    }

    /**
     * Stops further processing of the specified event.
     * 
     * @param event
     *            the event to disable. Assumed not <code>null</code>.
     */
    private void cancelEvent(final Event event) {
        event.type = SWT.None;
        event.doit = false;
    }
}

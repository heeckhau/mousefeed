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

import com.mousefeed.eclipse.preferences.PreferenceAccessor;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Globally listens for the selection events.
 * 
 * @author Andriy Palamarchuk
 */
public class GlobalSelectionListener implements Listener {
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
     * Processes an event.  
     * @param event the event. Not <code>null</code>.
     */
    public void handleEvent(Event event) {
        final Widget widget = event.widget;
        if (widget instanceof ToolItem || widget instanceof MenuItem) {
            if (widget.getData() instanceof ActionContributionItem) {
                final ActionContributionItem item =
                        (ActionContributionItem) widget.getData();
                final ActionDesc actionDesc =
                        generateActionDesc(item.getAction());
                giveActionFeedback(actionDesc, event);
            } else {
                // no action contribution item on the widget data
            }
        } else {
            // do not handle these types of actions
        }
    }

    /**
     * Generates action description from the action.
     * @param action
     * @return
     */
    private ActionDesc generateActionDesc(IAction action) {
        notNull(action);

        final ActionDesc actionDesc = new ActionDesc();
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
    private void giveActionFeedback(ActionDesc actionDesc, Event event) {
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

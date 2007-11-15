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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ExternalActionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ExternalActionManager.ICallback;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.keys.IBindingService;

//CHECKSTYLE:OFF

/**
 * Globally listens for the selection events.
 * 
 * @author Andriy Palamarchuk
 */
public class GlobalSelectionListener implements Listener {

    /**
     * Searches for an action inside of
     * <code>org.eclipse.ui.actions.TextActionHandler</code> utility actions.
     */
    private final TextActionHandlerActionLocator actionSearcher =
        new TextActionHandlerActionLocator();
    
    /**
     * The activity manager for the associated workbench.
     */
    private final IActivityManager activityManager;

    /**
     * The binding service for the associated workbench.
     */
    private final IBindingService bindingService;
    
    /**
     * Provides access to the plugin preferences.
     */
    private final PreferenceAccessor preferences = new PreferenceAccessor();

    /**
     * The constructor.
     */
    public GlobalSelectionListener() {
        bindingService =
            (IBindingService) getWorkbench().getAdapter(IBindingService.class);
        activityManager =
            getWorkbench().getActivitySupport().getActivityManager();
    }

    /** {@inheritDoc} */
    public void handleEvent(Event event) {
        final Widget widget = event.widget;
        if (widget instanceof ToolItem || widget instanceof MenuItem) {
            if (widget.getData() instanceof ActionContributionItem) {
                final ActionContributionItem item =
                        (ActionContributionItem) widget.getData();
                maybeReportActionAccelerator(item.getAction());
            } else {
                // no action contribution item on the widget data
            }
        } else {
            // do not handle these types of actions
        }
    }

    /**
     * Notifies user if the action has has an associated accelerator.  
     * @param action the action to report about.
     * Not <code>null</code>.
     */
    private void maybeReportActionAccelerator(IAction action) {
        notNull(action);

        if (reportAcceleratorForAction(action)) {
            return;
        }
        
        final TriggerSequence triggerSequence = findActionBinding(action);
        if (triggerSequence != null) {
            reportActionAccelerator(
                    action.getText(), triggerSequence.toString());
        }
    }

    /**
     * Scans current bindings, returns a trigger sequence, associated with this
     * action. Uses euristic to find an association.
     * Assumes that if a binding action and the provided action are the same,
     * if they have same class.
     * This logic can potentially fail if an action class is used for
     * different actions.
     * @param action the action to search trigger sequence for.
     * Returns <code>null</code> if the action is <code>null</code>.
     * @return the trigger sequence associated with the action
     * or <code>null</code> if such sequence was not found.
     */
    @SuppressWarnings("unchecked")
    private TriggerSequence findActionBinding(IAction action) {
        if (action == null) {
            return null;
        }
        if (action instanceof RetargetAction) {
            return findActionBinding(
                    ((RetargetAction) action).getActionHandler());
        }

        final Map matches = bindingService.getPartialMatches(
                KeySequence.getInstance());
        final Class<? extends IAction> actionClass = action.getClass();
        for (Object o : matches.keySet()) {
            final TriggerSequence triggerSequence = (TriggerSequence) o;
            final Binding binding = (Binding) matches.get(triggerSequence);
            final Command command =
                    binding.getParameterizedCommand().getCommand();
            final IHandler handler = getCommandHandler(command);
            if (!(handler instanceof ActionHandler)) {
                continue;
            }

            if (isCommandEnabled(command)) {
                final ActionHandler actionHandler = (ActionHandler) handler;
                final IAction boundAction = actionHandler.getAction();
                if (boundAction == null) {
                    continue;
                }
                if (boundAction.getClass().equals(actionClass)) {
                    return triggerSequence;
                }
                if (!(boundAction instanceof RetargetAction)) {
                    continue;
                }
                final IAction searchTarget =
                        ((RetargetAction) boundAction).getActionHandler(); 
                if (searchTarget == null) {
                    continue;
                }
                if (searchTarget.getClass().equals(actionClass)) {
                    return triggerSequence;
                }
                if (actionSearcher.isSearchable(searchTarget)) {
                    final String id = actionSearcher.findActionDefinitionId(
                            action, searchTarget);
                    reportAcceleratorForActionDefinition(id, action.getText());
                }
            }
        }
        return null;
    }

    /**
     * Reports an accelerator for an action.
     * @param action the action to report an accelerator for.
     * Not <code>null</code>.
     * @return <code>true</code> if no further processing is required.
     * This happens when an accelerator for an action is found.
     */
    private boolean reportAcceleratorForAction(IAction action) {
        if (action.getAccelerator() != 0) {
            reportActionAccelerator(action.getText(),
                    convertAcceleratorToStr(action.getAccelerator()));
            return true;
        } else if (reportAcceleratorForActionDefinition(
                action.getActionDefinitionId(), action.getText())) {
            return true;
        } else if (action instanceof RetargetAction) {
            
            final RetargetAction a = (RetargetAction) action;
            if (a.getActionHandler() != null) {
                return reportAcceleratorForAction(a.getActionHandler());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Converts the provided key combination code to accelerator.
     * @param accelerator The accelerator to convert; should be a valid SWT
     * accelerator value.
     * @return The equivalent key stroke; never <code>null</code>.
     */
    private String convertAcceleratorToStr(final int accelerator) {
        return
            SWTKeySupport.convertAcceleratorToKeyStroke(accelerator).format();
    }

    /**
     * Calls {@link #reportActionAccelerator(String, String)} for the provided
     * action, if the action definition is associated with accelerator. 
     * @param definitionId the action to report for.
     * If <code>null</code> or empty the method returns <code>false</code> right
     * away.
     * @param actionName the name to report action for.
     * Not blank;
     * @return <code>true</code> if no further analysis is required.
     * This happens when action has a definition id.
     */
    private boolean reportAcceleratorForActionDefinition(
            final String definitionId, final String actionName) {
        isTrue(StringUtils.isNotBlank(actionName));
        
        if (StringUtils.isBlank(definitionId)) {
            return false;
        }
        final String acceleratorStr =
            getActionDefinitionAccelerator(definitionId);
        if (StringUtils.isNotBlank(acceleratorStr)) {
            reportActionAccelerator(actionName, acceleratorStr);
        }
        return true;
    }

    /**
     * Reports to the user that action could be called by the provided
     * accelerator.
     * 
     * @param actionName
     *            the action name. If necessary, the method removes '&'
     *            characters from the action name. Not blank.
     * @param acceleratorStr
     *            the string describing the accelerator. Not blank.
     */
    private void reportActionAccelerator(String actionName,
            String acceleratorStr) {
        isTrue(StringUtils.isNotBlank(actionName));
        isTrue(StringUtils.isNotBlank(acceleratorStr));

        switch (preferences.getPromoteKeys()) {
        case DO_NOTHING:
            // go on
            break;
        case REMIND:
            new NagPopUp(actionName, acceleratorStr).open();
            break;
        case ENFORCE:
            // TODO implement
            new NagPopUp(actionName, acceleratorStr + "").open();
            break;
        default:
            throw new AssertionError();
        }
    }

    /**
     * Retrieves command handler from a command.
     * @param command the command to retrieve the handler from.
     * Not <code>null</code>.
     * @return the handler. Returns <code>null</code>,
     * if can't retrieve a handler. 
     */
    private IHandler getCommandHandler(Command command) {
        try {
            final Method method = Command.class.getDeclaredMethod("getHandler");
            method.setAccessible(true);
            return (IHandler) method.invoke(command);
        } catch (SecurityException e) {
            // want to know when this happens
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            // should never happen
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            // should never happen
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            // should never happen
            throw new AssertionError(e);
        }
    }

    /**
     * Returns <code>true</code> if the command is defined and is enabled. 
     * @param command the command to check. Not <code>null</code>.
     */
    private boolean isCommandEnabled(Command command) {
        return command.isDefined()
                && activityManager.getIdentifier(command.getId()).isEnabled();
    }

    /**
     * Finds action definition accelerator.
     * @param actionDefinitionId the action definition id to search accelerator
     * for.
     * Not blank.
     * @return A string representation of the accelerator. This is the
     * string representation that should be displayed to the user.
     */
    private String getActionDefinitionAccelerator(String actionDefinitionId) {
        isTrue(StringUtils.isNotBlank(actionDefinitionId));
        final ICallback callback =
                ExternalActionManager.getInstance().getCallback();
        return callback.getAcceleratorText(actionDefinitionId);
    }

    /**
     * Current workbench. Not <code>null</code>.
     */
    private IWorkbench getWorkbench() {
        return PlatformUI.getWorkbench();
    }
//CHECKSTYLE:ON
}

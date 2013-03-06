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

import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.collector.AbstractActionDesc;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.ExternalActionManager;
import org.eclipse.jface.action.ExternalActionManager.ICallback;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.internal.keys.BindingService;
import org.eclipse.ui.keys.IBindingService;

//COUPLING:OFF
/**
 * Generates {@link ActionDescImpl} from {@link IAction}. Pure strategy. The
 * logic was developed by trying different actions, making sure all the
 * ActionDesc components are correctly generated.
 * 
 * @author Andriy Palamarchuk
 * @author Robert Wloch
 */
@SuppressWarnings("restriction")
public class ActionActionDescGenerator {

    /**
     * Stores the generated action description.
     */
    private ActionDescImpl actionDesc;

    /**
     * Searches for an action inside of
     * <code>org.eclipse.ui.actions.TextActionHandler</code> utility actions.
     */
    private final TextActionHandlerActionLocator actionSearcher = new TextActionHandlerActionLocator();

    /**
     * The binding service for the associated workbench.
     */
    private final IBindingService bindingService;

    /**
     * The activity manager for the associated workbench.
     */
    private final IActivityManager activityManager;

    /**
     * Creates new finder.
     */
    public ActionActionDescGenerator() {
        bindingService = (IBindingService) getWorkbench().getAdapter(
                IBindingService.class);
        activityManager = getWorkbench().getActivitySupport()
                .getActivityManager();
    }

    /**
     * Generates action description from the action.
     * 
     * @param action
     *            the action to generate description for. Not <code>null</code>.
     * @return the action description for the provided action. Never
     *         <code>null</code>.
     */
    public AbstractActionDesc generate(final IAction action) {
        notNull(action);

        actionDesc = new ActionDescImpl();
        // actions may have no text nor tool tip text
        final String text = action.getText();
        final String toolTipText = action.getToolTipText();
        if (text != null) {
            actionDesc.setLabel(text);
        } else if (toolTipText != null) {
            actionDesc.setLabel(toolTipText);
        } else {
            actionDesc.setLabel(action.getClass().getSimpleName());
        }
        actionDesc.setClassName(action.getClass().getName());
        extractActionData(action);
        return actionDesc;
    }

    // clear, simple structure, leave extra returns
    /**
     * Scans action for data to populate action description.
     * 
     * @param action
     *            the action to search accelerator for. Not <code>null</code>.
     */
    public void extractActionData(final IAction action) {
        notNull(action);
        fromActionAccelerator(action.getAccelerator());
        fromActionDefinition(action.getActionDefinitionId());

        // retarget action?
        if (action instanceof RetargetAction) {
            final RetargetAction a = (RetargetAction) action;
            if (a.getActionHandler() != null) {
                extractActionData(a.getActionHandler());
            }
        }

        fromActionBinding(action);
    }

    /**
     * Populates {@link #actionDesc} from the action definition id.
     * 
     * @param actionDefinitionId
     *            . <code>null</code> if not defined.
     */
    private void fromActionDefinition(final String definitionId) {
        if (definitionId == null) {
            return;
        }
        actionDesc.setDef(definitionId);
        final String s = findAcceleratorForActionDefinition(definitionId);
        if (s != null) {
            actionDesc.setAccelerator(s);
        }
    }

    /**
     * Populates {@link #actionDesc} from the action accelerator.
     * 
     * @param accelerator
     *            . 0 if not defined.
     */
    private void fromActionAccelerator(final int accelerator) {
        if (accelerator != 0) {
            actionDesc.setAccelerator(keyToStr(accelerator));
        }
    }

    /**
     * Finds keyboard shortcut for the action definition id.
     * 
     * @param actionDefinitionId
     *            the action definition id to search accelerator for. If blank,
     *            the method returns <code>null</code>
     * @return A human-readable string representation of the accelerator. Is
     *         <code>null</code> if can't be found.
     */
    private String findAcceleratorForActionDefinition(final String definitionId) {
        if (StringUtils.isBlank(definitionId)) {
            return null;
        }
        final ICallback callback = ExternalActionManager.getInstance()
                .getCallback();
        return callback.getAcceleratorText(definitionId);
    }

    /**
     * Scans current bindings, returns a trigger sequence, associated with this
     * action. Uses heuristic to find an association. Assumes that if a binding
     * action and the provided action are the same, if they have same class.
     * This logic can potentially fail if an action class is used for different
     * actions.
     * 
     * @param action
     *            the action to search trigger sequence for. Returns
     *            <code>null</code> if the action is <code>null</code>.
     * @return the accelerator from the trigger sequence associated with the
     *         action or <code>null</code> if such sequence was not found.
     */
    private String fromActionBinding(final IAction action) {
        if (action == null) {
            return null;
        }
        if (action instanceof RetargetAction) {
            return fromActionBinding(((RetargetAction) action)
                    .getActionHandler());
        }

        return scanBindings(action);
    }

    /**
     * Scans bindings for the action.
     * 
     * @param action
     *            the action to scan bindings for. Assumed not <code>null</code>
     *            .
     * @return the accelerator from the action trigger sequence.
     *         <code>null</code> if the binding was not found.
     */
    @SuppressWarnings("rawtypes")
    private String scanBindings(final IAction action) {
        final KeySequence keySequence = KeySequence.getInstance();
        // In eclipse 4 BindingService#getPartialMatches() doesn't call the
        // BindingManager any more leading to an ArrayIndexOutOfBoundsException
        // due to bad code quality. Only for this reason, mousefeed needs to
        // perform a direct call on the BindingManager now.
        if (bindingService instanceof BindingService) {
            final BindingService theBindingService = (BindingService) bindingService;
            final Map matches = theBindingService.getBindingManager()
                    .getPartialMatches(keySequence);

            final Class<? extends IAction> actionClass = action.getClass();
            for (Object o : matches.keySet()) {
                final TriggerSequence triggerSequence = (TriggerSequence) o;
                final Binding binding = (Binding) matches.get(triggerSequence);
                final Command command = binding.getParameterizedCommand()
                        .getCommand();
                final IHandler handler = getCommandHandler(command);
                if (!(handler instanceof ActionHandler)) {
                    continue;
                }

                if (isCommandEnabled(command)) {
                    final String accelerator = getFromBindingData(action,
                            actionClass, triggerSequence, handler);
                    if (accelerator != null) {
                        return accelerator;
                    }
                }
            }
        }
        return null;
    }

    // RETURNCOUNT:OFF
    // clear, simple structure, leave extra returns
    /**
     * Gets accelerator for the bindings data.
     * 
     * @param action
     *            the action. Assumed not null.
     * @param actionClass
     *            effective action class. Assumed not null.
     * @param triggerSequence
     *            the binding trigger sequence. Assumed not null.
     * @param handler
     *            the action handler. Assumed not null.
     * @return the accelerator if found, <code>null</code> otherwise.
     */
    private String getFromBindingData(final IAction action,
            final Class<? extends IAction> actionClass,
            final TriggerSequence triggerSequence, final IHandler handler) {
        final ActionHandler actionHandler = (ActionHandler) handler;
        final IAction boundAction = actionHandler.getAction();
        if (boundAction == null) {
            return null;
        }
        if (boundAction.getClass().equals(actionClass)) {
            return triggerSequence.toString();
        }
        if (!(boundAction instanceof RetargetAction)) {
            return null;
        }
        final IAction searchTarget = ((RetargetAction) boundAction)
                .getActionHandler();
        if (searchTarget == null) {
            return null;
        }
        if (searchTarget.getClass().equals(actionClass)) {
            return triggerSequence.toString();
        }
        if (actionSearcher.isSearchable(searchTarget)) {
            final String id = actionSearcher.findActionDefinitionId(action,
                    searchTarget);
            return findAcceleratorForActionDefinition(id);
        }
        return null;
    }

    // RETURNCOUNT:ON

    /**
     * Converts the provided key combination code to accelerator.
     * 
     * @param accelerator
     *            The accelerator to convert; should be a valid SWT accelerator
     *            value.
     * @return The equivalent key stroke; never <code>null</code>.
     */
    private String keyToStr(final int accelerator) {
        return SWTKeySupport.convertAcceleratorToKeyStroke(accelerator)
                .format();
    }

    /**
     * Current workbench. Not <code>null</code>.
     */
    private IWorkbench getWorkbench() {
        return PlatformUI.getWorkbench();
    }

    /**
     * Retrieves command handler from a command.
     * 
     * @param command
     *            the command to retrieve the handler from. Not
     *            <code>null</code>.
     * @return the handler. Returns <code>null</code>, if can't retrieve a
     *         handler.
     */
    private IHandler getCommandHandler(final Command command) {
        try {
            final Method method = Command.class.getDeclaredMethod("getHandler");
            method.setAccessible(true);
            return (IHandler) method.invoke(command);
        } catch (final SecurityException e) {
            // want to know when this happens
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            // should never happen
            throw new AssertionError(e);
        } catch (final IllegalAccessException e) {
            // should never happen
            throw new AssertionError(e);
        } catch (final InvocationTargetException e) {
            // should never happen
            throw new AssertionError(e);
        }
    }

    /**
     * Returns <code>true</code> if the command is defined and is enabled.
     * 
     * @param command
     *            the command to check. Not <code>null</code>.
     */
    private boolean isCommandEnabled(final Command command) {
        return command.isDefined()
                && activityManager.getIdentifier(command.getId()).isEnabled();
    }
}
// COUPLING:ON

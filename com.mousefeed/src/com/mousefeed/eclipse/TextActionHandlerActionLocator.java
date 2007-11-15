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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.action.IAction;

/**
 * Searches for an action inside of
 * <code>org.eclipse.ui.actions.TextActionHandler</code> utility actions.
 * If an action is found, provides action definition id, for the global
 * action belonging to the same command.
 *
 * @author Andriy Palamarchuk
 */
public class TextActionHandlerActionLocator {

    /**
     * Name of class <code>org.eclipse.ui.actions.TextActionHandler</code>.
     */
    private static final String HANDLER_CLASS_NAME =
            "org.eclipse.ui.actions.TextActionHandler";

    /**
     * Action fields mapped to an action definition id.
     * Key - name of the field in
     * <code>org.eclipse.ui.actions.TextActionHandler</code>, storing an
     * action, value - the action definitions id for this action. 
     */
    private static final Map<String, String> HANDLER_ACTIONS;
    static {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("copyAction", "org.eclipse.ui.edit.copy");
        HANDLER_ACTIONS = Collections.unmodifiableMap(map);
    }

    /**
     * Indicates whether this class can search for an action inside
     * of the search target.
     * @param searchTarget where to search. Not <code>null</code>.
     * @return <code>true</code> if this class can search for an action inside
     * of the search target.
     */
    public boolean isSearchable(IAction searchTarget) {
        notNull(searchTarget);
        return searchTarget.getClass().getName().startsWith(
                HANDLER_CLASS_NAME + "$");
    }
    
    /**
     * Finds an action inside of <code>searchTarget</code>.
     * @param action the action to search. Not <code>null</code>.
     * @param searchTarget where to search. Not <code>null</code>.
     * @return the action definition id for the global action for the same
     * command as <code>action</code>, if it 
     */
    public String findActionDefinitionId(IAction action, IAction searchTarget) {
        try {
            return doFindActionDefinitionId(action, searchTarget);
        } catch (SecurityException e) {
            throw new AssertionError(e);
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Contains all the logic of
     * {@link #findActionDefinitionId(IAction, IAction)}, leaving to it only
     * exception handling.
     */
    private String doFindActionDefinitionId(IAction action,
            IAction searchTarget)
            throws NoSuchFieldException, IllegalAccessException {
        notNull(action);
        notNull(searchTarget);

        final Object handler = getTextActionHandler(searchTarget);
        for (final String fieldName : HANDLER_ACTIONS.keySet()) {
            final IAction handlerAction =
                    getActionFromField(handler, fieldName);
            if (handlerAction == null) {
                continue;
            }
            if (action.getClass().equals(
                    handlerAction.getClass())) {
                return HANDLER_ACTIONS.get(fieldName);
            }
        }
        return null;
    }

    /**
     * Retrieves an action from the provided action field.
     */
    private IAction getActionFromField(Object o, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        final Field actionField =
                o.getClass().getDeclaredField(fieldName);
        actionField.setAccessible(true);
        return (IAction) actionField.get(o);
    }

    /**
     * Retrieves <code>org.eclipse.ui.actions.TextActionHandler</code> from the
     * nested class action.
     * @param action the action to retrieve handler from. Not <code>null</code>.
     * @return the handler. Never <code>null</code>.
     */
    private Object getTextActionHandler(IAction action)
            throws NoSuchFieldException, IllegalAccessException {
        notNull(action);
        final Field handlerField =
                action.getClass().getDeclaredField("this$0");
        notNull(handlerField);
        handlerField.setAccessible(true);

        final Object handler = handlerField.get(action);
        notNull(handler);
        isTrue(handler.getClass().getName().equals(HANDLER_CLASS_NAME));
        return handler;
    }
}

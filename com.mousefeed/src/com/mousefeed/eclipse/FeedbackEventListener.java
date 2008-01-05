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

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;
import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.client.collector.ActionEvent;
import com.mousefeed.client.collector.IEventListener;
import com.mousefeed.eclipse.preferences.PreferenceAccessor;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

/**
 * Depending on the settings shows the user reminder about the action,
 * cancels the action.
 *
 * @author Andriy Palamarchuk
 */
public class FeedbackEventListener implements IEventListener {
    /**
     * Provides access to the plugin preferences.
     */
    private final PreferenceAccessor preferences =
            PreferenceAccessor.getInstance();

    // see base
    public void event(com.mousefeed.client.collector.Event event) {
        notNull(event);
        final ActionEvent actionEvent = (ActionEvent) event;
        isTrue(StringUtils.isNotBlank(actionEvent.getLabel()));

        if (!actionEvent.hasAccelerator()) {
            return;
        }
        if (!preferences.isInvocationControlEnabled()) {
            return;
        }

        switch (getOnWrongInvocationMode(actionEvent.getId())) {
        case DO_NOTHING:
            // go on
            break;
        case REMIND:
            new NagPopUp(actionEvent.getLabel(),
                    actionEvent.getAccelerator(), false).open();
            break;
        case ENFORCE:
            cancelEvent((Event) actionEvent.getClientEvent());
            new NagPopUp(actionEvent.getLabel(),
                    actionEvent.getAccelerator() + "", true).open();
            break;
        default:
            throw new AssertionError();
        }
    }

    /**
     * Returns the wrong invocation mode handling for the action with the
     * specified id.
     * @param id the action id. Assumed not <code>null</code>.
     * @return the mode. Not <code>null</code>.
     */
    private OnWrongInvocationMode getOnWrongInvocationMode(String id) {
        final OnWrongInvocationMode mode =
                preferences.getOnWrongInvocationMode(id);
        return mode == null ? preferences.getOnWrongInvocationMode() : mode;
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

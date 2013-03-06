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

import java.lang.reflect.Field;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.workbench.renderers.swt.HandledContributionItem;

/**
 * Retrieves {@link Command} from {@link HandledContributionItem}.
 * 
 * @author Robert Wloch (robert@rowlo.de)
 */
@SuppressWarnings("restriction")
public class HandledContributionItemCommandLocator {
    /**
     * Name of the field storing model in the {@link HandledContributionItem}
     * object.
     */
    private static final String MODEL_FIELD = "model";

    /**
     * Default constructor does nothing.
     */
    public HandledContributionItemCommandLocator() {
    }

    /**
     * Performs the actual retrieval.
     * 
     * @param item
     *            the handled contribution item to retrieve command from.
     * @return the command stored in the handled contribution item. Can be
     *         <code>null</code> if the command can't be found.
     */
    public Command get(final HandledContributionItem item) {
        final ParameterizedCommand parCommand = getItemParCommand(item);
        return parCommand == null ? null : parCommand.getCommand();
    }

    /**
     * Parameterized command of the handled contribution item.
     * 
     * @param item
     *            the item to extract the command from.
     * @return the command or <code>null</code> if can't be found.
     */
    private ParameterizedCommand getItemParCommand(
            final HandledContributionItem item) {
        // the ParameterizedCommand is stored in the field
        // HandledContributionItem.model.wbCommand
        final Field modelField = getModelField();
        try {
            modelField.setAccessible(true);
            final MHandledItem mItem = (MHandledItem) modelField.get(item);
            return mItem.getWbCommand();
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The model field of the handled contribution item class.
     * 
     * @return the model field. Never <code>null</code>.
     */
    private Field getModelField() {
        try {
            final Field modelField = HandledContributionItem.class
                    .getDeclaredField(MODEL_FIELD);
            notNull(modelField);
            return modelField;
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

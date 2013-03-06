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
import org.eclipse.ui.menus.CommandContributionItem;

/**
 * Retrieves {@link Command} from {@link CommandContributionItem}.
 *
 * @author Andriy Palamarchuk
 */
public class CommandContributionItemCommandLocator {
    /**
     * Name of the field storing command in the {@link CommandContributionItem}
     * object. 
     */
    private static final String COMMAND_FIELD = "command";

    /**
     * Default constructor does nothing.
     */
    public CommandContributionItemCommandLocator() {
    }
    
    /**
     * Performs the actual retrieval.
     * @param item the command contribution item to retrieve command from.
     * @return the command stored in the command contribution item.
     * Can be <code>null</code> if the command can't be found. 
     */
    public Command get(final CommandContributionItem item) {
        final ParameterizedCommand parCommand = getItemParCommand(item);
        return parCommand == null ? null : parCommand.getCommand();
    }

    /**
     * Parameterized command of the command contribution item.
     * @param item the item to extract the command from.
     * @return the command or <code>null</code> if can't be found.
     */
    private ParameterizedCommand getItemParCommand(
            final CommandContributionItem item) {
        final Field commandField = getCommandField();
        try {
            commandField.setAccessible(true);
            return (ParameterizedCommand) commandField.get(item);
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The command field of the command contribution item class.
     * @return the command field. Never <code>null</code>.
     */
    private Field getCommandField() {
        try {
            final Field commandField =
                    CommandContributionItem.class.getDeclaredField(
                            COMMAND_FIELD);
            notNull(commandField);
            return commandField;
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

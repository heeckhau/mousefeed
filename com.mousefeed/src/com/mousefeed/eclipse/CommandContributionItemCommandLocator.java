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
     * Performs the actual retrieval.
     * @param item the command contribution item to retrieve command from.
     * @return the command stored in the command contribution item.
     * Can be <code>null</code> if the command can't be found. 
     */
    public Command get(CommandContributionItem item) {
        final ParameterizedCommand parCommand = getItemParCommand(item);
        return parCommand == null ? null : parCommand.getCommand();
    }

    /**
     * Parameterized command of the command contribution item.
     * @param item the item to extract the command from.
     * @return the command or <code>null</code> if can't be found.
     */
    private ParameterizedCommand getItemParCommand(
            CommandContributionItem item) {
        final Field commandField = getCommandField();
        try {
            commandField.setAccessible(true);
            return (ParameterizedCommand) commandField.get(item);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

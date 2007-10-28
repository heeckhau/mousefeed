/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.client;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides localized messages.
 * Messages are retrieved with {@link #get(String)}.
 * If it is be provided with a class to retrieve the messages for, one can drop
 * this class name from the key names.  
 *
 * @see #get(String)
 * @author Andriy Palamarchuk
 */
public class Messages {
    /**
     * The base name of the messages properties file for {@link #BUNDLE}.
     */
    private static final String MESSAGES_BASE =
            Messages.class.getPackage().getName() + ".messages";

    /**
     * The messages resource bundle. 
     */
    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle(MESSAGES_BASE);
    
    /**
     * The class to find messages for by using keys missing class name.
     */
    private final Class<?> forClass;

    /**
     * Constructor. For an object created by this constructor it is necessary
     * to pass full message key to {@link #get(String)}.
     * @see #Messages(Class)
     */
    public Messages() {
        this(NoForClassIsSpecified.class);
    }

    /**
     * Constructor. For an object created by this constructor the messages
     * associated with the specified class can be retrieved by passing a key
     * part after class base name as well as full message key to
     * {@link #get(String)}.
     * @param forClass the class to retrieve messages for.
     * Passing <code>null</code> value is equivalent to {@link #Messages()}.
     * @see #get(String)
     */
    public Messages(Class<?> forClass) {
        this.forClass =
                forClass == null ? NoForClassIsSpecified.class : forClass;
    }
    
    /**
     * Returns the message for the defined key.
     * If the object is provided with a class to retrieve messages for
     * (e.g. is created with {@link #Messages(Class)}),
     * this method tries to retrieve the message on a key composed with that
     * class name and the provided key. If the message is not found, it finds
     * the message using the provided key as is.
     * If no class is provided, the method returns searches the message using
     * the provided key.
     * @param key the message id.
     * Not <code>null</code> or empty string.
     * @param arguments the message arguments when the message will be 
     * formatted by {@link MessageFormat#format}.
     * @return the message for the defined key. Never <code>null</code>.
     * @throws NullPointerException if the provided key is blank.
     * @throws MissingResourceException if the provided key is not found. 
     */
    public String get(String key, Object... arguments)
            throws NullPointerException, MissingResourceException {
        if (isBlank(key)) {
            throw new NullPointerException(
                    "Blank key was provided: '" + key + "'");
        }
        try {
            final String forClassKey = forClass.getSimpleName() + "." + key;
            return getFromBundle(forClassKey, arguments);
        } catch (MissingResourceException e) {
            return getFromBundle(key, arguments);
        }
    }

    /**
     * Retrieves the string specified by the key from bundle.
     * @param key the message key. Not blank.
     * @return the message by the key.
     * The message is formatted with {@link MessageFormat#format(Object)} if
     * any arguments are provided.
     */
    private String getFromBundle(String key, Object... arguments) {
        final String pattern = BUNDLE.getString(key);
        return arguments.length == 0
                ? pattern : MessageFormat.format(pattern, arguments);
    }

    /**
     * A "null" class used to initialize {@link Messages#forClass} if no class
     * is specified. 
     */
    private abstract static class NoForClassIsSpecified { }
}

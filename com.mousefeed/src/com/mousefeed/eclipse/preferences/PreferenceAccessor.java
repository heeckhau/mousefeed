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
package com.mousefeed.eclipse.preferences;

import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_DEFAULT_ON_WRONG_INVOCATION_MODE;
import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_INVOCATION_CONTROL_ENABLED;
import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_CONFIGURE_KEYBOARD_SHORTCUT_ENABLED;
import static com.mousefeed.eclipse.preferences.PreferenceConstants.P_CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.Activator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

/**
 * Provides access to the plugin preferences.
 * Singleton.
 * 
 * @author Andriy Palamarchuk
 * @author Robert Wloch
 */
public class PreferenceAccessor {
    /**
     * Name of the file to store action-specific behavior when the actions
     * are invoked wrong way.
     */
    static final String ACTIONS_WRONG_INVOCATION_MODE_FILE =
            "actionsWrongInvocationMode.xml";
    
    /**
     * The singleton instance.
     */
    private static final PreferenceAccessor INSTANCE = new PreferenceAccessor();
    
    /**
     * The root document for the action-specific wrong invocation mode
     * preferences.
     */
    private static final String TAG_ACTIONS_WRONG_INVOCATION_MODE =
            "actionsWrongInvocationMode";

    /**
     * The action tag name.
     */
    private static final String TAG_ACTION = "action";

    /**
     * The action id tag name.
     */
    private static final String TAG_ACTION_ID = "id";
    
    /**
     * The action label tag name.
     */
    private static final String TAG_ACTION_LABEL = "label";
    
    /**
     * The on wrong invocation mode handling approach.
     */
    private static final String TAG_ON_WRONG_INVOCATION_MODE =
            "onWrongInvocationMode";

    /**
     * Actions on wrong invocation mode settings. Keys - action ids, values -
     * the settings.   
     */
    private final Map<String, ActionOnWrongInvocationMode> actionsOnWrongMode =
            new HashMap<String, ActionOnWrongInvocationMode>();
    
    /**
     * Creates new preference accessor.
     * Because this class is a singleton, the constructor normally should not be
     * used. It is exposed for testing purposes only.
     */
    PreferenceAccessor() {
        loadActionsOnWrongInvocationMode();
    }
    
    /**
     * The singleton instance.
     * @return the singleton instance. Never <code>null</code>.
     */
    public static PreferenceAccessor getInstance() {
        return INSTANCE;
    }
    
    /**
     * Whether invocation control is enabled preference.
     * The preference indicates whether to help to learn the desired way to
     * invoke actions
     * @return current preference value whether invocation control is enabled.
     */
    public boolean isInvocationControlEnabled() {
        return getPreferenceStore().getBoolean(
                P_INVOCATION_CONTROL_ENABLED);
    }

    /**
     * @param invocationControlEnabled the new value for the setting returned by
     * {@link #isInvocationControlEnabled()}.
     * @see #isInvocationControlEnabled()
     */
    public void storeInvocationControlEnabled(
            final boolean invocationControlEnabled) {
        getPreferenceStore().setValue(
                P_INVOCATION_CONTROL_ENABLED, invocationControlEnabled);
    }
    
    /**
     * Whether keyboard shortcut configuration is enabled preference.
     * The preference indicates whether to show the Keys preference page
     * for often used actions without a shortcut.
     * @return current preference value whether keyboard shortcut configuration
     * is enabled.
     */
    public boolean isConfigureKeyboardShortcutEnabled() {
        return getPreferenceStore().getBoolean(
                P_CONFIGURE_KEYBOARD_SHORTCUT_ENABLED);
    }

    /**
     * @param configureKeyboardShortcutEnabled the new value for the setting returned by
     * {@link #isConfigureKeyboardShortcutEnabled()}.
     * @see #isConfigureKeyboardShortcutEnabled()
     */
    public void storeConfigureKeyboardShortcutEnabled(
            final boolean configureKeyboardShortcutEnabled) {
        getPreferenceStore().setValue(
                P_CONFIGURE_KEYBOARD_SHORTCUT_ENABLED, configureKeyboardShortcutEnabled);
    }
    
    /**
     * The default preference of the threshold for the action invocation counter
     * above which keyboard shortcut configuration is enabled.
     * @return the global configure keyboard shortcut threshold preference.
     * @see PreferenceConstants#P_CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD
     */
    public int getConfigureKeyboardShortcutThreshold() {
        return getPreferenceStore().getInt(
                P_CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD);
    }

    /**
     * @param configureKeyboardShortcutThreshold the new value for the setting returned by
     * {@link #getConfigureKeyboardShortcutThreshold()}.
     * @see #getConfigureKeyboardShortcutThreshold()
     */
    public void storeConfigureKeyboardShortcutThreshold(
            final int configureKeyboardShortcutThreshold) {
        getPreferenceStore().setValue(
                P_CONFIGURE_KEYBOARD_SHORTCUT_THRESHOLD, configureKeyboardShortcutThreshold);
    }
    
    /**
     * The default preference what to do by default on wrong invocation mode.
     * @return the global invocation mode preference. Never <code>null</code>.
     * @see PreferenceConstants#P_DEFAULT_ON_WRONG_INVOCATION_MODE
     */
    public OnWrongInvocationMode getOnWrongInvocationMode() {
        final String stored = getPreferenceStore().getString(
                P_DEFAULT_ON_WRONG_INVOCATION_MODE);
        return stored == null
                ? OnWrongInvocationMode.DEFAULT
                : OnWrongInvocationMode.valueOf(stored);
    }
    
    /**
     * The preference what to do on wrong invocation mode for the specified
     * action.
     * @param actionId the id of the action get preferences for.
     * Not <code>null</code>.
     * @return the invocation mode preference.
     * <code>null</code> if there is no action-specific setting.
     * In this case use the default preference value.
     */
    public OnWrongInvocationMode getOnWrongInvocationMode(final String actionId) {
        notNull(actionId);
        final ActionOnWrongInvocationMode mode =
                actionsOnWrongMode.get(actionId);
        return mode == null ? null : mode.getOnWrongInvocationMode();
    }
    
    /**
     * Saves action-specific on wrong invocation mode settings.
     * @param settings the new value. Not <code>null</code>.
     */
    public void setOnWrongInvocationMode(
            final ActionOnWrongInvocationMode settings) {
        notNull(settings);
        actionsOnWrongMode.put(settings.getId(), settings);
        saveActionsOnWrongInvocationMode();
    }
    
    /**
     * Returns action-specific wrong invocation mode handling for all actions.
     * @return the action-specific settings. Read-only.
     * The collection nor its elements should not be changed.
     * Note, the collection and the objects in it should be cloned for using
     * for any significant period of time. 
     * Never <code>null</code>, can be empty if no action-specific settings were
     * defined.
     * All objects in the collection have unique ids.
     */
    public Collection<ActionOnWrongInvocationMode>
            getActionsOnWrongInvocationMode() {
        return actionsOnWrongMode.values();
    }

    /**
     * Replaces the action-specific settings.
     * @param settings the new settings. Not <code>null</code>.
     * Changes to the data passed to this method won't affect the values stored
     * in this class.
     * @see #getActionsOnWrongInvocationMode()
     */
    public void setActionsOnWrongInvocationMode(
            final Collection<ActionOnWrongInvocationMode> settings) {
        this.actionsOnWrongMode.clear();
        for (ActionOnWrongInvocationMode mode : settings) {
            final ActionOnWrongInvocationMode clone;
            try {
                clone = (ActionOnWrongInvocationMode) mode.clone();
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException();
            }
            this.actionsOnWrongMode.put(clone.getId(), clone);
        }
        saveActionsOnWrongInvocationMode();
    }

    /**
     * Removes action-specific on wrong invocation mode setting.
     * After calling this method when the action with the specified id is
     * handled using the default settings. 
     * @param actionId the action id. not <code>null</code>.
     */
    public void removeOnWrongInvocaitonMode(final String actionId) {
        notNull(actionId);
        actionsOnWrongMode.remove(actionId);
        saveActionsOnWrongInvocationMode();
    }

    /**
     * Stores the provided preference.
     * @param value the new value. Not <code>null</code>.
     */
    public void storeOnWrongInvocationMode(final OnWrongInvocationMode value) {
        notNull(value);
        getPreferenceStore().setValue(
                P_DEFAULT_ON_WRONG_INVOCATION_MODE, value.name());
    }
    
    /**
     * Loads preferences for the {@link #getOnWrongInvocationMode(String)}.
     */
    private void loadActionsOnWrongInvocationMode() {
        final File file = getActionsWrongInvocationModeFile();
        if (!file.exists() || file.length() == 0) {
            // the file not initialized yet
            return;
        }
        Reader reader = null;
        try {
            reader = new FileReader(file);
            final XMLMemento memento = XMLMemento.createReadRoot(reader);
            loadActionsOnWrongInvocationMode(memento);
        } catch (final FileNotFoundException ignore) {
            // the file does not exist yet 
        } catch (final WorkbenchException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Loads action-specific invocation handling.
     * @param memento the memento to load the data from.
     * Assumed not <code>null</code>.
     * @see #loadActionsOnWrongInvocationMode()
     */
    private void loadActionsOnWrongInvocationMode(final XMLMemento memento) {
        actionsOnWrongMode.clear();
        final IMemento[] children = memento.getChildren(TAG_ACTION);
        for (IMemento child : children) {
            final ActionOnWrongInvocationMode mode =
                    new ActionOnWrongInvocationMode();
            mode.setLabel(child.getString(TAG_ACTION_LABEL));
            mode.setId(child.getString(TAG_ACTION_ID));
            mode.setOnWrongInvocationMode(
                    OnWrongInvocationMode.valueOf(child.getString(
                            TAG_ON_WRONG_INVOCATION_MODE)));
            actionsOnWrongMode.put(mode.getId(), mode); 
        }
    }

    /**
     * Saves settings loaded by {@link #loadActionsOnWrongInvocationMode()}.
     */
    private void saveActionsOnWrongInvocationMode() {
        final XMLMemento memento = createActionsOnWrongInvocationModeMemento();
        Writer writer = null;
        try {
            writer = new FileWriter(getActionsWrongInvocationModeFile());
            memento.save(writer);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Generates XML memento with the actions wrong invocation mode data. 
     * @return the memento. Never <code>null</code>.
     */
    private XMLMemento createActionsOnWrongInvocationModeMemento() {
        final XMLMemento memento = XMLMemento.createWriteRoot(
                TAG_ACTIONS_WRONG_INVOCATION_MODE);
        for (ActionOnWrongInvocationMode val : actionsOnWrongMode.values()) {
            final IMemento actionMemento = memento.createChild(TAG_ACTION);
            actionMemento.putString(TAG_ACTION_ID, val.getId());
            actionMemento.putString(TAG_ACTION_LABEL, val.getLabel());
            actionMemento.putString(TAG_ON_WRONG_INVOCATION_MODE,
                    val.getOnWrongInvocationMode().name());
        }
        return memento;
    }

    /**
     * The plugin preference store.
     * @return never <code>null</code>.
     */
    private IPreferenceStore getPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }
    
    /**
     * File storing action-specific preferences for action invocation mode.
     * @return the file for action-specific preferences when the actions are
     * invoked with a wrong invocation mode.
     */
    File getActionsWrongInvocationModeFile() {
        if (Activator.getDefault() == null) {
            return new File("nonexisting");
        }
        return Activator.getDefault()
                .getStateLocation()
                .append(ACTIONS_WRONG_INVOCATION_MODE_FILE)
                .toFile();
    }
}

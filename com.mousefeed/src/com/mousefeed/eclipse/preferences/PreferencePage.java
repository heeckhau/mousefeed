/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.eclipse.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mousefeed.client.Messages;
import com.mousefeed.client.OnWrongAccessMode;
import com.mousefeed.eclipse.Activator;

/**
 * Main MouseFeed preferences page.
 *
 * @author Andriy Palamarchuk
 */
public class PreferencePage
    extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage {
    
    /**
     * Provides messages text.
     */
    private static final Messages MESSAGES = new Messages(PreferencePage.class);

    /**
     * Creates new preference page.
     */
    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(MESSAGES.get("description"));
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
    @Override
    public void createFieldEditors() {
       
        addField(new ComboFieldEditor(
                PreferenceConstants.P_DEFAULT_ON_WRONG_ACCESS_MODE,
                MESSAGES.get("field.defaultOnWrongAccessMode.label"),
                OnWrongAccessMode.getLabelsAndNames(),
                getFieldEditorParent()));
    }

    /**
     * Does not do anything.
     * @param workbench not used.
     */
    public void init(@SuppressWarnings("unused") IWorkbench workbench) {}
}
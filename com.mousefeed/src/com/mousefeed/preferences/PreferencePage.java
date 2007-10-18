/*
 * Copyright (C) Heavy Lifting Software 2007.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html. If redistributing this code,
 * this entire header must remain intact.
 */
package com.mousefeed.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.mousefeed.Activator;

/**
 * Main MouseFeed preferences page.
 */

public class PreferencePage
    extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage {

    /**
     * Creates new preference page.
     */
    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("MouseFeed preferences");
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
    @Override
   public void createFieldEditors() {
       
       addField(new BooleanFieldEditor(
             PreferenceConstants.P_PROMOTE_KEYS,
             "Promote &keys",
             getFieldEditorParent()));
    }

    /**
     * Does not do anything.
     * @param workbench not used.
     */
    public void init(@SuppressWarnings("unused") IWorkbench workbench) {}
    
}
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

import static com.mousefeed.eclipse.Layout.FULL_WIDTH;
import static com.mousefeed.eclipse.Layout.STACKED_LABEL_V_OFFSET;
import static com.mousefeed.eclipse.Layout.STACKED_V_OFFSET;
import static com.mousefeed.eclipse.Layout.WINDOW_MARGIN;
import static com.mousefeed.eclipse.Layout.placeUnder;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.Messages;
import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.Activator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Provides control over how actions are invoked.
 * Contains global as well as action-specific settings.
 * 
 * @author Andriy Palamarchuk
 */
public class ActionInvocationPreferencePage extends PreferencePage
        implements IWorkbenchPreferencePage {
    /*
     * Does not use field editors because field editors work for grid layout
     * only, but this page uses form layout. 
     */
    
    /**
     * Provides messages text.
     */
    private static final Messages MESSAGES =
        new Messages(ActionInvocationPreferencePage.class);
    
    
    /**
     * Provides access to the plugin preferences.
     */
    private final PreferenceAccessor preferences = new PreferenceAccessor();

    /**
     * Setting what to do when user invokes an action using wrong invocation
     * mode.
     */
    private Combo onWrongInvocationModeCombo;

    /**
     * Constructor.
     */
    public ActionInvocationPreferencePage() {
        super();
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(MESSAGES.get("description"));
    }

    // see base
    protected Control createContents(Composite parent) {
        initializeDialogUnits(parent);
        
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FormLayout());
        
        Control c;
        c = createOnWrongInvocationModeLabel(composite, null);
        onWrongInvocationModeCombo =
                createOnWrongInvocationModeCombo(composite, c);
        updateOnWrongInvocationModeCombo(
                preferences.getOnWrongInvocationMode());

//        c = onWrongInvocationModeCombo;

        Dialog.applyDialogFont(composite);

        // set F1 help
        // PlatformUI.getWorkbench().getHelpSystem().setHelp(
        // getControl(), IHelpContextIds.FILE_TYPE_PREFERENCE_PAGE);

        return composite;
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        updateOnWrongInvocationModeCombo(OnWrongInvocationMode.DEFAULT);
    }

    @Override
    public boolean performOk() {
        preferences.storeOnWrongInvocationMode(
                getSelectedOnWrongInvocationMode());
        return super.performOk();
    }

    /**
     * Does not do anything.
     * @param workbench not used.
     */
    public void init(@SuppressWarnings("unused") IWorkbench workbench) {}

    /**
     * Creates a label for the combo.
     */
    private Control createOnWrongInvocationModeLabel(
            Composite container, Control above) {
        notNull(container);
        final Label label = new Label(container, SWT.NULL);
        label.setText(MESSAGES.get("field.defaultOnWrongInvocationMode.label"));
        placeUnder(label, above, STACKED_V_OFFSET);
        return label;
    }

    /**
     * Creates the combo {@link #onWrongInvocationModeCombo}.
     */
    private Combo createOnWrongInvocationModeCombo(
            Composite container, Control aboveControl) {
        notNull(container);
        isTrue(aboveControl == null || aboveControl instanceof Label);

        final Combo combo = new Combo(container, SWT.READ_ONLY);
        for (OnWrongInvocationMode mode : OnWrongInvocationMode.values()) {
            combo.add(mode.getLabel());
        }
        
        placeUnder(combo, aboveControl, STACKED_LABEL_V_OFFSET);
        final FormData formData = (FormData) combo.getLayoutData();
        formData.right = new FormAttachment(FULL_WIDTH, -WINDOW_MARGIN);
        return combo;
    }

    /**
     * The currently selected wrong invocation mode handling.
     * @return the wrong invocation mode handling. Never <code>null</code>.
     */
    private OnWrongInvocationMode getSelectedOnWrongInvocationMode() {
        final int i = onWrongInvocationModeCombo.getSelectionIndex();
        return OnWrongInvocationMode.values()[i];
    }

    /**
     * Set the name in the combo widget to the specified value.
     * @param mode the value to set the combo to. Not <code>null</code>.
     */
    private void updateOnWrongInvocationModeCombo(
            OnWrongInvocationMode mode) {
        notNull(mode);
        onWrongInvocationModeCombo.setText(mode.getLabel());
    }

}

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
package com.mousefeed.eclipse.preferences;

import static com.mousefeed.eclipse.Layout.STACKED_V_OFFSET;
import static com.mousefeed.eclipse.Layout.placeUnder;
import static com.mousefeed.eclipse.preferences.PreferenceConstants.INVOCATION_CONTROL_ENABLED_DEFAULT;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.Messages;
import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.Activator;
import com.mousefeed.eclipse.commands.OnWrongInvocationModeUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
     * Setting whether to control action invocation at all.
     * If turned off, no other options on this page are used.
     */
    private Button invocationControlEnabledCheckbox;

    /**
     * Setting what to do when user invokes an action using wrong invocation
     * mode.
     */
    private Combo onWrongInvocationModeCombo;

    
    /**
     * The UI factory class.
     */
    private final OnWrongInvocationModeUI onWrongInvocationModeUI =
            new OnWrongInvocationModeUI();

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
        invocationControlEnabledCheckbox =
                createInvocationControlEnabledCheckbox(composite, null);
        ((FormData) invocationControlEnabledCheckbox.getLayoutData()).top =
                new FormAttachment(0);
        c = invocationControlEnabledCheckbox;

        c = onWrongInvocationModeUI.createLabel(composite, c,
                MESSAGES.get("field.defaultOnWrongInvocationMode.label"));
        onWrongInvocationModeCombo =
                onWrongInvocationModeUI.createCombo(composite, c);
        updateOnWrongInvocationModeCombo(
                preferences.getOnWrongInvocationMode());
        updateInvocationControlEnabled(
                preferences.isInvocationControlEnabled());

        onInvocationControlEnabledCheckboxSelected();
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
        updateInvocationControlEnabled(
                INVOCATION_CONTROL_ENABLED_DEFAULT);
    }

    @Override
    public boolean performOk() {
        preferences.storeOnWrongInvocationMode(
                getSelectedOnWrongInvocationMode());
        preferences.storeInvocationControlEnabled(isInvocationControlEnabled());
        return super.performOk();
    }

    /**
     * Does not do anything.
     * @param workbench not used.
     */
    public void init(@SuppressWarnings("unused") IWorkbench workbench) {}

    /**
     * Creates the control for {@link #invocationControlEnabledCheckbox}.
     */
    private Button createInvocationControlEnabledCheckbox(
            Composite container, Control above) {
        notNull(container);
        
        final Button checkbox = new Button(container, SWT.CHECK | SWT.LEAD);
        checkbox.setText(
                MESSAGES.get("field.invocationControlEnabledCheckbox.label"));
        checkbox.setToolTipText(
                MESSAGES.get("field.invocationControlEnabledCheckbox.tooltip"));
        placeUnder(checkbox, above, STACKED_V_OFFSET);
        checkbox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onInvocationControlEnabledCheckboxSelected();
            }
        });
        return checkbox;
    }

    /**
     * Is called when the {@link #invocationControlEnabledCheckbox} selection
     * changed.
     */
    private void onInvocationControlEnabledCheckboxSelected() {
        final Button checkbox = invocationControlEnabledCheckbox;
        for (Control c : checkbox.getParent().getChildren()) {
            if (!c.equals(invocationControlEnabledCheckbox)) {
                c.setEnabled(isInvocationControlEnabled());
            }
        }
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

    /**
     * The currently selected value of the preference
     * whether action invocation control is enabled.
     * @return the action invocation control preference.
     */
    private boolean isInvocationControlEnabled() {
        return invocationControlEnabledCheckbox.getSelection();
    }
    
    /**
     * Make UI indicate whether to enable action control.
     * @param enabled the value shown by UI. 
     */
    private void updateInvocationControlEnabled(boolean enabled) {
        invocationControlEnabledCheckbox.setSelection(enabled);
    }
}

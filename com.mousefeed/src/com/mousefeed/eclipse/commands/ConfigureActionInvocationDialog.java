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
package com.mousefeed.eclipse.commands;

import static com.mousefeed.eclipse.Layout.STACKED_V_OFFSET;
import static com.mousefeed.eclipse.Layout.placeUnder;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.Messages;
import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.client.collector.AbstractActionDesc;
import com.mousefeed.eclipse.preferences.ActionOnWrongInvocationMode;
import com.mousefeed.eclipse.preferences.PreferenceAccessor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog to configure action invocation mode.
 * 
 * @author Andriy Palamarchuk
 */
public class ConfigureActionInvocationDialog extends Dialog {
    /**
     * Provides messages text.
     */
    private static final Messages MESSAGES =
            new Messages(ConfigureActionInvocationDialog.class);

    /**
     * The index of the value indicating using of the default on wrong
     * invocation mode handling. 
     */
    private static final int DEFAULT_ON_WRONG_INVOCATION_MODE_IDX = 0;

    /**
     * The action description of the action to configure invocation mode for.
     */
    private final AbstractActionDesc actionDesc;

    /**
     * Setting what to do when user invokes an action using wrong invocation
     * mode.
     */
    private Combo onWrongInvocationModeCombo;

    /**
     * Provides access to the plugin preferences.
     */
    private final PreferenceAccessor preferences =
            PreferenceAccessor.getInstance();
    
    /**
     * The UI factory class.
     */
    private final OnWrongInvocationModeUI onWrongInvocationModeUI =
            new OnWrongInvocationModeUI();

    /**
     * The constructor. Creates the dialog.
     * @param parentShell the parent shell. Not <code>null</code>.
     * @param actionDesc the action description to create the dialog for.
     * Not <code>null</code>. 
     */
    public ConfigureActionInvocationDialog(final Shell parentShell,
            final AbstractActionDesc actionDesc) {
        super(parentShell);
        notNull(parentShell);
        notNull(actionDesc);
        this.actionDesc = actionDesc;
    }

    // see base
    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FormLayout());
        Control c;
        c = createActionNameLabel(composite, null);
        
        c = onWrongInvocationModeUI.createLabel(composite, c,
                MESSAGES.get("field.onWrongInvocationMode.label"));
        onWrongInvocationModeCombo =
                onWrongInvocationModeUI.createCombo(composite, c);
        onWrongInvocationModeCombo.add(getDefaultInvocationMode(), 0);
        updateOnWrongInvocationModeCombo(
                preferences.getOnWrongInvocationMode(actionDesc.getId()));

        applyDialogFont(composite);
        return composite;
    }
    
    // see base
    @Override
    protected void configureShell(final Shell shell) {
        super.configureShell(shell);
        shell.setText(MESSAGES.get("title", actionDesc.getLabel()));
    }

    /**
     * Creates a label for {@link #onWrongInvocationModeCombo}.
     */
    private Control createActionNameLabel(
            final Composite container, final Control above) {
        notNull(container);
        final Label label = new Label(container, SWT.NULL);
        final String text = MESSAGES.get(
                "field.actionName.label", actionDesc.getLabel());
        label.setText(text);
        placeUnder(label, above, STACKED_V_OFFSET);
        return label;
    }

    // see base
    @Override
    protected void okPressed() {
        final OnWrongInvocationMode mode = getSelectedOnWrongInvocationMode();
        if (mode == null) {
            preferences.removeOnWrongInvocaitonMode(actionDesc.getId());
        } else {
            final ActionOnWrongInvocationMode actionMode =
                    new ActionOnWrongInvocationMode(actionDesc);
            actionMode.setOnWrongInvocationMode(
                    getSelectedOnWrongInvocationMode());
            preferences.setOnWrongInvocationMode(actionMode);
        }
        super.okPressed();
    }

    /**
     * Set the name in the combo widget to the specified value.
     * @param mode the value to set the combo to. <code>null</code> means to use
     * global settings.
     */
    private void updateOnWrongInvocationModeCombo(final OnWrongInvocationMode mode) {
        onWrongInvocationModeCombo.setText(mode == null
                ? getDefaultInvocationMode()
                : mode.getLabel());
    }
    
    /**
     * The currently selected wrong invocation mode handling.
     * @return the wrong invocation mode handling. <code>null</code> if the
     * user selected the default handling.
     */
    private OnWrongInvocationMode getSelectedOnWrongInvocationMode() {
        final int i = onWrongInvocationModeCombo.getSelectionIndex();
        return i == DEFAULT_ON_WRONG_INVOCATION_MODE_IDX
                ? null : OnWrongInvocationMode.values()[i - 1];
    }

    /**
     * The text for the {@link OnWrongInvocationMode} value indicating to use
     * invocation handling as defined by preferences.
     * @return the default invocation handling option. Never <code>null</code>.
     */
    private String getDefaultInvocationMode() {
        return MESSAGES.get("field.onWrongInvocationMode.value.default");
    }
}

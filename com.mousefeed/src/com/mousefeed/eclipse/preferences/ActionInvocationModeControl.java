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

import static com.mousefeed.eclipse.Layout.H_OFFSET;
import static com.mousefeed.eclipse.Layout.STACKED_V_OFFSET;
import static com.mousefeed.eclipse.Layout.WHOLE_SIZE;

import com.mousefeed.client.Messages;
import com.mousefeed.client.OnWrongInvocationMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Creates and manages action-specific invocation preferences UI.
 * 
 * @author Andriy Palamarchuk
 */
//COUPLING:OFF
class ActionInvocationModeControl extends Composite {
    /**
     * The columns enumeration. Each member of this enumeration provides
     * property name as name and column order as order of members in the
     * enumeration.
     * 
     * @author Andriy Palamarchuk
     */
    static enum Column {
        /**
         * The label column.
         */
        LABEL,

        /**
         * The on wrong invocation mode handling column.
         */
        MODE
    }

    /**
     * Provides text from the resource.
     */
    private static final Messages MESSAGES =
            new Messages(ActionInvocationModeControl.class);

    /**
     * Provides access to the plugin preferences.
     */
    private final PreferenceAccessor preferences =
            PreferenceAccessor.getInstance();
    
    /**
     * @see #getActionModes()
     */
    private final List<ActionOnWrongInvocationMode> actionModes =
            createActionModes();

    /**
     * Lists actions with their settings.
     */
    private final Table table;
    
    /**
     * The viewer for {@link #table}.
     */
    private final TableViewer tableViewer;
    
    /**
     * The button to remove action-specific settings.
     */
    private final Button removeButton;

    /**
     * Constructor.
     * @param parent the container where to create the UI.
     * Not <code>null</code>.
     */
    public ActionInvocationModeControl(final Composite parent) {
        super(parent, SWT.NONE);
        this.setLayout(new FormLayout());

        table = createTable(null);
        tableViewer = createTableViewer();
        resizeTableColumns();
        table.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.character == SWT.DEL && e.stateMask == 0) {
                    removeSelected();
                    e.doit = false;
                }            
            }
        });
        tableViewer.addSelectionChangedListener(
                new ISelectionChangedListener() {
                    public void selectionChanged(final SelectionChangedEvent event) {
                        onTableSelectionChanged();
                    }
                });
        
        removeButton = createRemoveButton();
        layoutTable(removeButton);
        createAddReminder(removeButton);
        
        onTableSelectionChanged();
    }

    /**
     * Is called when the table selection is changed.
     */
    private void onTableSelectionChanged() {
        removeButton.setEnabled(!tableViewer.getSelection().isEmpty());
    }

    /**
     * Removes the selected settings from {@link #actionModes}.
     */
    private void removeSelected() {
        final IStructuredSelection selection =
            (IStructuredSelection) tableViewer.getSelection();
        final List<?> selectedModes = selection.toList();
        if (selectedModes.isEmpty()) {
            getDisplay().beep();
        } else {
            actionModes.removeAll(selectedModes);
            tableViewer.refresh(false);
        }
    }

    /**
     * Sets {@link #table} column widths. 
     */
    private void resizeTableColumns() {
        for (TableColumn column : table.getColumns()) {
            column.pack();
        }
    }

    /**
     * Creates the table control to initialize {@link #table}.
     * @param aboveControl the control above this one.
     * Can be <code>null</code> if this is the topmost control in the container.
     * @return the table control.
     */
    private Table createTable(final Control aboveControl) {
        final Table t = new Table(this,
                SWT.V_SCROLL | SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        t.setLinesVisible(true);
        t.setHeaderVisible(true);
        
        {
            final TableColumn column = new TableColumn(t, SWT.NONE, 0);
            column.setText(MESSAGES.get("actionTable.column.action"));
        }

        {
            final TableColumn column = new TableColumn(t, SWT.NONE, 1);
            column.setText(
                    MESSAGES.get("actionTable.column.onWrongInvocationMode"));
        }
        return t;
    }

    /**
     * Creates a table viewer to initialize {@link #tableViewer}. 
     * @return the new table viewer. Not <code>null</code>.
     */
    private TableViewer createTableViewer() {
        final TableViewer viewer = new TableViewer(table);
        viewer.setContentProvider(
                new ActionInvocationModeTableContentProvider());
        viewer.setLabelProvider(
                new ActionInvocationModeTableLabelProvider());

        new TableEditor(table);

        viewer.setCellEditors(new CellEditor[] {
                null, createOnWrongInvocationModeCellEditor()});
        viewer.setColumnProperties(
                new String[] {Column.LABEL.name(), Column.MODE.name()});

        viewer.setCellModifier(
                new ActionInvocationModeTableCellModifier(viewer));
        viewer.setInput(actionModes);
        return viewer;
    }

    /**
     * Clones the action invocation control preferences for the table content. 
     * @return the table content. Never <code>null</code>, can be empty.
     * Sorted by label.
     */
    private List<ActionOnWrongInvocationMode> createActionModes() {
        final List<ActionOnWrongInvocationMode> modes =
            new ArrayList<ActionOnWrongInvocationMode>(
                    preferences.getActionsOnWrongInvocationMode());
        Collections.sort(modes,
                new ActionOnWrongInvocationMode.LabelComparator());
        return modes;
    }

    private ComboBoxCellEditor createOnWrongInvocationModeCellEditor() {
        return new ComboBoxCellEditor(table,
                OnWrongInvocationMode.getLabels(),
                SWT.READ_ONLY);
    }

    /**
     * Creates the button to allow user to remove the table entries.
     * @return a new remove button. Not <code>null</code>. 
     */
    private Button createRemoveButton() {
        final Button b = new Button(this, SWT.PUSH);
        final FormData formData = new FormData();
        formData.right = new FormAttachment(WHOLE_SIZE);
        formData.bottom = new FormAttachment(WHOLE_SIZE);
        b.setLayoutData(formData);
        
        b.setText(MESSAGES.get("removeButton.label"));
        b.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                removeSelected();
            }
        });
        return b;
    }

    /**
     * Creates a label with reminder how to add new entries to the table.
     * @param rightControl the control to the right.
     * @return the label control. Not <code>null</code>.
     */
    private Label createAddReminder(final Control rightControl) {
        final Label label = new Label(this, SWT.WRAP | SWT.LEFT);
        label.setText(MESSAGES.get("label.addActionConfigReminder"));

        final FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(rightControl, -H_OFFSET, SWT.LEFT);
        formData.top = new FormAttachment(rightControl, 0, SWT.TOP);
        label.setLayoutData(formData);
        return label;
    }

    /**
     * Positions {@link #table}.
     * @param aboveControl the control above this one. Can be <code>null</code>.
     * @param belowControl the control below this one.
     * Assumed not <code>null</code>.
     */
    private void layoutTable(final Control belowControl) {
        final FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(0, 0);
        formData.right = new FormAttachment(WHOLE_SIZE, 0);
        formData.bottom = new FormAttachment(
                belowControl, -STACKED_V_OFFSET, SWT.TOP);
        table.setLayoutData(formData);
    }

    /**
     * Action-specific invocation mode control settings as specified by user.
     * A clone of the actual preferences, so can be changed.
     * @return the action invocation mode settings. Never <code>null</code>.
     */
    public List<ActionOnWrongInvocationMode> getActionModes() {
        return actionModes;
    }

    /**
     * Removes all action-specific settings.
     */
    public void clearActionSettings() {
        getActionModes().clear();
        tableViewer.refresh();
    }
}
//COUPLING:ON

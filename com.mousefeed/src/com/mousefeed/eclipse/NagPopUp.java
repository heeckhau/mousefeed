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

import static org.apache.commons.lang.time.DateUtils.MILLIS_PER_SECOND;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
/**
 * Pop-up dialog, which notifies a user about wrong mouse/accelerator usage. 
 *
 * @author Andriy Palamarchuk
 */
public class NagPopUp extends PopupDialog {
    /**
     * How close to cursor along X axis the popup will be shown.
     */
    private static final int DISTANCE_TO_CURSOR = 50;

    /**
     * Horizontal indent for the dialog layout.
     */
    private static final int HORIZONTAL_INDENT = 5;

    /**
     * Number of times to increase font size in.
     */
    private static final int FONT_INCREASE_MULT = 2;
    
    /**
     * Time after which the pop up will automatically close itself.
     */
    private static final int CLOSE_TIMEOUT = 4 * (int) MILLIS_PER_SECOND;
    
    /**
     * Timeout, after which listener closes the dialog on any user action.
     * Is necessary to skip events caused by the current user action.
     */
    private static final int CLOSE_LISTENER_TIMEOUT = 250;

    /**
     * @see NagPopUp#NagPopUp(String, String)
     */
    private final String actionName;
    
    /**
     * @see NagPopUp#NagPopUp(String, String)
     */
    private final String accelerator;
    
    /**
     * Is <code>true</code> when the dialog is already open, but not closed yet.
     */
    private boolean open;

    /**
     * Closes the dialog on any outside action, such as click, key press, etc.
     */
    private Listener closeOnActionListener = new Listener() {
        public void handleEvent(@SuppressWarnings("unused") Event event) {
            NagPopUp.this.close();
        }
    };

    /**
     * Creates a pop-up with notification for the specified accelerator
     * and action.
     *
     * @param actionName
     *            the action name. If necessary, the method removes '&'
     *            characters from the action name. Not blank.
     * @param accelerator the string describing the accelerator. Not blank.
     */
    public NagPopUp(String actionName, String accelerator) {
        super((Shell) null, PopupDialog.HOVER_SHELLSTYLE,
                false, false, false, false,
                null, null);
        assert StringUtils.isNotBlank(actionName);
        assert StringUtils.isNotBlank(accelerator);

        this.actionName = actionName.replace("&", "");
        this.accelerator = accelerator;
    }

    /**
     * Creates a control for showing the info.
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        final Text text = new Text(parent, SWT.READ_ONLY | SWT.NO_FOCUS);

        // Use the compact margins employed by PopupDialog.
        GridData gd = new GridData(GridData.BEGINNING
                | GridData.FILL_BOTH);
        gd.horizontalIndent = HORIZONTAL_INDENT;
        gd.verticalIndent = POPUP_VERTICALSPACING;
        text.setLayoutData(gd);
        text.setText(accelerator + " (" + actionName + ")");
        
        configureBigFont(text);

        // since SWT.NO_FOCUS is only a hint...
        text.addFocusListener(new FocusAdapter() {
            @Override
            @SuppressWarnings("unused")
            public void focusGained(FocusEvent event) {
                NagPopUp.this.close();
            }
        });
        text.pack(true);
        return text;
    }

    /**
     * Configures big font for this. 
     * @param c the control to increase font for. Not <code>null</code>.
     */
    private void configureBigFont(Control c) {
        final FontData[] fontData = c.getFont().getFontData();
        for (int i = 0; i < fontData.length; i++) {
          fontData[i].setHeight(fontData[i].getHeight() * FONT_INCREASE_MULT);
        }
        final Font newFont = new Font(getDisplay(), fontData);
        c.setFont(newFont);
        c.addDisposeListener(new DestroyFontDisposeListener(newFont));
    }

    /**
     * {@inheritDoc}
     * Places the dialog close to a mouse pointer.
     */
    @Override
    public int open() {
        open = true;
        setParentShell(PlatformUI.getWorkbench().getDisplay().getActiveShell());
        getDisplay().timerExec(CLOSE_TIMEOUT,
                new Runnable() {
                    public void run() {
                        NagPopUp.this.close();
                    }
                });
        addCloseOnActionListeners();
        return super.open();
    }

    /** {@inheritDoc} */
    @Override
    public boolean close() {
        open = false;
        removeCloseOnActionListeners();
        return super.close();
    }

    /**
     * Adds listeners to close the dialog on any user action.
     * @see #closeOnActionListener
     * @see #CLOSE_LISTENER_TIMEOUT
     */
    private void addCloseOnActionListeners() {
        getDisplay().timerExec(CLOSE_LISTENER_TIMEOUT,
                new Runnable() {
                    public void run() {
                        if (!open) {
                            return;
                        }
                        final Listener l = closeOnActionListener;
                        getDisplay().addFilter(SWT.MouseDown, l);
                        getDisplay().addFilter(SWT.Selection, l);
                        getDisplay().addFilter(SWT.KeyDown, l);
                    }
                });
    }

    /**
     * Removes listeners to close the dialog on any user action.
     * @see #closeOnActionListener
     */
    private void removeCloseOnActionListeners() {
        // use workbench display, because can be called more than once,
        // including when this shell and the parent shell are already discarded
        final Display d = PlatformUI.getWorkbench().getDisplay();
        d.removeFilter(SWT.MouseDown, closeOnActionListener);
        d.removeFilter(SWT.Selection, closeOnActionListener);
        d.removeFilter(SWT.KeyDown, closeOnActionListener);
    }

    /**
     * Current dialog display. Never <code>null</code>.
     * Must be called only after dialog shell is created.
     */
    private Display getDisplay() {
        assert getParentShell() != null;
        return getParentShell().getDisplay();
    }

    /** {@inheritDoc} */
    @Override
    protected Point getInitialLocation(Point initialSize) {
        final Point p = getDisplay().getCursorLocation();
        p.x += DISTANCE_TO_CURSOR;
        p.y = Math.max(p.y - initialSize.y / 2, 0); 
        return p;
    }

    /**
     * Disposes the provided font when the widget it listens to is disposed. 
     */
    private static class DestroyFontDisposeListener implements DisposeListener {
        /**
         * The font to destroy.
         */
        private final Font newFont;

        public DestroyFontDisposeListener(Font newFont) {
            this.newFont = newFont;
        }

        @SuppressWarnings("unused")
        public void widgetDisposed(DisposeEvent e) {
            newFont.dispose();
        }
    }
}

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
package com.mousefeed.eclipse;

import static com.mousefeed.eclipse.Layout.WHOLE_SIZE;
import static com.mousefeed.eclipse.Layout.WINDOW_MARGIN;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;
import static org.apache.commons.lang.time.DateUtils.MILLIS_PER_SECOND;

import com.mousefeed.client.Messages;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

//COUPLING:OFF - just uses a lot of other classes. It's Ok.
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
     * Provides messages text.
     */
    private static final Messages MESSAGES = new Messages(NagPopUp.class);

    /**
     * @see NagPopUp#NagPopUp(String, String)
     */
    private final String actionName;
    
    /**
     * @see NagPopUp#NagPopUp(String, String)
     */
    private final String accelerator;
    
    /**
     * Indicates whether MouseFeed canceled the action the popup notifies about.
     */
    private final boolean actionCancelled;

    /**
     * Is <code>true</code> when the dialog is already open, but not closed yet.
     */
    private boolean open;
    
    /**
     * The notification text.
     */
    private StyledText actionDescriptionText;

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
     * @param actionName the action label. Not blank.
     * @param accelerator the string describing the accelerator. Not blank.
     * @param actionCancelled indicates whether MouseFeed canceled the action
     * the popup notifies about. 
     */
    public NagPopUp(
            String actionName, String accelerator, boolean actionCancelled) {
        super((Shell) null, PopupDialog.HOVER_SHELLSTYLE,
                false, false, false, false,
                getTitleText(actionCancelled),
                getActionCongigurationReminder());
        isTrue(StringUtils.isNotBlank(actionName));
        isTrue(StringUtils.isNotBlank(accelerator));

        this.actionName = actionName;
        this.accelerator = accelerator;
        this.actionCancelled = actionCancelled;
    }

    /**
     * Creates a control for showing the info.
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite composite = new Composite(parent, SWT.NO_FOCUS);
        composite.setLayout(new FormLayout());
        
        actionDescriptionText = createActionDescriptionText(composite);
        composite.pack(true);

        return composite;
    }

    /**
     * Creates the text control to show action description.
     * @param parent the parent control. Not <code>null</code>. 
     * @return the text control. Not <code>null</code>.
     */
    private StyledText createActionDescriptionText(Composite parent) {
        notNull(parent);
        final StyledText text = new StyledText(parent, SWT.READ_ONLY);
        final FormData formData = new FormData();
        formData.left = new FormAttachment(WINDOW_MARGIN);
        formData.right = new FormAttachment(WHOLE_SIZE, -WINDOW_MARGIN);
        formData.top = new FormAttachment(WINDOW_MARGIN);
        formData.bottom = new FormAttachment(WHOLE_SIZE, -WINDOW_MARGIN);
        text.setLayoutData(formData);
        text.setText(accelerator + " (" + actionName + ")");
        if (actionCancelled) {
            final StyleRange style = new StyleRange();
            style.start = 0;
            style.length = text.getText().length();
            style.strikeout = true;
            text.setStyleRange(style);
        }

        configureBigFont(text);

        // since SWT.NO_FOCUS is only a hint...
        text.addFocusListener(new FocusAdapter() {
            @Override
            @SuppressWarnings("unused")
            public void focusGained(FocusEvent event) {
                NagPopUp.this.close();
            }
        });
        return text;
    }
    
    /**
     * Set the text color here, not in {@link #createDialogArea(Composite)},
     * because it is redefined after that method is called.
     * @param parent the control parent. Not <code>null</code>.
     * @return the super value.
     */
    @Override
    protected Control createContents(Composite parent) {
        final Control control = super.createContents(parent);
        if (actionCancelled) {
            actionDescriptionText.setForeground(
                    getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        return control;
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
        setParentShell(getDisplay().getActiveShell());
        if (actionCancelled) {
            getDisplay().beep();
        }
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
     */
    private Display getDisplay() {
        return PlatformUI.getWorkbench().getDisplay();
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
     * Text to show in the title.
     * Is static to make sure it does not rely on the instance members because
     * is called before calling "super" constructor.
     * @param canceled whether the action was canceled.
     * @return the title text. Can be <code>null</code>.
     */
    private static String getTitleText(boolean canceled) {
        return canceled ? MESSAGES.get("canceled") : null;
    }

    /**
     * Generates the text shown at the bottom of the popup.
     * Is static to make sure it does not rely on the instance members because
     * is called before calling "super" constructor.
     * @return the text. Never <code>null</code>.
     */
    private static String getActionCongigurationReminder() {
        final IBindingService bindingService =
            (IBindingService) getWorkbench().getAdapter(IBindingService.class);
        final TriggerSequence[] bindings =
            bindingService.getActiveBindingsFor(
                    "com.mousefeed.commands.configureActionInvocation");
        final String binding = bindings.length == 0
                ? MESSAGES.get("configureActionInvocation-noBinding")
                : bindings[0].format();
        return MESSAGES.get("info", binding);
    }

    /**
     * Current workbench. Not <code>null</code>.
     */
    private static IWorkbench getWorkbench() {
        return PlatformUI.getWorkbench();
    }
}
//COUPLING:ON

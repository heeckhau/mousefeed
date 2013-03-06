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
package com.mousefeed.eclipse;

import static com.mousefeed.eclipse.Layout.WHOLE_SIZE;
import static com.mousefeed.eclipse.Layout.WINDOW_MARGIN;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;
import static org.apache.commons.lang.time.DateUtils.MILLIS_PER_SECOND;

import com.mousefeed.client.Messages;
import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.preference.PreferenceDialog;
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
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.dialogs.PreferencesUtil;

//COUPLING:OFF - just uses a lot of other classes. It's Ok.
/**
 * Pop-up dialog, which notifies a user about wrong mouse/accelerator usage. 
 *
 * @author Andriy Palamarchuk
 * @author Robert Wloch
 */
public class NagPopUp extends PopupDialog {
    /**
     * Launcher runnable to open preference dialog.
     * 
     * @author Robert Wloch
     */
    protected static class PreferenceDialogLauncher implements Runnable {
        /**
         * Data object used as data parameter to the keys preference page.
         */
        private final Object data;

        /**
         * Constructs a launcher to open the keys preference page with the optional data object as parameter.
         * @param data optional data object used as data parameter to the keys preference page
         */
        protected PreferenceDialogLauncher(final Object data) {
            this.data = data;
        }

        /**
         * Creates and opens the Keys preference page.
         */
        public void run() {
            final Display workbenchDisplay = PlatformUI.getWorkbench().getDisplay();
            final Shell activeShell = workbenchDisplay.getActiveShell();
            
            final String id = ORG_ECLIPSE_UI_PREFERENCE_PAGES_KEYS_ID;
            final String[] displayedIds = new String[] {id};
            final PreferenceDialog preferenceDialog = 
                    PreferencesUtil.createPreferenceDialogOn(activeShell, id, displayedIds, data);
            preferenceDialog.open();
        }
    }

    /**
     * ID of keys preference page.
     */
    private static final String ORG_ECLIPSE_UI_PREFERENCE_PAGES_KEYS_ID = "org.eclipse.ui.preferencePages.Keys";

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
     * The last action invocation reminder text factory.
     */
    private static final LastActionInvocationRemiderFactory REMINDER_FACTORY =
            new LastActionInvocationRemiderFactory();

    /**
     * @see NagPopUp#NagPopUp(String, String, boolean)
     */
    private final String actionName;

    /**
     * @see NagPopUp#NagPopUp(String, String)
     */
    private final String actionId;
    
    /**
     * @see NagPopUp#NagPopUp(String, String, boolean)
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
     * The notification link.
     */
    @SuppressWarnings("unused")
    private Link actionLink;
    
    /**
     * Closes the dialog on any outside action, such as click, key press, etc.
     */
    private Listener closeOnActionListener = new Listener() {
        public void handleEvent(final Event event) {
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
            final String actionName, final String accelerator, final boolean actionCancelled) {
        super((Shell) null, PopupDialog.HOVER_SHELLSTYLE,
                false, false, false, false, false,
                getTitleText(actionCancelled),
                getActionConfigurationReminder());
        isTrue(StringUtils.isNotBlank(actionName));
        isTrue(StringUtils.isNotBlank(accelerator));

        this.actionName = actionName;
        this.accelerator = accelerator;
        this.actionCancelled = actionCancelled;
        this.actionId = null;
    }

    /**
     * Creates a pop-up with suggestion to open the Keys preference page to
     * configure a keyboard shortcut for an action.
     *
     * @param actionName the action label. Not blank.
     * @param actionId the contribution id. Not blank.
     */
    public NagPopUp(
            final String actionName, final String actionId) {
        super((Shell) null, PopupDialog.HOVER_SHELLSTYLE,
                false, false, false, false, false,
                getTitleText(false),
                getActionConfigurationReminder());
        isTrue(StringUtils.isNotBlank(actionName));
        isTrue(StringUtils.isNotBlank(actionId));

        this.actionName = actionName;
        this.actionId = actionId;
        this.actionCancelled = false;
        this.accelerator = null;
    }

    /**
     * Creates a control for showing the info.
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NO_FOCUS);
        composite.setLayout(new FormLayout());
        
        if (isLinkPopup()) {
            final String linkText = MESSAGES.get("message.configureShortcut", actionName);
            actionLink = createLink(composite, linkText);
        } else {
            actionDescriptionText = createActionDescriptionText(composite);
        }

        composite.pack(true);

        return composite;
    }

    /**
     * Reusable check for actionId not null and accelerator being null.
     * @return true if actionId != null && accelerator == null
     */
    protected boolean isLinkPopup() {
        return actionId != null && accelerator == null;
    }
    
    /**
     * Creates the text control to show action description.
     * @param parent the parent control. Not <code>null</code>. 
     * @return the text control. Not <code>null</code>.
     */
    private StyledText createActionDescriptionText(final Composite parent) {
        notNull(parent);
        final StyledText text = new StyledText(parent, SWT.READ_ONLY);
        configureFormData(text);
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
            public void focusGained(final FocusEvent event) {
                NagPopUp.this.close();
            }
        });
        return text;
    }
    
    /**
     * Creates the link control to show a hyperlink to the Keys
     * preference page.
     * 
     * @param parent the parent control. Not <code>null</code>. 
     * @param text the text of the link
     * @return the link control. Not <code>null</code>.
     */
    protected Link createLink(final Composite parent, final String text) {
        final Link link = new Link(parent, SWT.NONE);
        link.setFont(parent.getFont());
        link.setText("<A>" + text + "</A>");  //$NON-NLS-1$//$NON-NLS-2$
        
        configureFormData(link);
        configureBigFont(link);

        link.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent e) {
                doLinkActivated();
            }
        });

        return link;
    }
    
    /**
     * Handle link activation.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    final void doLinkActivated() {
        Object data = null;
        
        final IWorkbench workbench = Activator.getDefault().getWorkbench();
        final ICommandService commandService = (ICommandService) workbench.getService(ICommandService.class);
        
        final Command command = commandService.getCommand(actionId);
        if (command != null) {
            final HashSet allParameterizedCommands = new HashSet();
            try {
                allParameterizedCommands.addAll(ParameterizedCommand
                        .generateCombinations(command));
            } catch (final NotDefinedException e) {
                // It is safe to just ignore undefined commands.
            }
            if (!allParameterizedCommands.isEmpty()) {
                data = allParameterizedCommands.iterator().next();
                
                // only commands can be bound to keyboard shortcuts
                openWorkspacePreferences(data);
            }
        }

    }

    /**
     * Opens the preference dialog with optional data.
     * 
     * @param data an optional data object that can be handed as a parameter to the preference dialog. May be null.
     */
    protected final void openWorkspacePreferences(final Object data) {
        final Display display = Display.getCurrent();
        final PreferenceDialogLauncher runnable = new PreferenceDialogLauncher(data);
        display.asyncExec(runnable);
    }
    
    /**
     * Set the text color here, not in {@link #createDialogArea(Composite)},
     * because it is redefined after that method is called.
     * @param parent the control parent. Not <code>null</code>.
     * @return the super value.
     */
    @Override
    protected Control createContents(final Composite parent) {
        final Control control = super.createContents(parent);
        if (actionCancelled) {
            actionDescriptionText.setForeground(
                    getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        return control;
    }
    
    /**
     * Configures sizes and margins for this. 
     * @param c the control to set the form data for. Not <code>null</code>.
     */
    private void configureFormData(final Control c) {
        final FormData formData = new FormData();
        formData.left = new FormAttachment(WINDOW_MARGIN);
        formData.right = new FormAttachment(WHOLE_SIZE, -WINDOW_MARGIN);
        formData.top = new FormAttachment(WINDOW_MARGIN);
        formData.bottom = new FormAttachment(WHOLE_SIZE, -WINDOW_MARGIN);
        c.setLayoutData(formData);
    }

    /**
     * Configures big font for this. 
     * @param c the control to increase font for. Not <code>null</code>.
     */
    private void configureBigFont(final Control c) {
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
    protected Point getInitialLocation(final Point initialSize) {
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
    private static String getTitleText(final boolean canceled) {
        return canceled
                ? MESSAGES.get("title.canceled")
                : MESSAGES.get("title.reminder");
    }

    /**
     * Generates the text shown at the bottom of the popup.
     * Is static to make sure it does not rely on the instance members because
     * is called before calling "super" constructor.
     * @return the text. Never <code>null</code>.
     */
    private static String getActionConfigurationReminder() {
        return REMINDER_FACTORY.getText();
    }
}
//COUPLING:ON

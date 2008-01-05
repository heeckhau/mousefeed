/*
 * Copyright (C) Heavy Lifting Software 2007-2008.
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
package com.mousefeed.eclipse.views;

import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.collector.Collector;
import com.mousefeed.client.collector.Event;
import com.mousefeed.client.collector.IEventListener;
import com.mousefeed.eclipse.Activator;
import com.mousefeed.eclipse.MouseFeedPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

/**
 * Shows log of user activity.
 *
 * @author Andriy Palamarchuk
 */
public class UserActivityView extends ViewPart {
    /**
     * Shows the user activity log.
     */
    private Text logText;

    /**
     * The action to turn on/off user activity logging.
     */
    private IAction toggleLoggingAction;
    
    /**
     * @see CollectorEventListener
     */
    private final CollectorEventListener collectorEventListener =
            new CollectorEventListener();
    
    /**
     * Indicator that user activity logging is turned on.
     */
    private boolean logging;

    // see base
    @Override
    public void createPartControl(Composite parent) {
        logText = new Text(parent,
                SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
        makeActions();
        contributeToActionBars();
    }

    /**
     * Contributes to the view action bars.
     */
    private void contributeToActionBars() {
        final IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    /**
     * Initializes local toolbar of the view.
     * @param manager the toolbar manager. Assumed not <code>null</code>.
     */
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(toggleLoggingAction);
    }

    /**
     * Creates view actions.
     */
    private void makeActions() {
        createToggleLoggingAction();
    }

    /**
     * Creates {@link #toggleLoggingAction}.
     */
    private void createToggleLoggingAction() {
        toggleLoggingAction = new Action("RUN TEXT", IAction.AS_CHECK_BOX) {
            public void run() {
                if (isChecked()) {
                    startLogging();
                } else {
                    stopLogging();
                }
            }
        };
        toggleLoggingAction.setText("Log User Activity");
        toggleLoggingAction.setToolTipText("Start/Stop User Activity Logging");
        toggleLoggingAction.setImageDescriptor(
                MouseFeedPluginImages.getInstance().getImageDescriptor(
                        MouseFeedPluginImages.IMG_TOGGLE_LOGGING));
    }

    /**
     * Start logging user activity.
     */
    private void startLogging() {
        logging = true;
        getCollector().addEventListener(collectorEventListener, null);
    }

    /**
     * Stop logging user activity.
     */
    protected void stopLogging() {
        if (logging) {
            getCollector().removeEventListener(collectorEventListener);
            logging = false;
        }
    }
    
    /**
     * Adds the provided text with an additional new line to the view. 
     * @param text the text to add to the view. Not null.
     */
    private void println(String text) {
        notNull(text);
        logText.append(text);
        logText.append("\n");
    }

    // see base
    @Override
    public void dispose() {
        stopLogging();
        super.dispose();
    }

    /**
     * The plugin user activity data collector.
     * @return the data collector. Not <code>null</code>.
     */
    private Collector getCollector() {
        return Activator.getDefault().getCollector();
    }

    // see base
    @Override
    public void setFocus() {
        logText.setFocus();
    }

    /**
     * The collector events listener. Reports user activity in the view. 
     */
    private class CollectorEventListener implements IEventListener {
        public void event(Event event) {
            println(event.toString());
        }
    }
}

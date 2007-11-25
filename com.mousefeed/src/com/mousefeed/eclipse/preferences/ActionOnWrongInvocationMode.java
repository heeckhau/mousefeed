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

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.client.collector.ActionDesc;

/**
 * Stores action-specific preferences for handling action invocation using
 * wrong mode.
 * Data stored in this class somewhat overlaps with {@link ActionDesc}.
 *
 * @author Andriy Palamarchuk
 */
public class ActionOnWrongInvocationMode {
    /**
     * @see #getId() 
     */
    private String id;
    
    /**
     * @see #getLabel()
     */
    private String label;
    
    /**
     * @see #getOnWrongInvocationMode()
     */
    private OnWrongInvocationMode onWrongInvocationMode;

    /**
     * Creates a new instance.
     */
    public ActionOnWrongInvocationMode() {
    }

    /**
     * Creates a new instance from the {@link ActionDesc} instance. Copies all
     * the data.
     * @param actionDesc the instance to create this instance from.
     * Not <code>null</code>
     */
    public ActionOnWrongInvocationMode(ActionDesc actionDesc) {
        notNull(actionDesc);
        setId(actionDesc.getId());
        setLabel(actionDesc.getLabel());
    }

    /**
     * The action id. Used to identify this preference.
     * @return the id. Never <code>null</code> after initialization.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set. Not <code>null</code>.
     * @see #getId()
     */
    public void setId(String id) {
        isTrue(isNotBlank(id));
        this.id = id;
    }

    /**
     * The action label. Used for display purposes.
     * @return the label. Not <code>null</code> after initialized.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set. Not blank.
     * @see #getLabel()
     */
    public void setLabel(String label) {
        isTrue(isNotBlank(label));
        this.label = label;
    }

    /**
     * What to do on wrong invocation.
     * @return the value. Never <code>null</code> after initialized.
     */
    public OnWrongInvocationMode getOnWrongInvocationMode() {
        return onWrongInvocationMode;
    }

    /**
     * @param onWrongInvocationMode the new value. Not <code>null</code>.
     * @see #getOnWrongInvocationMode()
     */
    public void setOnWrongInvocationMode(
            OnWrongInvocationMode onWrongInvocationMode) {
        notNull(onWrongInvocationMode);
        this.onWrongInvocationMode = onWrongInvocationMode;
    }
}

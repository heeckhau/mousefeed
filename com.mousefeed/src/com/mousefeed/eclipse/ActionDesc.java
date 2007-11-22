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

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Eclipse-specific action description.
 *
 * @author Andriy Palamarchuk
 */
public class ActionDesc extends com.mousefeed.client.collector.ActionDescBase {
    /**
     * @see #getClassName()
     */
    private String className;
    
    /**
     * @see #getDef()
     */
    private String def;
    
    /**
     * @return the id. Returns the first non-blank value of the following:
     * {@link #getDef()}, {@link #getClassName()}. If all of these are blank,
     * returns base implementation.
     */
    @Override
    public String getId() {
        if (isNotBlank(getDef())) {
            return getDef();
        }
        if (isNotBlank(getClassName())) {
            return getClassName();
        }
        return super.getId();
    }

    /**
     * The action/command class name.
     * @return the className. Can be <code>null</code>.
     * @see #getId()
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set.
     * If blank, the existing value does not change.
     * @see #getClassName()
     */
    public void setClassName(String className) {
        if (isBlank(className)) {
            return;
        }
        this.className = className;
    }

    /**
     * The action/command definition id.
     * @return the definition id. Can be <code>null</code>.
     * @see #getId()
     */
    public String getDef() {
        return def;
    }

    /**
     * @param def the definition id to set.
     * If blank, the existing value does not change.
     * @see #getDef()
     */
    public void setDef(String def) {
        if (isBlank(def)) {
            return;
        }
        this.def = def;
    }
}

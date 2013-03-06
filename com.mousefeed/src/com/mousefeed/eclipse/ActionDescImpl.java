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

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.Validate.isTrue;

import com.mousefeed.client.collector.AbstractActionDesc;

/**
 * Eclipse-specific action description.
 *
 * @author Andriy Palamarchuk
 */
public class ActionDescImpl extends AbstractActionDesc {
    /**
     * @see #getClassName()
     */
    private String className;
    
    /**
     * @see #getDef()
     */
    private String def;

    /**
     * Default constructor does nothing.
     */
    public ActionDescImpl() {
    }
    
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
    public void setClassName(final String className) {
        if (isBlank(className)) {
            return;
        }
        
        isNullOrNotEqual(this.className, className);
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
    public void setDef(final String def) {
        if (isBlank(def)) {
            return;
        }
        
        isNullOrNotEqual(this.def, def);
        this.def = def;
    }

    /**
     * Throws <code>IllegalArgumentException</code> if <code>val1</code> is not
     * <code>null</code> and is equal <code>val2</code>. 
     * @param val1 the main value to check. Can be <code>null</code>.
     * @param val2 the value to check against. Assumed not <code>null</code>.
     */
    private void isNullOrNotEqual(final String val1, final String val2) {
        isTrue(val1 == null || val2.equals(val1),
                "Should be different: " + val2 + " and " + val1);
    }
}

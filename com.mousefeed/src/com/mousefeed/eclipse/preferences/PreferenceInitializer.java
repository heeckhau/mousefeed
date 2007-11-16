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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.Activator;

/**
 * Initializes default preference values.
 *
 * @author Andriy Palamarchuk
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /** {@inheritDoc} */
    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore store =
               Activator.getDefault().getPreferenceStore();
        store.setDefault(
                PreferenceConstants.P_DEFAULT_ON_WRONG_INVOCATION_MODE,
                OnWrongInvocationMode.DEFAULT.name());
    }
}

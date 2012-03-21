/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.rwt.branding.AbstractBranding;

class UIBrandingAndroid extends AbstractBranding {

  @Override
  public String getServletName() {
    return "configurator";
  }

  @Override
  public String getThemeId() {
    return UIConfigurator.THEME_ID_ANDROID;
  }

  @Override
  public String getTitle() {
    return "EclipseRT Configurator for Android";
  }

  @Override
  public String getDefaultEntryPoint() {
    return UIConfigurator.DEFAULT_ENTRY_POINT;
  }
}

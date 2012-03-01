/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ConfiguratorUiFooter {

  public void createFooter( Shell shell ) {
    Composite footer = UiHelper.createGridComposite( shell, 1, true );
    // TODO - Get the RTP version.
    UiHelper.createLabel( footer, 1, "RTP Version XZY" );
  }
}

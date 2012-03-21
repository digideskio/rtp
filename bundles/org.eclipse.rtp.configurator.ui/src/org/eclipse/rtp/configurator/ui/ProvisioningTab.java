/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class ProvisioningTab extends AbstractTabContribution {

  @Override
  public String getTitle() {
    return "Provisioning";
  }

  @Override
  @SuppressWarnings( "serial" )
  protected void populateControl( Display display, Composite composite ) {
    Composite planetTab = UiHelper.createGreedyGridComposite( composite, 1, true );
    UiHelper.createLabel( planetTab, 1, "Provisioning Tab" );
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
  }
}

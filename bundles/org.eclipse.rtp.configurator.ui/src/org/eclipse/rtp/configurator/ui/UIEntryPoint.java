/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UIEntryPoint implements IEntryPoint {

  private final List<TabContribution> contributions = new ArrayList<TabContribution>();
  private ConfiguratorUiHeader configuratorUiHeader;
  private ConfiguratorUiBody configuratorUiBody;
  private ConfiguratorUiFooter configuratorUiFooter;

  @Override
  public int createUI() {
    initUI();
    openUI();
    return 0;
  }

  private void initUI() {
    configuratorUiHeader = new ConfiguratorUiHeader();
    configuratorUiBody = new ConfiguratorUiBody();
    configuratorUiFooter = new ConfiguratorUiFooter();
  }

  private void openUI() {
    Display display = new Display();
    final Shell shell = UiHelper.createShell( display );
    initShell( shell );
    configuratorUiHeader.createHeader( display, shell );
    contributions.add( new ComponentsTab() );
    // contributions.add( new ProvisioningTab() );
    configuratorUiBody.createBody( shell, contributions );
    configuratorUiFooter.createFooter( shell );
    shell.open();
  }

  private void initShell( final Shell shell ) {
    shell.addDisposeListener( new DisposeListener() {

      @SuppressWarnings( "synthetic-access" )
      @Override
      public void widgetDisposed( DisposeEvent event ) {
        disposeUI();
      }
    } );
  }

  private void disposeUI() {
    configuratorUiHeader.dispose();
    configuratorUiBody.dispose();
    configuratorUiFooter.dispose();
  }
}

/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ConfiguratorUiBody {

  public void createBody( Shell shell, List<TabContribution> contributions ) {
    Composite body = UiHelper.createGreedyGridComposite( shell, 1, true );
    final TabFolder tabFolder = new TabFolder( body, SWT.BORDER );
    tabFolder.setLayoutData( UiHelper.createGreedyGridData() );
    for( TabContribution contribution : contributions ) {
      TabItem tabItem = new TabItem( tabFolder, SWT.INHERIT_DEFAULT );
      tabItem.setText( contribution.getTitle() );
      Control control = contribution.getControl( shell.getDisplay(), tabFolder );
      control.setLayoutData( UiHelper.createGreedyGridData() );
      tabItem.setControl( control );
    }
    if( tabFolder.getChildren().length > 0 ) {
      tabFolder.setSelection( 0 );
    }
  }
}

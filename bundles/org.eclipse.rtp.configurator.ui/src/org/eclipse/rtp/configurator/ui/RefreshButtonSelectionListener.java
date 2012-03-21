/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

final class RefreshButtonSelectionListener extends SelectionAdapter {

  private final ComponentsTab componentsTab;

  RefreshButtonSelectionListener( ComponentsTab componentsTab ) {
    this.componentsTab = componentsTab;
  }

  @Override
  public void widgetSelected( SelectionEvent e ) {
    this.componentsTab.refresh();
  }
}

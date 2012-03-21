/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractTabContribution implements TabContribution {

  @Override
  public Control getControl( Display display, Composite parent ) {
    Composite composite = UiHelper.createGridComposite( parent, 1, true );
    populateControl( display, composite );
    return composite;
  }

  protected abstract void populateControl( Display display, Composite composite );
}

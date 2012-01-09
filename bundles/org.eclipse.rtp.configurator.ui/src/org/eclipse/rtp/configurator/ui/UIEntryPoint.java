/******************************************************************************* 
* Copyright (c) 2012 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.configurator.ui;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class UIEntryPoint implements IEntryPoint {

  @Override
  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 1, false ) );
    shell.setSize( 500, 500 );
    shell.open();
    return 0;
  }
}

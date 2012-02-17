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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Constructs the Configurator UI header.
 * @author fluffi
 *
 */
public class ConfiguratorUiHeader {

  public void createHeader( Display display, Shell shell ) {
    Composite header = UiHelper.createGridComposite( shell, 3 );
    Image logo = new Image( display, getClass().getResourceAsStream( "/images/rtp-icon.png" ) );
    Label label = UiHelper.createLabel( header, 1, "" );
    label.setImage( logo );
    UiHelper.createLabel( header, 2, new UIBrandingWeb().getTitle() );
  }
}

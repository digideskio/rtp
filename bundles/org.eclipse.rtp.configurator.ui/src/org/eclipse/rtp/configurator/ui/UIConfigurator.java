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

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.eclipse.rwt.application.ApplicationConfigurator;


public class UIConfigurator implements ApplicationConfigurator {

  public static final String RTP = "rtp";

  @Override
  public void configure( ApplicationConfiguration configuration ) {
    configuration.addEntryPoint( "default", UIEntryPoint.class );
    configuration.addStyleSheet( RTP, "theme/theme.css" );
    configuration.addBranding( new UIBranding() );
  }
}

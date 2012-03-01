/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class SourcesLabelProvider implements ITableLabelProvider {

  private Image httpSource;
  private final ComponentsTab componentsTab;

  // TODO check supported sources...
  // private Image fileSource;
  public SourcesLabelProvider( ComponentsTab componentsTab ) {
    this.componentsTab = componentsTab;
  }

  public void init( Display display ) {
    httpSource = new Image( display, getClass().getResourceAsStream( "/images/rtp-icon.png" ) );
    // fileSource = new Image( display, getClass().getResourceAsStream( "/images/rtp-icon.png" )
    // );
  }

  @Override
  public Image getColumnImage( Object element, int columnIndex ) {
    Image result = null;
    if( columnIndex == 0 && element instanceof Source ) {
      result = httpSource;
    }
    return result;
  }

  @Override
  public String getColumnText( Object element, int columnIndex ) {
    String result = "";
    if( columnIndex == 0 ) {
      if( element instanceof Source ) {
        result = ( ( Source )element ).getName();
      } else if( element instanceof SourceVersion ) {
        result = ( ( SourceVersion )element ).getVersion();
      }
    } else if( columnIndex == 1 && element instanceof SourceVersion ) {
      result = ( ( SourceVersion )element ).getRepositoryUrl().toString();
    } else if( columnIndex == 2 && element instanceof SourceVersion ) {
      boolean installed = componentsTab.isInstalled( ( SourceVersion )element );
      if( installed ) {
        result = "installed";
      } else {
        result = "uninstalled";
      }
    }
    return result;
  }

  @Override
  public void addListener( ILabelProviderListener listener ) {
    // No listeners.
  }

  @Override
  public void dispose() {
    // Nothing to dispose.
  }

  @Override
  public boolean isLabelProperty( Object element, String property ) {
    return false;
  }

  @Override
  public void removeListener( ILabelProviderListener listener ) {
    // No listeners used.
  }
}

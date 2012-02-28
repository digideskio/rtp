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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class SourcesLabelProvider implements ITableLabelProvider {

  private Image httpSource;

  // TODO check supported sources...
  // private Image fileSource;
  public SourcesLabelProvider() {
  }

  public void init( Display display ) {
    httpSource = new Image( display, getClass().getResourceAsStream( "/images/rtp-icon.png" ) );
    // fileSource = new Image( display, getClass().getResourceAsStream( "/images/rtp-icon.png" )
    // );
  }

  @Override
  public Image getColumnImage( Object element, int columnIndex ) {
    if( columnIndex == 0 ) {
      return httpSource;
    }
    return null;
  }

  @Override
  public String getColumnText( Object element, int columnIndex ) {
    Source row = ( Source )element;
    if( columnIndex == 0 ) {
      // source name
      return row.getName();
    } else if (columnIndex == 1) {
      // TODO - support multiple versions
      return row.getVersions().get( 0 ).getVersion();
    }
    return null;
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
/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rtp.core.model.Source;

class SourcesContentProvider implements ITreeContentProvider {

  private List<Source[]> model;

  public SourcesContentProvider() {
  }

  @Override
  public void dispose() {
    // Nothing to dispose.
  }

  @Override
  public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
    this.model = ( List<Source[]> )newInput;
  }

  @Override
  public Object[] getElements( Object inputElement ) {
    return model.toArray();
  }

  @Override
  public Object[] getChildren( Object parentElement ) {
    return null;
  }

  @Override
  public Object getParent( Object element ) {
    return null;
  }

  @Override
  public boolean hasChildren( Object element ) {
    return false;
  }
}
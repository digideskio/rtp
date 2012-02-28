/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rtp.core.model.Source;


public class ComponentsFilter extends ViewerFilter {
  private String searchString;

  public void setSearchText(String s) {
      // Search must be a substring of the existing value
      this.searchString = ".*" + s + ".*";
  }

  @Override
  public boolean select( Viewer viewer, Object parentElement, Object element) {
      if (searchString == null || searchString.length() == 0) {
          return true;
      }
      Source source = (Source) element;
      if (source.getName().matches(searchString)) {
          return true;
      }
      // TODO - support multiple versions?
      if (source.getVersions().get( 0 ).getVersion().matches(searchString)) {
          return true;
      }

      return false;
  }
}

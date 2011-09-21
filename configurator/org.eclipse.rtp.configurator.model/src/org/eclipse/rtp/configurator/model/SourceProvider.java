/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.configurator.model;

import java.util.ArrayList;
import java.util.List;


public class SourceProvider {
  
  private List<Source> sources;

  public SourceProvider() {
    this.sources = new ArrayList<Source>();
  }

  public void addSource( Source source ) {
    sources.add( source );
  }

  public List<Source> getSources() {
    return new ArrayList<Source>( sources );
  }
}

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;


public class SourceProviderTest {
  
  @Test
  public void testAddSources() {
    SourceProvider sourceProvider = new SourceProvider();
    Source source = mock( Source.class );
    
    sourceProvider.addSource( source );
    
    List<Source> sources = sourceProvider.getSources();
    assertEquals( 1, sources.size() );
    assertEquals( source, sources.get( 0 ) );
  }
  
  @Test
  public void testGetSources() {
    SourceProvider sourceProvider = new SourceProvider();
    sourceProvider.addSource( mock( Source.class ) );
    
    List<Source> sources = sourceProvider.getSources();
    sourceProvider.addSource( mock( Source.class ) );
    
    assertEquals( 1, sources.size() );
  }
}

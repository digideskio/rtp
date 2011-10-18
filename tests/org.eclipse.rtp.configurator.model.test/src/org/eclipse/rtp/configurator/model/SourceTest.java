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

import org.eclipse.rtp.configurator.model.Source;
import org.junit.Before;
import org.junit.Test;


public class SourceTest {
  
  private Source source;

  @Before
  public void setUp() {
    source = new Source( "foo", "description", "http://info.bar" );
  }
  
  @Test
  public void testGetName() {
    assertEquals( "foo", source.getName() );
  }
  
  @Test
  public void testGetDescription() {
    assertEquals( "description", source.getDescription() );
  }
  
  @Test
  public void testGetInfoUrl() {
    assertEquals( "http://info.bar", source.getInfoUrl() );
  }
  
  @Test
  public void testAddVersion() {
    SourceVersion version = mock( SourceVersion.class );

    source.addVersion( version );
    
    List<SourceVersion> versions = source.getVersions();
    assertEquals( 1, versions.size() );
    assertEquals( version, versions.get( 0 ) );
  }
  
  @Test
  public void testGetVersion() {
    source.addVersion( mock( SourceVersion.class ) );
    List<SourceVersion> versions = source.getVersions();
    source.addVersion( mock( SourceVersion.class ) );
    
    assertEquals( 1, versions.size() );
  }
}

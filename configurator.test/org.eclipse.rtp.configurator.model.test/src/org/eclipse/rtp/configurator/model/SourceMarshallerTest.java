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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rtp.configurator.model.internal.SourceUnMarshallerImpl;
import org.junit.Test;


public class SourceMarshallerTest {
  
  @Test
  public void first() throws IOException {
    InputStream stream = readExampleSources();
    SourceMarshaller marshaller = new SourceUnMarshallerImpl();
    
    SourceProvider provider = marshaller.marshal( stream );
    assertEquals( 2, provider.getSources().size() );
    checkFirstSource( provider.getSources().get( 0 ) );
    checkSecondSource( provider.getSources().get( 1 ) );
  }
  
  private void checkFirstSource( Source source ) {
    assertEquals( "rap", source.getName() );
    assertEquals( 2, source.getVersions().size() );
    checkFirstRapVersion( source );
    checkSecondRapVersion( source );
  }

  private void checkFirstRapVersion( Source source ) {
    SourceVersion source1 = source.getVersions().get( 0 );
    assertEquals( "1.4", source1.getVersion() );
    assertEquals( "http://foo.bar", source1.getRepositoryUrl() );
    assertEquals( "Some text", source1.getDescription() );
    assertEquals( "http://foo.bar/info", source1.getInfoUrl() );
  }

  private void checkSecondRapVersion( Source source ) {
    SourceVersion source2 = source.getVersions().get( 1 );
    assertEquals( "1.5", source2.getVersion() );
    assertEquals( "http://foo.bar2", source2.getRepositoryUrl() );
    assertEquals( "Some text2", source2.getDescription() );
    assertEquals( "http://foo.bar/info2", source2.getInfoUrl() );
  }

  private void checkSecondSource( Source source ) {
    assertEquals( "equinox", source.getName() );
    assertEquals( 1, source.getVersions().size() );
    checkEquinoxVersion( source );
  }

  private void checkEquinoxVersion( Source source ) {
    SourceVersion source1 = source.getVersions().get( 0 );
    assertEquals( "3.8.1", source1.getVersion() );
    assertEquals( "http://foo.bar3", source1.getRepositoryUrl() );
    assertEquals( "Some text3", source1.getDescription() );
    assertEquals( "http://foo.bar/info3", source1.getInfoUrl() );
  }

  private InputStream readExampleSources() {
    String path = "example-sources.json";
    return getClass().getResourceAsStream( path );
  }
  
}

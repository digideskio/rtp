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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.rtp.configurator.model.internal.SourceUnMarshallerImpl;
import org.junit.Test;
import org.osgi.framework.Bundle;


public class SourceUnMarshallerTest {
  
  @Test
  public void first() throws IOException {
    InputStream stream = readExampleSources();
    SourceUnMarshaller marshaller = new SourceUnMarshallerImpl();
    
    SourceProvider provider = marshaller.marshal( stream );
    assertEquals( 2, provider.getSources().size() );
    checkFirstSource( provider.getSources().get( 0 ) );
    checkSecondSource( provider.getSources().get( 1 ) );
  }
  
  private void checkFirstSource( Source source ) {
    assertEquals( "rap", source.getName() );
    assertEquals( "Some text", source.getDescription() );
    assertEquals( "http://foo.bar/info", source.getInfoUrl() );
    assertEquals( 2, source.getVersions().size() );
    checkFirstRapVersion( source );
    checkSecondRapVersion( source );
  }

  private void checkFirstRapVersion( Source source ) {
    SourceVersion source1 = source.getVersions().get( 0 );
    assertEquals( "org.eclipse.rap.feature.group", source1.getId() );
    assertEquals( "1.4", source1.getVersion() );
    assertEquals( "http://foo.bar", source1.getRepositoryUrl() );
  }

  private void checkSecondRapVersion( Source source ) {
    SourceVersion source2 = source.getVersions().get( 1 );
    assertEquals( "org.eclipse.rap.feature.group", source2.getId() );
    assertEquals( "1.5", source2.getVersion() );
    assertEquals( "http://foo.bar2", source2.getRepositoryUrl() );
  }

  private void checkSecondSource( Source source ) {
    assertEquals( "equinox", source.getName() );
    assertEquals( "Some text3", source.getDescription() );
    assertEquals( "http://foo.bar/info3", source.getInfoUrl() );
    assertEquals( 1, source.getVersions().size() );
    checkEquinoxVersion( source );
  }

  private void checkEquinoxVersion( Source source ) {
    SourceVersion source1 = source.getVersions().get( 0 );
    assertEquals( "org.eclipse.equinox.feature.group", source1.getId() );
    assertEquals( "3.8.1", source1.getVersion() );
    assertEquals( "http://foo.bar3", source1.getRepositoryUrl() );
  }

  private InputStream readExampleSources() throws IOException {
    Bundle bundle = Platform.getBundle( "org.eclipse.rtp.configurator.model.test" );
    URL unResolvedUrl = FileLocator.find( bundle, new Path("data/example-sources.json"), null );
    URL testDataUrl = FileLocator.resolve( unResolvedUrl );
    File testDataFile = new Path( testDataUrl.getFile() ).toFile();
    return new FileInputStream( testDataFile );
  }
  
}

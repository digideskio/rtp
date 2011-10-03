/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.rtp.configurator.model.internal.SourceUnMarshallerImpl;
import org.junit.Test;

public class SourceUnMarshallerTest {

  @Test
  public void testUnmarshal() throws IOException {
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
    SourceVersion version = source.getVersions().get( 0 );
    assertEquals( "1.4", version.getVersion() );
    assertEquals( "http://foo.bar", version.getRepositoryUrl() );
    List<Feature> features = version.getFeatures();
    assertTrue( 2 == features.size() );
    checkFeature( "org.eclipse.rap.feature.group", "2.5.0", features.get( 0 ) );
    checkFeature( "org.eclipse.rap.feature.group.ext", "1.5.0", features.get( 1 ) );
  }

  private void checkSecondRapVersion( Source source ) {
    SourceVersion version = source.getVersions().get( 1 );
    assertEquals( "1.5", version.getVersion() );
    assertEquals( "http://foo.bar2", version.getRepositoryUrl() );
    List<Feature> features = version.getFeatures();
    assertTrue( 2 == features.size() );
    checkFeature( "org.eclipse.rap.feature.group", "2.5.0", features.get( 0 ) );
    checkFeature( "org.eclipse.rap.feature.group.ext", "1.5.0", features.get( 1 ) );
  }

  private void checkFeature( String id, String version, Feature feature ) {
    assertEquals( id, feature.getId() );
    assertEquals( version, feature.getVersion() );
  }

  private void checkSecondSource( Source source ) {
    assertEquals( "equinox", source.getName() );
    assertEquals( "Some text3", source.getDescription() );
    assertEquals( "http://foo.bar/info3", source.getInfoUrl() );
    assertEquals( 1, source.getVersions().size() );
    checkEquinoxVersion( source );
  }

  private void checkEquinoxVersion( Source source ) {
    SourceVersion version = source.getVersions().get( 0 );
    // assertEquals( "org.eclipse.equinox.feature.group", source1.getId() );
    assertEquals( "3.8.1", version.getVersion() );
    assertEquals( "http://foo.bar3", version.getRepositoryUrl() );
    List<Feature> features = version.getFeatures();
    assertTrue( 2 == features.size() );
    checkFeature( "org.eclipse.rap.feature.group", "2.5.0", features.get( 0 ) );
    checkFeature( "org.eclipse.rap.feature.group.ext", "1.5.0", features.get( 1 ) );
  }

  private InputStream readExampleSources() throws IOException {
    return this.getClass().getResourceAsStream( "example-sources.json" );
  }
}

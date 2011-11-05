/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.rtp.configurator.service.provider.internal.util.Fixture;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;

public class ConfiguratorServiceSearchTest {

  private SourceProvider provider;
  private RTPDefaultService configuratorService;

  @Before
  public void setUp() throws IOException {
    configuratorService = new RTPDefaultService();
    configuratorService.setUp();
    InputStream stream = Fixture.readExampleSources();
    provider = Fixture.getSourceProvider( stream );
    ModelUtil.setSourceProvider( provider );
  }

  @Test
  public void testList() throws CoreException {
    List<String> sourcesExpected = getSortedTestData( true );
    List<String> list = configuratorService.list();
    assertTrue( Arrays.equals( sourcesExpected.toArray(), list.toArray() ) );
  }

  @Test
  public void testShow() throws CoreException {
    List<String> testData = getSortedTestData( false );
    ArrayList<String> expectedList = new ArrayList<String>();
    expectedList.add( testData.get( 0 ) );
    expectedList.add( testData.get( 1 ) );
    List<String> showParameters = new ArrayList<String>();
    showParameters.add( "equinox" );
    List<String> list = configuratorService.show( showParameters );
    assertListsEquals( expectedList, list );
  }

  @Test
  public void testShowManyParameters() throws CoreException {
    List<String> testData = getSortedTestData( false );
    ArrayList<String> expectedList = new ArrayList<String>();
    expectedList.add( testData.get( 0 ) );
    expectedList.add( testData.get( 1 ) );
    List<String> showParameters = new ArrayList<String>();
    showParameters.add( "equinox" );
    List<String> list = configuratorService.show( showParameters );
    assertListsEquals( expectedList, list );
  }

  @Test
  public void testSearch() throws CoreException {
    List<String> testData = getSortedTestData( false );
    ArrayList<String> expectedList = new ArrayList<String>();
    expectedList.add( testData.get( 2 ) );
    expectedList.add( testData.get( 3 ) );
    expectedList.add( testData.get( 4 ) );
    List<String> searchParameters = new ArrayList<String>();
    searchParameters.add( "ra" );
    List<String> list = configuratorService.search( searchParameters );
    assertListsEquals( expectedList, list );
  }

  private void assertListsEquals( List<String> expectedList, List<String> list2 ) {
    for( int i = 0; i < expectedList.size(); i++ ) {
      assertEquals( expectedList.get( i ), list2.get( i ) );
    }
  }

  private List<String> getSortedTestData( boolean addInstallInfo ) {
    return configuratorService.sourcesToString( provider.getSources(), addInstallInfo );
  }
}

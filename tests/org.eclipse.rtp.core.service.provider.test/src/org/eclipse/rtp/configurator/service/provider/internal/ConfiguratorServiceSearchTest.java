/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.rtp.configurator.service.provider.internal.util.Fixture;
import org.eclipse.rtp.core.model.Source;
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
    InputStream stream = Fixture.readExampleSources();
    provider = Fixture.getSourceProvider( stream );
    ModelUtil.setSourceProvider( provider );
  }

  @Test
  public void testList() throws CoreException {
    List<String> listExpected = getSortedTestData();
    List<String> list = configuratorService.list();
    assertTrue( Arrays.equals( listExpected.toArray(), list.toArray() ) );
  }

  @Test
  public void testShow() throws CoreException {
    List<String> listExpected = getSortedTestData();
    List<String> showParameters = new ArrayList<String>();
    showParameters.add( "equinox" );
    List<String> list = configuratorService.show( showParameters );
    assertTrue( Arrays.equals( new String[]{
      listExpected.get( 0 )
    }, list.toArray() ) );
  }

  @Test
  public void testShowManyParameters() throws CoreException {
    List<String> listExpected = getSortedTestData();
    List<String> showParameters = new ArrayList<String>();
    showParameters.add( "equinox" );
    List<String> list = configuratorService.show( showParameters );
    assertTrue( Arrays.equals( new String[]{
      listExpected.get( 0 )
    }, list.toArray() ) );
  }

  @Test
  public void testSearch() throws CoreException {
    List<String> listExpected = getSortedTestData();
    List<String> searchParameters = new ArrayList<String>();
    searchParameters.add( "ra" );
    List<String> list = configuratorService.search( searchParameters );
    assertTrue( Arrays.equals( new String[]{
      listExpected.get( 1 )
    }, list.toArray() ) );
  }

  private List<String> getSortedTestData() {
    List<String> listExpected = new ArrayList<String>();
    for( Source source : provider.getSources() ) {
      listExpected.add( source.toString() );
    }
    Collections.sort( listExpected );
    return listExpected;
  }
}

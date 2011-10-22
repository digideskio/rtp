/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureInstallException;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureManager;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.RepositoryManager;
import org.eclipse.rtp.configurator.service.provider.internal.util.Fixture;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Version;

public class ConfiguratorServiceTest {

  private RTPDefaultService configuratorService;
  private P2Util p2UtilMock;
  private SourceProvider sourceProvider;
  private FeatureManager featureManager;
  private RepositoryManager repositoryManager;

  @Before
  public void setUp() throws IOException {
    configuratorService = new RTPDefaultService();
    p2UtilMock = mock( P2Util.class );
    configuratorService.setP2Util( p2UtilMock );
    InputStream inputStream = Fixture.readExampleSources();
    sourceProvider = Fixture.getSourceProvider( inputStream );
    ModelUtil.setSourceProvider( sourceProvider );
  }

  @Test
  public void testInstall() throws CoreException, FeatureInstallException, URISyntaxException {
    featureManager = mock( FeatureManager.class );
    repositoryManager = mock( RepositoryManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    when( p2UtilMock.getRepositoryManager() ).thenReturn( repositoryManager );
    SourceVersion sourceVersion = sourceProvider.getSources().get( 0 ).getVersions().get( 0 );
    IStatus status = configuratorService.install( sourceVersion );
    verify( featureManager, atLeastOnce() ).installFeature( sourceVersion );
    verify( repositoryManager, atLeastOnce() ).addRepository( new URI( "http://foo.bar" ) );
    assertTrue( status.isOK() );
  }

  @Test
  public void testInstallNoVersion()
    throws CoreException, FeatureInstallException, URISyntaxException
  {
    featureManager = mock( FeatureManager.class );
    repositoryManager = mock( RepositoryManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    when( p2UtilMock.getRepositoryManager() ).thenReturn( repositoryManager );
    List<SourceVersion> versions = sourceProvider.getSources().get( 0 ).getVersions();
    Collections.sort( versions, getSourceVersionComparator() );
    SourceVersion latestSourceVersion = versions.get( 0 );
    IStatus status = configuratorService.install( latestSourceVersion );
    verify( featureManager, atLeastOnce() ).installFeature( latestSourceVersion );
    verify( repositoryManager, atLeastOnce() ).addRepository( new URI( "http://foo.bar2" ) );
    assertTrue( status.isOK() );
  }

  @Test
  public void testNothingToInstall()
    throws CoreException, FeatureInstallException, URISyntaxException
  {
    SourceVersion sourceVersion = new SourceVersion( "1.6",
                                                     "http://test.com",
                                                     "",
                                                     "http://test.com" );
    IStatus status = configuratorService.install( sourceVersion );
    assertFalse( status.isOK() );
  }

  @Test
  public void testRemove() throws CoreException, FeatureInstallException, URISyntaxException {
    SourceVersion sourceVersion = sourceProvider.getSources().get( 0 ).getVersions().get( 0 );
    List<SourceVersion> souceVersionToUninstall = new ArrayList<SourceVersion>();
    souceVersionToUninstall.add( sourceVersion );
    featureManager = mock( FeatureManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    IStatus status = configuratorService.remove( souceVersionToUninstall );
    verify( featureManager, atLeastOnce() ).uninstallFeature( sourceVersion );
    assertTrue( status.isOK() );
  }

  @Test
  public void testNothingToRemove()
    throws CoreException, FeatureInstallException, URISyntaxException
  {
    featureManager = mock( FeatureManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    SourceVersion sourceVersion = new SourceVersion( "1.6",
                                                     "http://test.com",
                                                     "",
                                                     "http://test.com" );
    List<SourceVersion> sourceVersionsToUninstall = new ArrayList<SourceVersion>();
    sourceVersionsToUninstall.add( sourceVersion );
    IStatus status = configuratorService.remove( sourceVersionsToUninstall );
    assertTrue( status.isOK() );
  }

  @Test
  public void testUpdateWorldUpdateAvailable()
    throws CoreException, FeatureInstallException, URISyntaxException
  {
    List<SourceVersion> sourceVersions = sourceProvider.getSources().get( 0 ).getVersions();
    Collections.sort( sourceVersions, getSourceVersionComparator() );
    SourceVersion latestSourceVersion = sourceVersions.get( 0 );
    featureManager = mock( FeatureManager.class );
    repositoryManager = mock( RepositoryManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    when( p2UtilMock.getRepositoryManager() ).thenReturn( repositoryManager );
    IStatus status = configuratorService.updateWorld();
    verify( featureManager, atLeastOnce() ).uninstallFeature( latestSourceVersion );
    verify( repositoryManager, atLeastOnce() ).addRepository( new URI( latestSourceVersion.getRepositoryUrl() ) );
    assertTrue( status.isOK() );
  }

  @Test
  public void testUpdateWorldNoUpdateAvailable()
    throws CoreException, FeatureInstallException, URISyntaxException
  {
    List<SourceVersion> sourceVersions = sourceProvider.getSources().get( 0 ).getVersions();
    Collections.sort( sourceVersions, getSourceVersionComparator() );
    SourceVersion latestSourceVersion = sourceVersions.get( 0 );
    featureManager = mock( FeatureManager.class );
    repositoryManager = mock( RepositoryManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    when( p2UtilMock.getRepositoryManager() ).thenReturn( repositoryManager );
    when( featureManager.isInstalled( latestSourceVersion ) ).thenReturn( true );
    IStatus status = configuratorService.updateWorld();
    verify( featureManager, never() ).uninstallFeature( latestSourceVersion );
    verify( repositoryManager, never() ).addRepository( new URI( latestSourceVersion.getRepositoryUrl() ) );
    assertTrue( status.isOK() );
  }

  @Test
  public void testUpdateAvailable()
    throws CoreException, FeatureInstallException, URISyntaxException
  {
    List<String> iusToUpdate = new ArrayList<String>();
    iusToUpdate.add( "rap" );
    List<SourceVersion> sourceVersions = sourceProvider.getSources().get( 0 ).getVersions();
    Collections.sort( sourceVersions, getSourceVersionComparator() );
    SourceVersion latestSourceVersion = sourceVersions.get( 0 );
    featureManager = mock( FeatureManager.class );
    repositoryManager = mock( RepositoryManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    when( p2UtilMock.getRepositoryManager() ).thenReturn( repositoryManager );
    IStatus status = configuratorService.update( iusToUpdate );
    verify( featureManager, atLeastOnce() ).uninstallFeature( latestSourceVersion );
    verify( repositoryManager, atLeastOnce() ).addRepository( new URI( latestSourceVersion.getRepositoryUrl() ) );
    assertTrue( status.isOK() );
  }

  @Test
  public void testUpdateNoUpdateAvailable()
    throws CoreException, FeatureInstallException, URISyntaxException
  {
    List<String> iusToUpdate = new ArrayList<String>();
    iusToUpdate.add( "rap" );
    List<SourceVersion> sourceVersions = sourceProvider.getSources().get( 0 ).getVersions();
    Collections.sort( sourceVersions, getSourceVersionComparator() );
    SourceVersion latestSourceVersion = sourceVersions.get( 0 );
    featureManager = mock( FeatureManager.class );
    repositoryManager = mock( RepositoryManager.class );
    when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
    when( p2UtilMock.getRepositoryManager() ).thenReturn( repositoryManager );
    when( featureManager.isInstalled( latestSourceVersion ) ).thenReturn( true );
    IStatus status = configuratorService.update( iusToUpdate );
    verify( featureManager, never() ).uninstallFeature( latestSourceVersion );
    verify( repositoryManager, never() ).addRepository( new URI( latestSourceVersion.getRepositoryUrl() ) );
    assertTrue( status.isOK() );
  }

  private Comparator<SourceVersion> getSourceVersionComparator() {
    Comparator<SourceVersion> comparator = new Comparator<SourceVersion>() {

      @Override
      public int compare( SourceVersion arg0, SourceVersion arg1 ) {
        String version = arg0.getVersion();
        String version2 = arg1.getVersion();
        return new Version( version2 ).compareTo( new Version( version ) );
      }
    };
    return comparator;
  }
}

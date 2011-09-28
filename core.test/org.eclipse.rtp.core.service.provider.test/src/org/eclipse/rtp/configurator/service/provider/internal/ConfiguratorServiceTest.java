/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.rtp.configurator.model.SourceProvider;
import org.eclipse.rtp.configurator.model.SourceVersion;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureInstallException;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureManager;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.RepositoryManager;
import org.eclipse.rtp.configurator.service.provider.internal.util.ConfiguratorModelUtil;
import org.eclipse.rtp.configurator.service.provider.internal.util.Fixture;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;
import org.junit.Before;
import org.junit.Test;

public class ConfiguratorServiceTest {

	private DefaultConfiguratorService configuratorService;
	private P2Util p2UtilMock;
  private SourceProvider sourceProvider;
  private FeatureManager featureManager;
  private RepositoryManager repositoryManager;

	@Before
	public void setUp() throws IOException {
		configuratorService = new DefaultConfiguratorService();
		p2UtilMock = mock(P2Util.class);
		configuratorService.setP2Util(p2UtilMock);
		InputStream inputStream = Fixture.readExampleSources();
		sourceProvider = Fixture.getSourceProvider( inputStream );
	    ConfiguratorModelUtil.setSourceProvider( sourceProvider );
	}

	
	@Test
	public void testInstall() throws CoreException, FeatureInstallException, URISyntaxException {
	  featureManager = mock( FeatureManager.class );
	  repositoryManager = mock( RepositoryManager.class );
      when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
      when( p2UtilMock.getRepositoryManager() ).thenReturn( repositoryManager );
      SourceVersion sourceVersion = sourceProvider.getSources().get( 0 ).getVersions().get( 0 );
      List<String> parameters = new ArrayList<String>();
      parameters.add( "rap" );
      parameters.add( "1.4" );
      IStatus status = configuratorService.install( parameters );
      
      verify( featureManager, atLeastOnce() ).installFeature( sourceVersion );
      verify( repositoryManager, atLeastOnce() ).addRepository( new URI("http://foo.bar") );
      assertTrue( status.isOK() );
	}
	
	@Test
	public void testNothingToInstall() throws CoreException, FeatureInstallException, URISyntaxException {
	  List<String> parameters = new ArrayList<String>();
	  parameters.add( "rap" );
	  parameters.add( "1.6" );
	  IStatus status = configuratorService.install( parameters );
	  
	  assertFalse( status.isOK() );
	}
	
	@Test
	public void testRemove() throws CoreException, FeatureInstallException, URISyntaxException {
	  SourceVersion sourceVersion = sourceProvider.getSources().get( 0 ).getVersions().get( 0 );
	  featureManager = mock( FeatureManager.class );
	  when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
	  when(featureManager.isInstalled( "org.eclipse.rap.feature.group", "1.4" )).thenReturn( true );
	  
	  List<String> parameters = new ArrayList<String>();
	  parameters.add( "rap" );
	  IStatus status = configuratorService.remove( parameters );
	  
	  verify( featureManager, atLeastOnce() ).uninstallFeature( sourceVersion );
	  assertTrue( status.isOK() );
	}
	
	@Test
	public void testNothingToRemove() throws CoreException, FeatureInstallException, URISyntaxException {
	  featureManager = mock( FeatureManager.class );
	  when( p2UtilMock.getFeatureManager() ).thenReturn( featureManager );
	  
	  List<String> parameters = new ArrayList<String>();
	  parameters.add( "rap" );
	  IStatus status = configuratorService.remove( parameters );
	  
	  assertTrue( status.isOK() );
	}
}

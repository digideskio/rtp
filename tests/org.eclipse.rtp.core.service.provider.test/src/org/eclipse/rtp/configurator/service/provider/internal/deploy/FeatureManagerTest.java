/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 * 
 * Contributors:
 *     EclipseSource - initial API and implementation
 *     SAP AG - fix for 380700

 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal.deploy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.internal.provisional.configurator.Configurator;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.planner.IPlanner;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.rtp.core.model.SourceVersion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings( "restriction" )
public class FeatureManagerTest {

  @Mock
  IProvisioningAgent provisioningAgent;
  @Mock
  RepositoryManager repositoryManager;
  @Mock
  Configurator configurator;
  @Mock
  IMetadataRepositoryManager metadataRepositoryMock;
  @Mock
  IProfileRegistry profileRegistry;
  @Mock
  IProfile profile;
  @Mock
  IPlanner planner;
  @Mock
  IProvisioningPlan provisioningPlan;
  FeatureManager objectUnderTest;
  static final String FEATURE_NAME = "testFeature";
  static final String FEATURE_VERSION = "1.0.0";

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks( this );
    this.objectUnderTest = new FeatureManager( provisioningAgent, repositoryManager, configurator );
    when( repositoryManager.getRepositories() ).thenReturn( null );
    when( provisioningAgent.getService( IMetadataRepositoryManager.SERVICE_NAME ) ).thenReturn( metadataRepositoryMock );
    when( metadataRepositoryMock.getKnownRepositories( IRepositoryManager.REPOSITORIES_ALL ) ).thenReturn( new URI[]{} );
    when( provisioningAgent.getService( IProfileRegistry.SERVICE_NAME ) ).thenReturn( profileRegistry );
    when( profileRegistry.getProfile( IProfileRegistry.SELF ) ).thenReturn( profile );
    when( provisioningAgent.getService( IPlanner.SERVICE_NAME ) ).thenReturn( planner );
  }

  @Test
  public void testInstallUnknownFeature() throws FeatureInstallException {
    when( provisioningPlan.getStatus() ).thenReturn( new Status( Status.ERROR,
                                                                 FEATURE_NAME,
                                                                 "some error message" ) );
    when( planner.getProvisioningPlan( any( IProfileChangeRequest.class ),
                                       any( ProvisioningContext.class ),
                                       any( SubMonitor.class ) ) ).thenReturn( provisioningPlan );
    try {
      this.objectUnderTest.installFeature( new SourceVersion( FEATURE_VERSION, "", "", "" ) );
      fail();
    } catch( FeatureInstallException e ) {
      assertEquals( "Cannot complete the request.  See the error log for details.\n",
                    e.getMessage() );
    }
    verifyZeroInteractions( configurator );
  }

  @Test
  public void testUninstallUnknownFeature() throws FeatureInstallException {
    when( provisioningPlan.getStatus() ).thenReturn( new Status( Status.ERROR,
                                                                 FEATURE_NAME,
                                                                 "some error message" ) );
    when( planner.getProvisioningPlan( any( IProfileChangeRequest.class ),
                                       any( ProvisioningContext.class ),
                                       any( SubMonitor.class ) ) ).thenReturn( provisioningPlan );
    try {
      this.objectUnderTest.uninstallFeature( new SourceVersion( FEATURE_VERSION, "", "", "" ) );
      fail();
    } catch( FeatureInstallException e ) {
      assertEquals( "Cannot complete the request.  See the error log for details.\n",
                    e.getMessage() );
    }
    verifyZeroInteractions( configurator );
  }

  @Test
  public void testGenerateErrorMessage() {
    IStatus[] children = new IStatus[ 2 ];
    children[ 0 ] = new Status( 0, "test.id", "child 0" );
    children[ 1 ] = new Status( 0, "test.id", "child 1" );
    MultiStatus status = new MultiStatus( "test.id", 0, children, "multi status", null );
    StringBuilder errorMessage = new StringBuilder();
    objectUnderTest.generateErrorMessage( status, errorMessage );
    assertEquals( "multi status\nchild 0\nchild 1\n", errorMessage.toString() );
  }
  
  @Test
  public void testGenerateErrorMessageNull() {
    IStatus status = new Status( 0, "test.id", null );
    StringBuilder errorMessage = new StringBuilder();
    objectUnderTest .generateErrorMessage( status, errorMessage );
    assertEquals( "\n", errorMessage.toString() );
  }
}

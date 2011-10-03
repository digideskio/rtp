/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal.deploy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.internal.provisional.configurator.Configurator;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileChangeOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UninstallOperation;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.rtp.configurator.model.Feature;
import org.eclipse.rtp.configurator.model.SourceVersion;

@SuppressWarnings( "restriction" )
public class FeatureManager {

  public enum Action {
    UNINSTALL, INSTALL
  }
  private final IProvisioningAgent provisioningAgent;
  private final RepositoryManager repositoryManager;
  private final Configurator configurator;

  public FeatureManager( IProvisioningAgent provisioningAgent,
                         RepositoryManager repositoryManager,
                         Configurator configurator )
  {
    this.provisioningAgent = provisioningAgent;
    this.repositoryManager = repositoryManager;
    this.configurator = configurator;
  }

  // TODO: Not tested
  public void installFeature( SourceVersion sourceVersion ) throws FeatureInstallException {
    ProfileChangeOperation operation = resolveProfileChangeOperation( sourceVersion, Action.INSTALL );
    executeProfileChangeOperation( operation );
    applyChanges();
  }

  // TODO: Not tested
  public void uninstallFeature( SourceVersion sourceVersion ) throws FeatureInstallException {
    ProfileChangeOperation operation = resolveProfileChangeOperation( sourceVersion,
                                                                      Action.UNINSTALL );
    executeProfileChangeOperation( operation );
    applyChanges();
  }

  // TODO: Not tested
  private ProfileChangeOperation resolveProfileChangeOperation( SourceVersion sourceVersion,
                                                                Action action )
    throws FeatureInstallException
  {
    ProvisioningSession session = new ProvisioningSession( provisioningAgent );
    ProvisioningContext context = createProvisioningContext();
    List<Feature> features = sourceVersion.getFeatures();
    List<IInstallableUnit> unitsToInstall = new ArrayList<IInstallableUnit>();
    for( Feature feature : features ) {
      Collection<IInstallableUnit> units = getInstallableUnits( context,
                                                                feature.getId(),
                                                                feature.getVersion(),
                                                                action );
      unitsToInstall.addAll( units );
    }
    ProfileChangeOperation operation = getOperation( session, unitsToInstall, action );
    operation.setProvisioningContext( context );
    resolveOperation( operation );
    return operation;
  }

  private ProfileChangeOperation getOperation( ProvisioningSession session,
                                               Collection<IInstallableUnit> units,
                                               Action action ) throws FeatureInstallException
  {
    ProfileChangeOperation operation;
    if( action.equals( Action.UNINSTALL ) ) {
      operation = new UninstallOperation( session, units );
    } else {
      operation = new InstallOperation( session, units );
    }
    return operation;
  }

  private void resolveOperation( ProfileChangeOperation operation ) throws FeatureInstallException {
    IStatus result = operation.resolveModal( null );
    if( !result.isOK() ) {
      String errorMessage = generateErrorMessage( result );
      throw new FeatureInstallException( errorMessage );
    }
  }

  private void applyChanges() throws FeatureInstallException {
    try {
      configurator.applyConfiguration();
    } catch( IOException e ) {
      throw new FeatureInstallException( e );
    }
  }

  protected ProvisioningContext createProvisioningContext() {
    ProvisioningContext context = new ProvisioningContext( provisioningAgent );
    context.setArtifactRepositories( repositoryManager.getRepositories() );
    context.setArtifactRepositories( repositoryManager.getRepositories() );
    return context;
  }

  protected void executeProfileChangeOperation( ProfileChangeOperation operation )
    throws FeatureInstallException
  {
    ProvisioningJob provisioningJob = operation.getProvisioningJob( new NullProgressMonitor() );
    if( provisioningJob == null ) {
      return;
    }
    IStatus result = provisioningJob.runModal( new NullProgressMonitor() );
    if( !result.isOK() ) {
      throw new FeatureInstallException( result.getMessage() );
    }
  }

  private String generateErrorMessage( IStatus result ) {
    IStatus[] children = result.getChildren();
    String message = "";
    for( int i = 0; i < children.length; i++ ) {
      message = children[ i ].getMessage() + "\n";
    }
    return message;
  }

  protected Collection<IInstallableUnit> getInstallableUnits( ProvisioningContext context,
                                                              String id,
                                                              String version,
                                                              Action action )
  {
    IQueryable<IInstallableUnit> queryable = action == Action.INSTALL
                                                                     ? context.getMetadata( new NullProgressMonitor() )
                                                                     : getProfile();
    Collection<IInstallableUnit> toInstall = queryable.query( QueryUtil.createIUQuery( id,
                                                                                       Version.create( version ) ),
                                                              null )
      .toUnmodifiableSet();
    return toInstall;
  }

  public IProfile getProfile() {
    IProfileRegistry profileRegistry = ( IProfileRegistry )provisioningAgent.getService( IProfileRegistry.SERVICE_NAME );
    return profileRegistry.getProfile( IProfileRegistry.SELF );
  }

  public boolean isInstalled( SourceVersion sourceVersion ) {
    List<IInstallableUnit> result = new ArrayList<IInstallableUnit>();
    IProfile profile = getProfile();
    for( Feature feature : sourceVersion.getFeatures() ) {
      IQueryResult<IInstallableUnit> query = profile.query( QueryUtil.createIUQuery( feature.getId(),
                                                                                     Version.create( feature.getVersion() ) ),
                                                            new NullProgressMonitor() );
      result.addAll( query.toSet() );
    }
    return result.size() > 0;
  }
}

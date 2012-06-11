/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 * 
 * Contributors:
 *     EclipseSource - initial API and implementation
 *     SAP AG - fix for bug 382106 
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureInstallException;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureManager;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.RepositoryManager;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;
import org.eclipse.rtp.core.RuntimeProvisioningService;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;

public class RuntimeProvisioningServiceImpl implements RuntimeProvisioningService {

  private P2Util p2Util;

  public void setUp() {
    setP2Util( new P2Util() );
  }

  public void shutDown() {
    setP2Util( null );
  }

  public void setP2Util( P2Util p2Util ) {
    this.p2Util = p2Util;
  }

  @Override
  public IStatus install( SourceVersion sourceVersion ) {
    IStatus result = null;
    try {
      if( sourceVersion != null ) {
        System.out.println( "Loading repository: " + sourceVersion.getRepositoryUrl() );
        loadRepository( sourceVersion );
        System.out.println( "Repository loaded: " + sourceVersion.getRepositoryUrl() );
        System.out.println( "Installation started" );
        installVersion( sourceVersion );
        result = Status.OK_STATUS;
      } else {
        result = Status.CANCEL_STATUS;
      }
    } catch( Exception e ) {
      e.printStackTrace();
      result = new Status( IStatus.ERROR,
                           ProviderActivator.BUNDLE_ID,
                           "Failed to isntall features",
                           e );
    }
    return result;
  }

  private void installVersion( SourceVersion sourceVersion ) throws FeatureInstallException {
    FeatureManager featureManager = p2Util.getFeatureManager();
    featureManager.installFeature( sourceVersion );
  }

  private void loadRepository( SourceVersion sourceVersion ) throws URISyntaxException {
    if( sourceVersion.getRepositoryUrl() != null ) {
      RepositoryManager repositoryManager = p2Util.getRepositoryManager();
      repositoryManager.addRepository( new URI( sourceVersion.getRepositoryUrl() ) );
    }
  }

  @Override
  public IStatus remove( List<SourceVersion> sourceVersionsToUnisntall ) {
    FeatureManager featureManager = p2Util.getFeatureManager();
    List<IStatus> errorStatus = uninstall( featureManager, sourceVersionsToUnisntall );
    return errorStatus.isEmpty()
                                ? Status.OK_STATUS
                                : new MultiStatus( ProviderActivator.BUNDLE_ID,
                                                   0,
                                                   errorStatus.toArray( new IStatus[ 0 ] ),
                                                   "Uinstall status",
                                                   null );
  }

  private List<IStatus> uninstall( FeatureManager featureManager,
                                   List<SourceVersion> sourceVersionsToUnisntall )
  {
    List<IStatus> errorStatus = new ArrayList<IStatus>();
    for( SourceVersion sourceVersion : sourceVersionsToUnisntall ) {
      try {
        featureManager.uninstallFeature( sourceVersion );
      } catch( FeatureInstallException e ) {
        e.printStackTrace();
        Status status = new Status( IStatus.ERROR,
                                    ProviderActivator.BUNDLE_ID,
                                    "Failed to uninstall source version: "
                                        + sourceVersion.toString(),
                                    e );
        errorStatus.add( status );
      }
    }
    return errorStatus;
  }

  @Override
  public List<String> search( List<String> anyListOf ) {
    List<Source> result = new ModelUtil().search( anyListOf );
    return sourcesToStringSorted( result, false );
  }

  @Override
  public List<String> show( List<String> anyListOf ) {
    List<String> showSource = new ArrayList<String>();
    showSource.add( anyListOf.get( 0 ) );
    List<Source> result = new ModelUtil().search( showSource );
    return sourcesToStringSorted( result, false );
  }

  @Override
  public List<String> list() throws CoreException {
    List<Source> sources = new ModelUtil().list();
    List<String> sourcesAsString = sourcesToStringSorted( sources, true );
    return sourcesAsString;
  }

  @Override
  public void addRepository( URI repository ) {
    p2Util.getRepositoryManager().addRepository( repository );
  }

  @Override
  public void removeRepository( URI repository ) {
    p2Util.getRepositoryManager().removeRepository( repository );
  }

  @Override
  public IStatus update( List<String> anyListOf ) {
    List<Source> sourceToUpdate = new ModelUtil().search( anyListOf );
    IStatus result = updateSources( sourceToUpdate );
    return result;
  }

  @Override
  public URI[] getRepositories() {
    return p2Util.getRepositoryManager().getRepositories();
  }

  @Override
  public IStatus updateWorld() {
    List<Source> sources = ModelUtil.getSourceProvider().getSources();
    IStatus result = updateSources( sources );
    return result;
  }

  private IStatus updateSources( List<Source> sources ) {
    List<IStatus> updateStatusList = new ArrayList<IStatus>();
    FeatureManager featureManager = p2Util.getFeatureManager();
    for( Source source : sources ) {
      List<SourceVersion> versions = source.getVersions();
      Collections.sort( versions, new ModelUtil().getSourceVersionComparator() );
      SourceVersion latestSourceVersion = versions.get( 0 );
      if( !featureManager.isInstalled( latestSourceVersion ) ) {
        updateStatusList.addAll( uninstall( featureManager, versions ) );
        updateStatusList.add( install( latestSourceVersion ) );
      }
    }
    IStatus result = new MultiStatus( ProviderActivator.BUNDLE_ID,
                                      0,
                                      updateStatusList.toArray( new IStatus[ 0 ] ),
                                      "Update status",
                                      null );
    return result;
  }

  private List<String> sourcesToStringSorted( List<Source> sources, boolean addInstalledInfo ) {
    List<String> sourcesAsString = sourcesToString( sources, addInstalledInfo );
    return sourcesAsString;
  }

  protected List<String> sourcesToString( List<Source> sources, boolean addInstalledInfo ) {
    List<String> sourcesAsString = new ArrayList<String>();
    for( Source source : sources ) {
      sourcesAsString.add( source.toString() );
      List<String> versionsToString = versionsToString( source, addInstalledInfo );
      sourcesAsString.addAll( versionsToString );
    }
    return sourcesAsString;
  }

  private List<String> versionsToString( Source source, boolean addInstalledInfo ) {
    List<SourceVersion> versions = source.getVersions();
    Collections.sort( versions, new ModelUtil().getSourceVersionComparator() );
    List<String> result = new ArrayList<String>();
    for( SourceVersion sourceVersion : versions ) {
      if( addInstalledInfo ) {
        String installedInfo = getInstallInfo( sourceVersion );
        result.add( installedInfo );
      }
      result.add( sourceVersion.toString() );
    }
    return result;
  }

  private String getInstallInfo( SourceVersion sourceVersion ) {
    FeatureManager featureManager = p2Util.getFeatureManager();
    boolean installed = featureManager.isInstalled( sourceVersion );
    String installedInfo = installed
                                    ? "[installed]"
                                    : "[not installed]";
    return installedInfo;
  }
}

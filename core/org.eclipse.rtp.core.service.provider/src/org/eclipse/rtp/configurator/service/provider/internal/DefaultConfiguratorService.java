/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
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
import org.eclipse.rtp.configurator.core.IConfiguratorService;
import org.eclipse.rtp.configurator.model.Source;
import org.eclipse.rtp.configurator.model.SourceVersion;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureInstallException;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureManager;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.RepositoryManager;
import org.eclipse.rtp.configurator.service.provider.internal.util.ConfiguratorModelUtil;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;

public class DefaultConfiguratorService implements IConfiguratorService {

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
  public IStatus install( List<String> parameter ) {
    IStatus result = null;
    try {
      SourceVersion sourceVersion = getSourceVersions( parameter );
      if( sourceVersion != null ) {
        loadRepository( sourceVersion );
        install( sourceVersion );
        result = Status.OK_STATUS;
      } else {
        System.out.println( "No source found to install" );
        result = Status.CANCEL_STATUS;
      }
    } catch( Exception e ) {
      System.out.println( "Feature will not be installed. Failed to load repository" );
      result = new Status( IStatus.ERROR,
                           ProviderActivator.BUNDLE_ID,
                           "Failed to isntall features",
                           e );
    }
    return result;
  }

  private SourceVersion getSourceVersions( List<String> parameter ) {
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<String> sourceName = new ArrayList<String>();
    sourceName.add( parameter.get( 0 ) );
    List<Source> searchSources = searchSources( sourceName, sources );
    SourceVersion sourceVersion = searchSourceVerions( parameter.get( 1 ), searchSources );
    return sourceVersion;
  }

  private void install( SourceVersion sourceVersion ) throws FeatureInstallException {
    FeatureManager featureManager = p2Util.getFeatureManager();
    featureManager.installFeature( sourceVersion );
  }

  private void loadRepository( SourceVersion sourceVersion ) throws URISyntaxException {
    RepositoryManager repositoryManager = p2Util.getRepositoryManager();
    repositoryManager.addRepository( new URI( sourceVersion.getRepositoryUrl() ) );
  }

  private SourceVersion searchSourceVerions( String sourceVersion, List<Source> sources ) {
    SourceVersion result = null;
    for( Source source : sources ) {
      List<SourceVersion> versions = source.getVersions();
      for( SourceVersion version : versions ) {
        if( version.getVersion().equals( sourceVersion ) ) {
          result = version;
        }
      }
    }
    return result;
  }

  @Override
  public IStatus update( List<String> anyListOf ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStatus remove( List<String> anyListOf ) {
    FeatureManager featureManager = p2Util.getFeatureManager();
    List<SourceVersion> sourceVersionsToUnisntall = getSourceVersionsToUninstall( anyListOf,
                                                                                  featureManager );
    List<IStatus> errorStatus = uninstall( featureManager, sourceVersionsToUnisntall );
    return errorStatus.isEmpty()
                                ? Status.OK_STATUS
                                : new MultiStatus( ProviderActivator.BUNDLE_ID,
                                                   0,
                                                   errorStatus.toArray( new IStatus[ 0 ] ),
                                                   "Uinstall status",
                                                   null );
  }

  private List<SourceVersion> getSourceVersionsToUninstall( List<String> anyListOf,
                                                            FeatureManager featureManager )
  {
    List<SourceVersion> result = new ArrayList<SourceVersion>();
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<Source> sourceToUinstall = searchSources( anyListOf, sources );
    for( Source source : sourceToUinstall ) {
      result.addAll( source.getVersions() );
    }
    return result;
  }

  private List<IStatus> uninstall( FeatureManager featureManager,
                                   List<SourceVersion> sourceVersionsToUnisntall )
  {
    List<IStatus> errorStatus = new ArrayList<IStatus>();
    for( SourceVersion sourceVersion : sourceVersionsToUnisntall ) {
      try {
        featureManager.uninstallFeature( sourceVersion );
      } catch( FeatureInstallException e ) {
        System.out.println( "Failed to uninstall source version: " + sourceVersion.toString() );
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
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<Source> result = searchSources( anyListOf, sources );
    return getSortedSources( result );
  }

  @Override
  public List<String> show( List<String> anyListOf ) {
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<String> showSource = new ArrayList<String>();
    showSource.add( anyListOf.get( 0 ) );
    List<Source> result = searchSources( showSource, sources );
    return getSortedSources( result );
  }

  @Override
  public List<String> list() throws CoreException {
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<String> sourcesAsString = getSortedSources( sources );
    return sourcesAsString;
  }

  private List<Source> searchSources( List<String> anyListOf, List<Source> sources ) {
    List<Source> result = new ArrayList<Source>();
    for( Source source : sources ) {
      String name = source.getName();
      for( String term : anyListOf ) {
        if( name.contains( term ) ) {
          result.add( source );
        }
      }
    }
    return result;
  }

  private List<String> getSortedSources( List<Source> sources ) {
    List<String> sourcesAsString = new ArrayList<String>();
    for( Source source : sources ) {
      sourcesAsString.add( source.toString() );
    }
    Collections.sort( sourcesAsString );
    return sourcesAsString;
  }

  @Override
  public IStatus updateWorld() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStatus refresh() {
    // TODO Auto-generated method stub
    return null;
  }
}

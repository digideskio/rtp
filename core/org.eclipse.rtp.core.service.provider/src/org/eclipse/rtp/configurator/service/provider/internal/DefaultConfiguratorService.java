/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.rtp.configurator.core.IConfiguratorService;
import org.eclipse.rtp.configurator.model.Source;
import org.eclipse.rtp.configurator.model.SourceProvider;
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStatus update( List<String> anyListOf ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStatus remove( List<String> anyListOf ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> search( List<String> anyListOf ) {
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<String> result = new ArrayList<String>();
    for( Source source : sources ) {
      String name = source.getName();
      for( String term : anyListOf ) {
        if( name.contains( term ) ) {
          result.add( source.toString() );
        }
      }
    }
    Collections.sort( result );
    return result;
  }

  @Override
  public List<String> show( List<String> anyListOf ) {
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<String> result = new ArrayList<String>();
    String showName = anyListOf.get( 0 );
    for( Source source : sources ) {
      if( source.getName().contains( showName ) ) {
          result.add( source.toString() );
      }
    }
    Collections.sort( result );
    return result;
  }

  @Override
  public List<String> list() throws CoreException {
    List<Source> sources = ConfiguratorModelUtil.getSourceProvider().getSources();
    List<String> sourcesAsString = getSourtedSources( sources );
    return sourcesAsString;
  }

  private List<String> getSourtedSources( List<Source> sources ) {
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

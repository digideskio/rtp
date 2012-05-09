/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.rest.provider.internal;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.rtp.configurator.rest.provider.RestProviderActivator;
import org.eclipse.rtp.configurator.rest.provider.internal.util.PathInfoUtil;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;

@SuppressWarnings( "restriction" )
public class GetRequestHandler {

  public String handleRequest( HttpServletRequest request ) throws CoreException {
    String result = null;
    String pathInfo = request.getPathInfo();
    String[] pathSegments = new PathInfoUtil().splitPathInfo( pathInfo );
    if( pathInfo == null || pathInfo.length() == 0 ) {
      result = "";
    } else if( "list".equalsIgnoreCase( pathSegments[ 0 ] ) ) {
      result = handleListRequest( pathSegments );
    } else if( "show".equalsIgnoreCase( pathSegments[ 0 ] ) ) {
      result = handleShowRequest( pathInfo, request.getParameter( "query" ) );
    } else if( "search".equalsIgnoreCase( pathSegments[ 0 ] ) ) {
      result = handleSearchRequest( pathInfo, request.getParameter( "query" ) );
    } else {
      result = "Unsupported";
    }
    return result;
  }

  private String handleListRequest( String[] pathSegments ) throws CoreException {
    List<Source> sources = getModelUtil().list();
    if( pathSegments.length == 2 && "installed".equalsIgnoreCase( pathSegments[ 1 ] ) ) {
      sources = filterUninstalledSourceVersions( sources );
    } else if( pathSegments.length == 2 && "uninstalled".equalsIgnoreCase( pathSegments[ 1 ] ) ) {
      sources = filterInstalledSourceVersions( sources );
    }
    String result = sourcesToString( sources );
    return result;
  }

  private List<Source> filterUninstalledSourceVersions( List<Source> sources ) {
    List<Source> result = new ArrayList<Source>();
    P2Util p2Util = getP2Util();
    for( Source source : sources ) {
      Source sourceToAdd = new Source( source.getName(),
                                       source.getDescription(),
                                       source.getInfoUrl() );
      filterUninstalled( p2Util, source.getVersions(), sourceToAdd );
      if( sourceToAdd.getVersions().size() > 0 ) {
        result.add( sourceToAdd );
      }
    }
    return result;
  }

  private List<Source> filterInstalledSourceVersions( List<Source> sources ) {
    List<Source> result = new ArrayList<Source>();
    P2Util p2Util = getP2Util();
    for( Source source : sources ) {
      Source sourceToAdd = new Source( source.getName(),
                                       source.getDescription(),
                                       source.getInfoUrl() );
      filterInstalled( p2Util, source.getVersions(), sourceToAdd );
      if( sourceToAdd.getVersions().size() > 0 ) {
        result.add( sourceToAdd );
      }
    }
    return result;
  }

  private void filterUninstalled( P2Util p2Util, List<SourceVersion> versions, Source sourceToAdd )
  {
    for( SourceVersion sourceVersion : versions ) {
      if( p2Util.isSourceVersionInstalled( sourceVersion ) ) {
        sourceToAdd.addVersion( sourceVersion );
      }
    }
  }

  private void filterInstalled( P2Util p2Util, List<SourceVersion> versions, Source sourceToAdd ) {
    for( SourceVersion sourceVersion : versions ) {
      if( !p2Util.isSourceVersionInstalled( sourceVersion ) ) {
        sourceToAdd.addVersion( sourceVersion );
      }
    }
  }

  private String handleShowRequest( String pathInfo, String query ) {
    String result = null;
    if( query == null || query.length() == 0 ) {
      result = "";
    } else {
      List<String> queryList = Arrays.asList( new String[]{
        query
      } );
      List<Source> sources = getModelUtil().search( queryList );
      result = sourcesToString( sources );
    }
    return result;
  }

  private String handleSearchRequest( String pathInfo, String query ) {
    String result = null;
    if( query == null || query.length() == 0 ) {
      result = "";
    } else {
      List<String> queryList = Arrays.asList( new String[]{
        query
      } );
      List<Source> sources = getModelUtil().search( queryList );
      result = sourcesToString( sources );
    }
    return result;
  }

  private String sourcesToString( List<Source> sources ) {
    SourceUnMarshaller marshalService = getMarshalService();
    String result = marshalService.unmarshal( sources );
    return result;
  }

  protected ModelUtil getModelUtil() {
    return new ModelUtil();
  }

  protected P2Util getP2Util() {
    return new P2Util();
  }

  protected SourceUnMarshaller getMarshalService() {
    RestProviderActivator restProviderActivator = RestProviderActivator.getDefault();
    SourceUnMarshaller sourceUnmarshallerService = restProviderActivator.getMarshalService();
    return sourceUnmarshallerService;
  }
}

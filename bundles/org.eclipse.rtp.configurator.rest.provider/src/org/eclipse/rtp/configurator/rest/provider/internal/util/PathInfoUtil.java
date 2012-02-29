/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.rest.provider.internal.util;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;

public class PathInfoUtil {

  public SourceVersion getSourceVersion( String pathInfo, ModelUtil modelUtil, List<Source> sources )
  {
    String sourceName = getSourceName( pathInfo );
    List<Source> searchSources = modelUtil.searchSources( Arrays.asList( new String[]{
      sourceName
    } ), sources );
    String sourceVersion = getSourceVersion( pathInfo );
    SourceVersion searchSourceVerions = modelUtil.searchSourceVerions( sourceVersion, searchSources );
    return searchSourceVerions;
  }

  public String getSourceVersion( String pathInfo ) {
    String result;
    String[] provisioningInfo = getProvisioningInfo( pathInfo );
    if(provisioningInfo.length >= 3){
      result = provisioningInfo[2];
    }else{
      result = "";
    }
    return result;
  }

  public String getSourceName( String pathInfo ) {
    String[] provisioningInfo = getProvisioningInfo( pathInfo );
    return provisioningInfo[ 1 ];
  }

  public boolean isProvisioning( String pathInfo, ModelUtil modelUtil, List<Source> sources ) {
    boolean result = false;
    String[] provisioningInfo = getProvisioningInfo( pathInfo );
    if( provisioningInfo.length != 0 && isProvisionigCommand( provisioningInfo[ 0 ] ) ) {
      List<String> sourceQuery = Arrays.asList( new String[]{
        provisioningInfo[ 1 ]
      } );
      List<Source> searchResult = modelUtil.searchSources( sourceQuery, sources );
      result = !searchResult.isEmpty();
    }
    return result;
  }

  private boolean isProvisionigCommand( String provisioningCommand ) {
    return "install".equalsIgnoreCase( provisioningCommand )
           || "uninstall".equalsIgnoreCase( provisioningCommand );
  }

  public String[] getProvisioningInfo( String pathInfo ) {
    String[] provisioningInfo = new String[ 0 ];
    if( pathInfo != null && pathInfo.length() > 0 ) {
      Path path = new Path( pathInfo );
      if( path.segmentCount() >= 2 ) {
        provisioningInfo = path.segments();
      }
    }
    return provisioningInfo;
  }

  public String[] splitPathInfo( String pathInfo ) {
    String[] result = new String[ 0 ];
    if( pathInfo != null && pathInfo.length() > 0 ) {
      result = new Path( pathInfo ).segments();
    }
    return result;
  }
}

/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rtp.configurator.rest.RestTemplate;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;

public class ComponentsTabContentUtil {

  private List<Source> sources;
  private List<Source> installedSources;
  private RestTemplate restTemplate;
  private String configurationURI;
  private static String[] comboLabels = new String[]{
    "all", "installed", "uninstalled"
  };
  private final Map<String, String> listMapping = new HashMap<String, String>() {

    {
      put( comboLabels[ 0 ], "/rt/list" );
      put( comboLabels[ 1 ], "/rt/list/installed" );
      put( comboLabels[ 2 ], "/rt/list/uninstalled" );
    }
  };

  public void setConfigurationURI( String configurationURI ) {
    this.configurationURI = configurationURI;
  }

  public String[] getComboLabels() {
    return comboLabels;
  }

  public List<Source> getSourcec() {
    return sources;
  }

  public RestTemplate getRestTemplate() {
    return restTemplate;
  }

  public boolean isInstalled( SourceVersion sourceVersion ) {
    boolean result = false;
    for( int i = 0; i < installedSources.size() && result == false; i++ ) {
      Source source = installedSources.get( i );
      // TODO Fix me. This is a hack!
      List<SourceVersion> versions = source.getVersions();
      for( int j = 0; j < versions.size() && result == false; j++ ) {
        result = versions.get( j ).toString().equals( sourceVersion.toString() );
      }
    }
    return result;
  }

  public Source getSourceVersionSource( SourceVersion sourceVersion ) {
    Source result = null;
    if( sources != null ) {
      for( Source source : sources ) {
        List<SourceVersion> versions = source.getVersions();
        for( SourceVersion version : versions ) {
          if( version.equals( sourceVersion ) ) {
            result = source;
          }
        }
      }
    }
    return result;
  }

  public void refresh( String componentsFilter ) {
    restTemplate = new RestTemplate( configurationURI );
    sources = restTemplate.getForEntitiesAsList( listMapping.get( componentsFilter ), Source.class );
    installedSources = restTemplate.getForEntitiesAsList( listMapping.get( comboLabels[ 1 ] ),
                                                          Source.class );
  }
}

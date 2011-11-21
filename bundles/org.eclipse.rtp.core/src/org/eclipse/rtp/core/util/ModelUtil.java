/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.core.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.rtp.core.internal.CoreActivator;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.eclipse.rtp.core.model.SourceVersion;
import org.osgi.framework.Version;

public class ModelUtil {

  private static final String CONFIGURATION_URL = "configuration.url";
  private static SourceUnMarshaller sourceUnMarshaller;
  private static SourceProvider sourceProvider;

  public static void setUnMarshaller( SourceUnMarshaller sourceUnMarshaller ) {
    ModelUtil.sourceUnMarshaller = sourceUnMarshaller;
  }

  public static void unsetUnMarshaller( SourceUnMarshaller sourceUnMarshaller ) {
    ModelUtil.sourceUnMarshaller = null;
  }

  public static SourceUnMarshaller getSourceUnMarshaller() {
    return sourceUnMarshaller;
  }

  public static void setSourceProvider( SourceProvider sourceProvider ) {
    ModelUtil.sourceProvider = sourceProvider;
  }

  public static SourceProvider getSourceProvider() {
    if( sourceProvider == null ) {
      sourceProvider = getDefaultModel();
    }
    return ModelUtil.sourceProvider;
  }

  private static SourceProvider getDefaultModel() {
    SourceProvider result = null;
    String[] modelURLs = new String[]{
      getConfigurationURL(), getLocalURL()
    };
    for( int i = 0; i < modelURLs.length && result == null; i++ ) {
      try {
        URL url = new URL( modelURLs[ i ] );
        result = sourceUnMarshaller.marshal( url.openStream() );
      } catch( Exception e ) {
        System.out.println( "Failed to load model" );
        e.printStackTrace();
      }
    }
    return result;
  }

  private static String getLocalURL() {
    String result = "";
    try {
      URL unresolvedURL = FileLocator.find( CoreActivator.getBundleContext().getBundle(),
                                            new Path( "data/juno-sources.json" ),
                                            null );
      result = FileLocator.resolve( unresolvedURL ).toExternalForm();
    } catch( Exception e ) {
      // Ignore
    }
    return result;
  }

  public static String getConfigurationURL() {
    return CoreActivator.getBundleContext().getProperty( CONFIGURATION_URL );
  }

  public Comparator<SourceVersion> getSourceVersionComparator() {
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

  public Comparator<Source> getSourceComparator() {
    Comparator<Source> comparator = new Comparator<Source>() {

      @Override
      public int compare( Source arg0, Source arg1 ) {
        String name = arg0.getName();
        String name2 = arg1.getName();
        return name.compareTo( name2 );
      }
    };
    return comparator;
  }

  public List<Source> searchSources( List<String> anyListOf, List<Source> sources ) {
    List<Source> result = new ArrayList<Source>();
    for( Source source : sources ) {
      String name = source.getName();
      for( String term : anyListOf ) {
        if( term.length() > 0 && name.contains( term ) ) {
          result.add( source );
        }
      }
    }
    return result;
  }

  public SourceVersion searchSourceVerions( String sourceVersion, List<Source> sources ) {
    SourceVersion result = null;
    for( Source source : sources ) {
      List<SourceVersion> versions = source.getVersions();
      result = getVersion( sourceVersion, versions );
    }
    return result;
  }

  public SourceVersion getVersion( String sourceVersion, List<SourceVersion> versions ) {
    SourceVersion result = null;
    Collections.sort( versions, getSourceVersionComparator() );
    if( sourceVersion == null || sourceVersion.length() == 0 ) {
      result = versions.get( 0 );
    } else {
      for( SourceVersion version : versions ) {
        if( version.getVersion().equals( sourceVersion ) ) {
          result = version;
        }
      }
    }
    return result;
  }
}

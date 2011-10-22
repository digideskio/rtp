/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal.util;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.rtp.configurator.service.provider.internal.ProviderActivator;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceUnMarshaller;

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
        // System.out.println( "Failed to load model" );
        // e.printStackTrace();
      }
    }
    return result;
  }

  private static String getLocalURL() {
    String result = "";
    try {
      URL unresolvedURL = FileLocator.find( ProviderActivator.getBundleContext().getBundle(),
                                            new Path( "data/juno-sources.json" ),
                                            null );
      result = FileLocator.resolve( unresolvedURL ).toExternalForm();
    } catch( Exception e ) {
      // Ignore
    }
    return result;
  }

  protected static String getConfigurationURL() {
    return ProviderActivator.getBundleContext().getProperty( CONFIGURATION_URL );
  }
}

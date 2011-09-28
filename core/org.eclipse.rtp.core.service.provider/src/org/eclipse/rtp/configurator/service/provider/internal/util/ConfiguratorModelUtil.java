package org.eclipse.rtp.configurator.service.provider.internal.util;

import java.net.URL;

import org.eclipse.rtp.configurator.model.SourceProvider;
import org.eclipse.rtp.configurator.model.SourceUnMarshaller;

public class ConfiguratorModelUtil {

  private static SourceUnMarshaller sourceUnMarshaller;
  private static SourceProvider sourceProvider;
  private static String defaultModeURL = "http://foo";

  public static void setUnMarshaller( SourceUnMarshaller sourceUnMarshaller ) {
    ConfiguratorModelUtil.sourceUnMarshaller = sourceUnMarshaller;
  }

  public static void unsetUnMarshaller( SourceUnMarshaller sourceUnMarshaller ) {
    ConfiguratorModelUtil.sourceUnMarshaller = null;
  }

  public static SourceUnMarshaller getSourceUnMarshaller() {
    return sourceUnMarshaller;
  }
  
  public static void setSourceProvider(SourceProvider sourceProvider){
    ConfiguratorModelUtil.sourceProvider = sourceProvider;
  }
  
  public static SourceProvider getSourceProvider(){
    if(sourceProvider == null){
      sourceProvider = getDefaultModel();
    }
    return ConfiguratorModelUtil.sourceProvider;
  }

  private static SourceProvider getDefaultModel() {
    SourceProvider result = null;
    try {
      URL url = new URL( defaultModeURL );
      result = sourceUnMarshaller.marshal( url.openStream( ) );
    } catch( Exception e ) {
      System.out.println("Failed to load model");
      e.printStackTrace();
    }
    return result;
  }
  
  
}

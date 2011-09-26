package org.eclipse.rtp.configurator.service.provider.internal.util;

import org.eclipse.rtp.configurator.model.SourceProvider;
import org.eclipse.rtp.configurator.model.SourceUnMarshaller;

public class ConfiguratorModelUtil {

  private static SourceUnMarshaller sourceUnMarshaller;
  private static SourceProvider sourceProvider;

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
    return ConfiguratorModelUtil.sourceProvider;
  }
  
  
}

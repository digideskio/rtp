package org.eclipse.rtp.configurator.model;


public class Feature {
  
  private String id;
  private String version;

  
  public Feature() {
    // Only for Gson
  }
  
  public Feature(String id, String version) {
    this.id = id;
    this.version = version;
  }
  
  public String getId() {
    return id;
  }
  
  public String getVersion() {
    return version;
  }
}

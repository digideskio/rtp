package org.eclipse.rtp.configurator.ui.internal.event;

public class ConfigurationEvent implements IConfigurationEvent {

  private final String instanceURI;
  private final String sessionId;

  public ConfigurationEvent( String instanceURI, String sessionId ) {
    this.instanceURI = instanceURI;
    this.sessionId = sessionId;
  }

  @Override
  public String getNewIntanceURI() {
    return instanceURI;
  }

  @Override
  public String getSessionId() {
    // TODO Auto-generated method stub
    return null;
  }
}

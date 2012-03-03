package org.eclipse.rtp.configurator.ui.internal.event;

import org.eclipse.rtp.configurator.rest.IEventService;

public class EventingServiceUtil {

  private static IEventService eventService;
  private static IConfigurationListener configurationListener;

  public void addEventService( IEventService eventService ) {
    EventingServiceUtil.eventService = eventService;
  }

  public void removeEventService( IEventService eventService ) {
    EventingServiceUtil.eventService = null;
  }

  public void addConfigurationListenerService( IConfigurationListener configurationListener ) {
    EventingServiceUtil.configurationListener = configurationListener;
  }

  public void removeConfigurationListenerService( IConfigurationListener configurationListener ) {
    EventingServiceUtil.configurationListener = null;
  }

  public static IConfigurationListener getConfigurationListenerService() {
    return configurationListener;
  }

  public static IEventService getEventService() {
    return eventService;
  }
}

/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
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

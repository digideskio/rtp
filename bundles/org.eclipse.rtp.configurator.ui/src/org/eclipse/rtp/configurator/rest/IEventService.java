package org.eclipse.rtp.configurator.rest;

import org.eclipse.rtp.configurator.ui.internal.event.IConfigurationEvent;

public interface IEventService {

  void fireConfigurationEvent( IConfigurationEvent event );
}

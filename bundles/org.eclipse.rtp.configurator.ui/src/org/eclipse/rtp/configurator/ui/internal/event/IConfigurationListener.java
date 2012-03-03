package org.eclipse.rtp.configurator.ui.internal.event;

import org.eclipse.rtp.configurator.ui.ComponentsTab;

public interface IConfigurationListener {

  void addInterestedView( String sessionId, ComponentsTab componentsTab );

  void removeInterestedView( String sessionId );

  void configurationchanged( IConfigurationEvent event );
}

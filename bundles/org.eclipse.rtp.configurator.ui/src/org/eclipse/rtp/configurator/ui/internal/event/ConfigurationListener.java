/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui.internal.event;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rtp.configurator.ui.ComponentsTab;

public class ConfigurationListener implements IConfigurationListener {

  private final Map<String, ComponentsTab> sessionId2ComponentTabMap;

  public ConfigurationListener() {
    sessionId2ComponentTabMap = new HashMap<String, ComponentsTab>();
  }

  @Override
  public void addInterestedView( String sessionId, ComponentsTab componentsTab ) {
    sessionId2ComponentTabMap.put( sessionId, componentsTab );
  }

  @Override
  public void removeInterestedView( String sessionId ) {
    sessionId2ComponentTabMap.remove( sessionId );
  }

  @Override
  public void configurationchanged( IConfigurationEvent event ) {
    for( String key : sessionId2ComponentTabMap.keySet() ) {
      ComponentsTab componentsTab = sessionId2ComponentTabMap.get( key );
      componentsTab.configurationChanged( event );
    }
  }
}

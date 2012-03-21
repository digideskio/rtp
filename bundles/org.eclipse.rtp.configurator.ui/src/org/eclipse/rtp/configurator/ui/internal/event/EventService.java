/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui.internal.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rtp.configurator.rest.IEventService;

public class EventService implements IEventService {

  private final List<IConfigurationListener> listeners;

  public EventService() {
    listeners = new ArrayList<IConfigurationListener>();
  }

  public void addService( IConfigurationListener listener ) {
    if( !listeners.contains( listener ) ) {
      listeners.add( listener );
    }
  }

  public void removeService( IConfigurationListener listener ) {
    if( listeners.contains( listener ) ) {
      listeners.remove( listener );
    }
  }

  @Override
  public void fireConfigurationEvent( IConfigurationEvent event ) {
    for( IConfigurationListener listener : listeners ) {
      listener.configurationchanged( event );
    }
  }
}

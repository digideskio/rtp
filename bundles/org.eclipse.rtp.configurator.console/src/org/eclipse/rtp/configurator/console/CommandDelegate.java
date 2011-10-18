/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.console;

import java.util.List;

public interface CommandDelegate {

  void unsupportedOperation( String operation );

  void install( List<String> parameter );

  void update( List<String> anyListOf );

  void remove( List<String> anyListOf );

  void search( List<String> anyListOf );

  void show( List<String> anyListOf );

  void list( );

  void updateWorld( );

  void refresh( );
}
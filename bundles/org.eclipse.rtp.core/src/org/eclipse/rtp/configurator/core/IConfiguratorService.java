/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.core;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public interface IConfiguratorService {

  IStatus install( List<String> parameter );

  IStatus update( List<String> anyListOf );

  IStatus remove( List<String> anyListOf );

  List<String> search( List<String> anyListOf );

  List<String> show( List<String> anyListOf );

  List<String> list() throws CoreException;

  IStatus updateWorld();
}

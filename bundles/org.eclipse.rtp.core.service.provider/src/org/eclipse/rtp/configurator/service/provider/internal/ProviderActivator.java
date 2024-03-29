/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ProviderActivator implements BundleActivator {

  public static String BUNDLE_ID = "org.eclipse.rtp.configurator.service.provider";
  private static BundleContext bundleContext;

  @Override
  public void start( BundleContext context ) throws Exception {
    ProviderActivator.bundleContext = context;
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    bundleContext = null;
  }

  public static BundleContext getBundleContext() {
    return bundleContext;
  }
}

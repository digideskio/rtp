/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.rest.provider;

import org.eclipse.rtp.core.RuntimeProvisioningService;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;

public class RestProviderActivator implements BundleActivator {

  private static BundleContext context;
  private static RuntimeProvisioningService provisioningService;
  private static RestProviderActivator instance;
  private static HttpService httpService;
  private static SourceUnMarshaller sourceUnmarshallerServie;

  public static void setUp( RuntimeProvisioningService service ) {
    provisioningService = service;
  }

  public static void shutDown( RuntimeProvisioningService service ) {
    provisioningService = null;
  }

  public RuntimeProvisioningService getProvisioningService() {
    return provisioningService;
  }

  public static void setUpHttpService( HttpService service ) {
    httpService = service;
  }

  public static void shutDownHttpService( HttpService service ) {
    httpService = null;
  }

  public HttpService getHttpService() {
    return httpService;
  }

  public static void setUpSourceUnmarshaller( SourceUnMarshaller service ) {
    sourceUnmarshallerServie = service;
  }

  public static void shutDownSourceUnmarshaller( SourceUnMarshaller service ) {
    sourceUnmarshallerServie = null;
  }

  public SourceUnMarshaller getMarshalService() {
    return sourceUnmarshallerServie;
  }

  static BundleContext getContext() {
    return context;
  }

  public static RestProviderActivator getDefault() {
    return instance;
  }

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    RestProviderActivator.context = bundleContext;
    RestProviderActivator.instance = this;
  }

  public static void activateServices() {
    try {
      httpService.registerServlet( "/rt", new RuntimeProvisioningServlet(), null, null );
    } catch( Exception e ) {
      System.out.println( "Failed to register servlets" );
      e.printStackTrace();
    }
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    RestProviderActivator.context = null;
    RestProviderActivator.instance = null;
  }

  public static void deactivateServices() {
    httpService.unregister( "/rt" );
  }
}

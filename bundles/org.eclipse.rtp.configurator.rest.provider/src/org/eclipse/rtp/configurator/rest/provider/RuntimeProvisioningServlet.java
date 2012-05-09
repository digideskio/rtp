/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.rest.provider;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.rtp.configurator.rest.provider.internal.DeleteRequestHandler;
import org.eclipse.rtp.configurator.rest.provider.internal.GetRequestHandler;
import org.eclipse.rtp.configurator.rest.provider.internal.PutRequestHandler;

public class RuntimeProvisioningServlet extends HttpServlet implements Servlet {

  private static final int ERROR_STATUS = 500;
  private static final int OK_STATUS = 200;
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet( HttpServletRequest req, HttpServletResponse resp )
    throws ServletException, IOException
  {
    GetRequestHandler getRequestHandler = new GetRequestHandler();
    String resultList = "";
    try {
      resultList = getRequestHandler.handleRequest( req );
      resp.setStatus( OK_STATUS );
    } catch( CoreException e ) {
      resp.setStatus( ERROR_STATUS );
    }
    PrintWriter writer = resp.getWriter();
    writer.write( resultList );
    writer.flush();
  }

  @Override
  protected void doDelete( HttpServletRequest req, HttpServletResponse resp )
    throws ServletException, IOException
  {
    DeleteRequestHandler deleteRequestHandler = new DeleteRequestHandler();
    IStatus requestStatus = deleteRequestHandler.handleRequest( req,
                                                                RestProviderActivator.getDefault()
                                                                  .getProvisioningService() );
    PrintWriter writer = resp.getWriter();
    if( requestStatus.isOK() ) {
      resp.setStatus( OK_STATUS );
    } else {
      writer.write( requestStatus.getMessage() );
      resp.setStatus( ERROR_STATUS );
      writer.flush();
    }
  }

  @Override
  protected void doPut( HttpServletRequest req, HttpServletResponse resp )
    throws ServletException, IOException
  {
    PutRequestHandler putRequestHandler = new PutRequestHandler();
    IStatus requestStatus = putRequestHandler.handleRequest( req,
                                                             RestProviderActivator.getDefault()
                                                               .getProvisioningService() );
    if( requestStatus.isOK() ) {
      resp.setStatus( OK_STATUS );
    } else {
      resp.setStatus( ERROR_STATUS );
      PrintWriter writer = resp.getWriter();
      writer.write( requestStatus.getMessage() );
      writer.flush();
    }
  }
}

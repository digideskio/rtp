/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.rest.provider.internal;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.rtp.configurator.rest.provider.internal.util.PathInfoUtil;
import org.eclipse.rtp.core.RuntimeProvisioningService;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;

public class DeleteRequestHandler {

  public IStatus handleRequest( HttpServletRequest request,
                                RuntimeProvisioningService provisioningService )
  {
    IStatus result = Status.CANCEL_STATUS;
    String pathInfo = request.getPathInfo();
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    if( pathInfo == null || pathInfo.length() == 0 ) {
      result = Status.CANCEL_STATUS;
    } else if( pathInfoUtil.isProvisioning( pathInfo, getModelUtil(), getSources() ) ) {
      result = removeResources( provisioningService, pathInfo, pathInfoUtil );
    }
    return result;
  }

  private IStatus removeResources( RuntimeProvisioningService provisioningService,
                                   String pathInfo,
                                   PathInfoUtil pathInfoUtil )
  {
    IStatus result;
    SourceVersion sourceVerionToRemove = pathInfoUtil.getSourceVersion( pathInfo,
                                                                        getModelUtil(),
                                                                        getSources() );
    List<SourceVersion> sourceVersoinsToDelte = Arrays.asList( new SourceVersion[]{
      sourceVerionToRemove
    } );
    result = provisioningService.remove( sourceVersoinsToDelte );
    return result;
  }

  protected List<Source> getSources() {
    return ModelUtil.getSourceProvider().getSources();
  }

  protected ModelUtil getModelUtil() {
    return new ModelUtil();
  }
}

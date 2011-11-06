/*******************************************************************************
 * Copyright (c) 2011 Sebastian Schmidt and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastian Schmidt - initial API and implementation
 ******************************************************************************/
package org.eclipse.rtp.httpdeployer.internal;

import javax.servlet.ServletException;

import org.eclipse.rtp.core.IRTPService;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class HttpDeployerComponent {

	private HttpDeployerInitializer initializer = new HttpDeployerInitializer();

	protected HttpService httpService;
	protected IRTPService rtpService;

	public void setRtpService(IRTPService rtpService) {
		this.rtpService = rtpService;
	}

	public void unsetRtpService(IRTPService rtpService) {
		this.rtpService = null;
	}

	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	protected void startService() throws ServletException, NamespaceException {
		initializer.setRtpService(rtpService);
		initializer.setHttpService(httpService);
		initializer.init();
	}

	protected void shutdownService() {
		initializer.unregister();
	}
}
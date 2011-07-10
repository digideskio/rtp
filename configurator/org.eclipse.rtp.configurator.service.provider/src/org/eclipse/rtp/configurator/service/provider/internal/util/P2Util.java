/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal.util;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.rtp.configurator.service.provider.internal.ProviderActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class P2Util {

	public IProvisioningAgent getAgent() throws CoreException {
		BundleContext bundleContext = ProviderActivator.getBundleContext();
		String packageLocation = bundleContext.getProperty("package.location");
		URI packageLocationURI = new Path(packageLocation).append("p2").toFile().toURI();
		IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) getService(bundleContext, IProvisioningAgentProvider.SERVICE_NAME);
		checkAgentAvailable(agentProvider);
		return agentProvider.createAgent(packageLocationURI);
	}

	private void checkAgentAvailable(IProvisioningAgentProvider agentProvider) throws CoreException {
		if (agentProvider == null) {
			String message = "Agent provider service not available"; //$NON-NLS-1$
			IStatus status = StatusUtil.createStatus(IStatus.ERROR, message, null);
			throw new CoreException(status);
		}
	}

	public Object getService(BundleContext context, String name) {
		if (context == null || name == null) {
			throw new IllegalArgumentException();
		}
		ServiceReference reference = context.getServiceReference(name);
		Object result = null;
		if (reference != null) {
			result = context.getService(reference);
			context.ungetService(reference);
		}
		return result;
	}

}

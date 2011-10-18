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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.internal.provisional.configurator.Configurator;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.rtp.configurator.service.provider.internal.ProviderActivator;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureManager;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.RepositoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class P2Util {
	
	private static Configurator configurator;
	private static RepositoryManager repositoryManager;
	private static IProvisioningAgent provisioningAgent;
	private static FeatureManager featureManager;
	
	public FeatureManager getFeatureManager(){
		return featureManager;
	}
	
	public RepositoryManager getRepositoryManager(){
		return repositoryManager;
	}
	
	public static void setConfigurator(Configurator configuratorService){
		P2Util.configurator = configuratorService;
	}
	
	public static void unsetConfigurator(Configurator configuratorService){
		P2Util.configurator = null;
	}
	
	protected static void startService() throws CoreException {
		provisioningAgent = getAgent();
		repositoryManager = new RepositoryManager(provisioningAgent);
		featureManager = new FeatureManager(provisioningAgent, repositoryManager, configurator);
	}

	protected static void shutdown() {
		featureManager = null;
		repositoryManager = null;
		provisioningAgent.stop();
		provisioningAgent = null;
		configurator = null;
	}

	private static IProvisioningAgent getAgent() throws CoreException {
		BundleContext bundleContext = ProviderActivator.getBundleContext();
		IProvisioningAgentProvider agentProvider = getService(bundleContext, IProvisioningAgentProvider.class, IProvisioningAgentProvider.SERVICE_NAME);
		checkAgentAvailable(agentProvider);
		return agentProvider.createAgent(null);
	}
	
	private static void checkAgentAvailable(IProvisioningAgentProvider agentProvider) throws CoreException {
		if (agentProvider == null) {
			String message = "Agent provider service not available"; //$NON-NLS-1$
			IStatus status = StatusUtil.createStatus(IStatus.ERROR, message, null);
			throw new CoreException(status);
		}
	}
	
	private static <T> T getService(BundleContext context, Class<T> type, String name) {
		if (context == null || name == null) {
			throw new IllegalArgumentException();
		}
		ServiceReference<?> reference = context.getServiceReference(name);
		T result = null;
		if (reference != null) {
			result = (T) context.getService(reference);
			context.ungetService(reference);
		}
		return result;
	}
}

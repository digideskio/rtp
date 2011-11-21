/*******************************************************************************
 * Copyright (c) 2011 Sebastian Schmidt and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastian Schmidt - initial API and implementation
 ******************************************************************************/
package org.eclipse.rtp.httpdeployer.feature;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.rtp.core.RuntimeProvisioningService;
import org.eclipse.rtp.core.model.Feature;
import org.eclipse.rtp.core.model.SourceVersion;

public class FeatureManager {

	private RuntimeProvisioningService rtpService;

	public FeatureManager(RuntimeProvisioningService rtpService) {
		this.rtpService = rtpService;
	}

	// TODO: Not tested
	public void installFeature(String featureId, String version)
			throws FeatureInstallException {
		Feature feature = new Feature(featureId, version);
		SourceVersion sourceVersion = new SourceVersion();
		sourceVersion.addFeature(feature);
		IStatus result = rtpService.install(sourceVersion);
		if (!result.isOK()) {
			throw new FeatureInstallException(result.getMessage());
		}
	}

	// TODO: Not tested
	public void uninstallFeature(String featureId, String version)
			throws FeatureInstallException {
		Feature feature = new Feature(featureId, version);
		SourceVersion sourceVersion = new SourceVersion();
		sourceVersion.addFeature(feature);
		List<SourceVersion> sourceVersionsToUninstall = new ArrayList<SourceVersion>();
		sourceVersionsToUninstall.add(sourceVersion);
		IStatus result = rtpService.remove(sourceVersionsToUninstall);
		if (!result.isOK()) {
			throw new FeatureInstallException(result.getMessage());
		}
	}
}
/******************************************************************************* 
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.service.provider.internal;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.rtp.configurator.core.IConfiguratorService;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;

public class DefaultConfiguratorService implements IConfiguratorService {

	private P2Util p2Util;

	public void setUp() {
		setP2Util(new P2Util());
	}

	public void shutDown() {
		setP2Util(null);
	}

	public void setP2Util(P2Util p2Util) {
		this.p2Util = p2Util;
	}

	@Override
	public IStatus install(List<String> parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStatus update(List<String> anyListOf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStatus remove(List<String> anyListOf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> search(List<String> anyListOf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> show(List<String> anyListOf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> list() throws CoreException {
		return null;
	}

	@Override
	public IStatus updateWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStatus refresh() {
		// TODO Auto-generated method stub
		return null;
	}

}

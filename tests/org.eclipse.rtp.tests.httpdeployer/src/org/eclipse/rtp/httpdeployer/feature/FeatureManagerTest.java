/*******************************************************************************
 * Copyright (c) 2011 Sebastian Schmidt and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastian Schmidt - initial API and implementation
 ******************************************************************************/
package org.eclipse.rtp.httpdeployer.feature;

import org.eclipse.equinox.p2.planner.IPlanner;
import org.eclipse.rtp.httpdeployer.repository.RepositoryManager;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FeatureManagerTest {

	@Mock
	RepositoryManager repositoryManager;

	@Mock
	IPlanner planner;

	FeatureManager objectUnderTest;

	static final String FEATURE_NAME = "testFeature";
	static final String FEATURE_VERSION = "1.0.0";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
}

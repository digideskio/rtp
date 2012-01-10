/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.core.service.provider.test;

import org.eclipse.rtp.configurator.service.provider.internal.ConfiguratorServiceSearchTest;
import org.eclipse.rtp.configurator.service.provider.internal.ConfiguratorServiceTest;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.FeatureManagerTest;
import org.eclipse.rtp.configurator.service.provider.internal.deploy.RepositoryManagerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith( Suite.class )
@SuiteClasses( { 
  ConfiguratorServiceSearchTest.class,
  ConfiguratorServiceTest.class,
  FeatureManagerTest.class,
  RepositoryManagerTest.class
} )
public class AllServiceProviderTestSuite {
  // no content
}

/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.httpdeployer.test;

import org.eclipse.rtp.httpdeployer.bundle.BundleServletOperationsTest;
import org.eclipse.rtp.httpdeployer.bundle.BundleServletTest;
import org.eclipse.rtp.httpdeployer.feature.FeatureServletTest;
import org.eclipse.rtp.httpdeployer.internal.HttpDeployerComponentTest;
import org.eclipse.rtp.httpdeployer.repository.RepositoryManagerTest;
import org.eclipse.rtp.httpdeployer.repository.RepositoryServletTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith( Suite.class )
@SuiteClasses( {
  BundleServletOperationsTest.class,
  BundleServletTest.class,
  FeatureServletTest.class,
  HttpDeployerComponentTest.class,
  RepositoryManagerTest.class,
  RepositoryServletTest.class
} )
public class AllHttpDeployerTestSuite {
}

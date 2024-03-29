/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.core.model.test;

import org.eclipse.rtp.core.model.SourceProviderTest;
import org.eclipse.rtp.core.model.SourceTest;
import org.eclipse.rtp.core.model.SourceUnMarshallerTest;
import org.eclipse.rtp.core.model.SourceVersionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith( Suite.class )
@SuiteClasses( {
  SourceProviderTest.class,
  SourceTest.class,
  SourceVersionTest.class,
  SourceUnMarshallerTest.class
} )
public class AllModelTestSuite {
  // no content
}

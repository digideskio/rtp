/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.rtp.core.model.SourceVersion;
import org.junit.Test;

public class SourceVersionTest {

  @Test
  public void testGetValues() {
    SourceVersion version = new SourceVersion( "1.4",
                                               "http://foo.bar",
                                               "description",
                                               "http://info.bar" );
    assertEquals( "1.4", version.getVersion() );
    assertEquals( "http://foo.bar", version.getRepositoryUrl() );
    assertTrue( version.getFeatures().isEmpty() );
  }
}

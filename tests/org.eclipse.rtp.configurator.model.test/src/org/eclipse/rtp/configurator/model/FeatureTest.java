/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FeatureTest {

  @Test
  public void testGetValues() {
    Feature feature = new Feature( "org.eclipse.rap.feature", "1.5.0" );
    assertEquals( "org.eclipse.rap.feature", feature.getId() );
    assertEquals( "1.5.0", feature.getVersion() );
  }
}

package org.eclipse.rtp.configurator.service.provider.internal.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ModelUtilTest {

  @Test
  public void testModelUrlReadFromProperty() {
    String expectedURL = "http://test.com";
    System.setProperty( "configuration.url", expectedURL );
    String configurationURL = ModelUtil.getConfigurationURL();
    assertEquals( expectedURL, configurationURL );
  }
}

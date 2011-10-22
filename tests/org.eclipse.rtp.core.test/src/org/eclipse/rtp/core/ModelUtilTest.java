package org.eclipse.rtp.core;

import static org.junit.Assert.assertEquals;

import org.eclipse.rtp.core.util.ModelUtil;
import org.junit.Test;

public class ModelUtilTest {

	@Test
	public void testModelUrlReadFromProperty() {
		String expectedURL = "http://test.com";
		System.setProperty("configuration.url", expectedURL);
		String configurationURL = ModelUtil.getConfigurationURL();
		assertEquals(expectedURL, configurationURL);
	}
}

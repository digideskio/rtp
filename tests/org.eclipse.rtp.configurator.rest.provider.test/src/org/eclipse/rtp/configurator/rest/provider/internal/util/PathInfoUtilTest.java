package org.eclipse.rtp.configurator.rest.provider.internal.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;

public class PathInfoUtilTest {

  private Source source;
  private Source source2;
  private SourceVersion sourceVersion;
  private SourceVersion sourceVersion2;
  private List<Source> sources;

  @Before
  public void setUp() throws Exception {
    initModel();
  }

  private void initModel() {
    source = new Source( "test", "test source", "http://foo/test" );
    source2 = new Source( "test2", "test source2", "http://foo/test2" );
    sourceVersion = new SourceVersion( "1.0.0", "http://foo/source", "test mock", "http://foo/info" );
    source.addVersion( sourceVersion );
    sourceVersion2 = new SourceVersion( "1.1.0",
                                        "http://foo/source",
                                        "test mock",
                                        "http://foo/info" );
    source2.addVersion( sourceVersion2 );
    sources = Arrays.asList( new Source[]{
      source, source2
    } );
  }

  @Test
  public void testsearchSourceVersionTest() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    SourceVersion result = pathInfoUtil.getSourceVersion( "/install/test/1.0.0/", new ModelUtil(), sources );
    assertEquals( sourceVersion, result );
  }

  @Test
  public void testsearchSourceVersionTest2() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    SourceVersion result = pathInfoUtil.getSourceVersion( "/install/test2/1.1.0/", new ModelUtil(), sources );
    assertEquals( sourceVersion2, result );
  }

  @Test
  public void testsearchSourceVersionTestNotExits() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    SourceVersion result = pathInfoUtil.getSourceVersion( "/install/test3/1.1.0/", new ModelUtil(), sources );
    assertNull( result );
  }

  @Test
  public void testGetSourceVersionString() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    String result = pathInfoUtil.getSourceVersion( "install/foo/1.0.0" );
    assertEquals( "1.0.0", result );
  }

  @Test
  public void testGetSourceName() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    String result = pathInfoUtil.getSourceName( "install/foo/1.0.0" );
    assertEquals( "foo", result );
  }

  @Test
  public void testIsProvisioningNullPathInfo() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( null, new ModelUtil(), sources );
    assertFalse( result );
  }

  @Test
  public void testIsProvisioningEmtpyPathInfo() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( "", new ModelUtil(), sources );
    assertFalse( result );
  }

  @Test
  public void testIsProvisioningPathInfoStartsAndEndsWithSlash() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( "/install/test/1.0.0/", new ModelUtil(), sources );
    assertTrue( result );
  }

  @Test
  public void testIsProvisioningPathInfoStartsWithSlash() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( "/install/test/1.0.0", new ModelUtil(), sources );
    assertTrue( result );
  }

  @Test
  public void testIsProvisioningPathInfoEndsWithSlash() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( "install/test/1.0.0/", new ModelUtil(), sources );
    assertTrue( result );
  }

  @Test
  public void testIsProvisioningPathInfoWithoutEndAnsStartSlash() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( "install/test/1.0.0", new ModelUtil(), sources );
    assertTrue( result );
  }

  @Test
  public void testIsProvisioningPathInfoSourceNotExists() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( "install/test3/1.0.0", new ModelUtil(), sources );
    assertFalse( result );
  }

  @Test
  public void testIsProvisioningPathInfoSourceVersionNotExists() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    boolean result = pathInfoUtil.isProvisioning( "install/test3/1.2.0", new ModelUtil(), sources );
    assertFalse( result );
  }

  @Test
  public void testGetProvisioningInfo() {
    PathInfoUtil pathInfoUtil = new PathInfoUtil();
    String[] result = pathInfoUtil.getProvisioningInfo( "install/foo/1.0.0/" );
    assertArrayEquals( new String[]{
      "install", "foo", "1.0.0"
    }, result );
  }
}

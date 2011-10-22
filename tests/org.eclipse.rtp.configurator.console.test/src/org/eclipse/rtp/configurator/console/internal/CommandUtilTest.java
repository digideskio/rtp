package org.eclipse.rtp.configurator.console.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rtp.configurator.console.internal.util.Fixture;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandUtilTest {

  private CommandUtil commandUtil;
  private List<Source> exampleSources;

  @Before
  public void setUp() throws Exception {
    exampleSources = getExampleSources();
    commandUtil = new CommandUtil() {

      @Override
      protected List<Source> getSources() {
        return exampleSources;
      }
    };
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetSourceVersions() {
    Source source = exampleSources.get( 0 );
    List<String> parameters = new ArrayList<String>();
    parameters.add( source.getName() );
    parameters.add( "1.4" );
    SourceVersion sourceVersions = commandUtil.getSourceVersions( parameters );
    assertEquals( source.getVersions().get( 0 ), sourceVersions );
  }

  @Test
  public void testNoParametersProvided() {
    List<String> parameters = new ArrayList<String>();
    SourceVersion sourceVersions = commandUtil.getSourceVersions( parameters );
    assertNull( sourceVersions );
  }

  @Test
  public void testGetSourceVersionsToUninstall() {
    Source source = exampleSources.get( 0 );
    List<String> parameters = new ArrayList<String>();
    parameters.add( source.getName() );
    List<SourceVersion> sourceVersions = commandUtil.getSourceVersionsToUninstall( parameters );
    assertEquals( source.getVersions(), sourceVersions );
  }

  @Test
  public void testUninstallEmptyList() {
    List<String> parameters = new ArrayList<String>();
    List<SourceVersion> sourceVersions = commandUtil.getSourceVersionsToUninstall( parameters );
    assertEquals( 0, sourceVersions.size() );
  }

  private List<Source> getExampleSources() throws IOException {
    SourceProvider sourceProvider = Fixture.getSourceProvider( Fixture.readExampleSources() );
    return sourceProvider.getSources();
  }
}

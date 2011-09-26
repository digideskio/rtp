package org.eclipse.rtp.configurator.service.provider.internal;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.rtp.configurator.model.Source;
import org.eclipse.rtp.configurator.model.SourceProvider;
import org.eclipse.rtp.configurator.model.SourceUnMarshaller;
import org.eclipse.rtp.configurator.service.provider.internal.util.ConfiguratorModelUtil;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;


public class ConfiguratorServiceSearchTest {

  private SourceProvider provider;
  private DefaultConfiguratorService configuratorService;

  @Before
  public void setUp() throws IOException{
    configuratorService = new DefaultConfiguratorService();
    InputStream stream = readExampleSources();
    SourceUnMarshaller marshaller = ConfiguratorModelUtil.getSourceUnMarshaller();
    provider = marshaller.marshal( stream );
    ConfiguratorModelUtil.setSourceProvider( provider );
  }
  
  @Test
  public void testList() throws CoreException
  {
    List<String> listExpected = getSortedTestData();
    
    List<String> list = configuratorService.list();
    
    assertTrue( Arrays.equals( listExpected.toArray( ), list.toArray( ) ) );
  }
  
  @Test
  public void testShow() throws CoreException
  {
    List<String> listExpected = getSortedTestData();
    List<String> showParameters = new ArrayList<String>();
    showParameters.add( "equinox" );
    
    List<String> list = configuratorService.show( showParameters );
    
    assertTrue( Arrays.equals( new String[]{ listExpected.get( 0 ) }, list.toArray( ) ) );
  }
  
  @Test
  public void testShowManyParameters() throws CoreException
  {
    List<String> listExpected = getSortedTestData();
    List<String> showParameters = new ArrayList<String>();
    showParameters.add( "equinox" );
    
    List<String> list = configuratorService.show( showParameters );
    
    assertTrue( Arrays.equals( new String[]{ listExpected.get( 0 ) }, list.toArray( ) ) );
  }
  
  @Test
  public void testSearch() throws CoreException
  {
    List<String> listExpected = getSortedTestData();
    List<String> searchParameters = new ArrayList<String>();
    searchParameters.add( "ra" );
    
    List<String> list = configuratorService.search( searchParameters );
    
    assertTrue( Arrays.equals( new String[]{ listExpected.get( 1 ) }, list.toArray( ) ) );
  }

  private List<String> getSortedTestData() {
    List<String> listExpected = new ArrayList<String>();
    for( Source source : provider.getSources() ) {
      listExpected.add( source.toString() );
    }
    Collections.sort( listExpected );
    return listExpected;
  }
  
  private InputStream readExampleSources() throws IOException {
    Bundle bundle = Platform.getBundle( "org.eclipse.rtp.configurator.model.test" );
    URL unResolvedUrl = FileLocator.find( bundle, new Path("data/example-sources.json"), null );
    URL testDataUrl = FileLocator.resolve( unResolvedUrl );
    File testDataFile = new Path( testDataUrl.getFile() ).toFile();
    return new FileInputStream( testDataFile );
  }

}

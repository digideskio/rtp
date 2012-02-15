package org.eclipse.rtp.configurator.rest.provider.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.rtp.configurator.service.provider.internal.util.P2Util;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings( "restriction" )
public class GetRequestHandlerTest {

  private Source source;
  private Source source2;
  ModelUtil modelUtilMock;
  SourceUnMarshaller marshalService;
  private GetRequestHandler requestHandler;
  private P2Util p2UtilMock;

  @Before
  public void setUp() {
    source = new Source( "test", "test source", "http://foo/test" );
    source.addVersion( new SourceVersion( "1.0",
                                          "http://foo/repository",
                                          "test version 1",
                                          "http://foo/info/url" ) );
    source.addVersion( new SourceVersion( "2.0",
                                          "http://foo/repository",
                                          "test version 2",
                                          "http://foo/info/url" ) );
    source2 = new Source( "test2", "test source2", "http://foo/test2" );
    source2.addVersion( new SourceVersion( "1.0.",
                                           "http://foo/repository2",
                                           "test version",
                                           "http://foo/info/url2" ) );
    modelUtilMock = mock( ModelUtil.class );
    marshalService = mock( SourceUnMarshaller.class );
    p2UtilMock = mock( P2Util.class );
    requestHandler = new GetRequestHandler() {

      @Override
      protected ModelUtil getModelUtil() {
        return modelUtilMock;
      }

      @Override
      protected SourceUnMarshaller getMarshalService() {
        return marshalService;
      }

      @Override
      protected P2Util getP2Util() {
        return p2UtilMock;
      }
    };
  }

  @Test
  public void testHandleRequestList() throws CoreException {
    List<Source> expectedSources = new ArrayList<Source>();
    expectedSources.add( source );
    expectedSources.add( source2 );
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/list" );
    when( modelUtilMock.list() ).thenReturn( expectedSources );
    String expectedResult = "unmarshalling sources";
    when( marshalService.unmarshal( expectedSources ) ).thenReturn( expectedResult );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( modelUtilMock, atLeastOnce() ).list();
    verify( marshalService, atLeastOnce() ).unmarshal( expectedSources );
    assertEquals( expectedResult, result );
  }

  @Test
  public void testHandleRequestListInstalled() throws CoreException {
    List<Source> sources = Arrays.asList( new Source[]{
      source, source2
    } );
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/list/installed" );
    when( modelUtilMock.list() ).thenReturn( sources );
    String expectedResult = "unmarshalling sources";
    when( marshalService.unmarshal( anyList() ) ).thenReturn( expectedResult );
    when( p2UtilMock.isSourceVersionInstalled( source.getVersions().get( 0 ) ) ).thenReturn( true );
    when( p2UtilMock.isSourceVersionInstalled( source.getVersions().get( 1 ) ) ).thenReturn( false );
    when( p2UtilMock.isSourceVersionInstalled( source2.getVersions().get( 0 ) ) ).thenReturn( true );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( modelUtilMock, atLeastOnce() ).list();
    verify( marshalService, atLeastOnce() ).unmarshal( anyList() );
    verify( p2UtilMock, atLeastOnce() ).isSourceVersionInstalled( source.getVersions().get( 0 ) );
    verify( p2UtilMock, atLeastOnce() ).isSourceVersionInstalled( source.getVersions().get( 1 ) );
    verify( p2UtilMock, atLeastOnce() ).isSourceVersionInstalled( source2.getVersions().get( 0 ) );
    assertEquals( expectedResult, result );
  }

  @Test
  public void testHandleRequestListUninstalled() throws CoreException {
    List<Source> sources = Arrays.asList( new Source[]{
      source, source2
    } );
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/list/uninstalled" );
    when( modelUtilMock.list() ).thenReturn( sources );
    String expectedResult = "unmarshalling sources";
    when( marshalService.unmarshal( anyList() ) ).thenReturn( expectedResult );
    when( p2UtilMock.isSourceVersionInstalled( source.getVersions().get( 0 ) ) ).thenReturn( false );
    when( p2UtilMock.isSourceVersionInstalled( source.getVersions().get( 1 ) ) ).thenReturn( true );
    when( p2UtilMock.isSourceVersionInstalled( source2.getVersions().get( 0 ) ) ).thenReturn( false );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( modelUtilMock, atLeastOnce() ).list();
    verify( marshalService, atLeastOnce() ).unmarshal( anyList() );
    verify( p2UtilMock, atLeastOnce() ).isSourceVersionInstalled( source.getVersions().get( 0 ) );
    verify( p2UtilMock, atLeastOnce() ).isSourceVersionInstalled( source.getVersions().get( 1 ) );
    verify( p2UtilMock, atLeastOnce() ).isSourceVersionInstalled( source2.getVersions().get( 0 ) );
    assertEquals( expectedResult, result );
  }

  @Test
  public void testHandleRequestShow() throws CoreException {
    List<Source> expectedSources = new ArrayList<Source>();
    expectedSources.add( source );
    List<String> queryList = new ArrayList<String>();
    queryList.add( "test" );
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/show" );
    when( requestMock.getParameter( "query" ) ).thenReturn( "test" );
    when( modelUtilMock.search( queryList ) ).thenReturn( expectedSources );
    when( marshalService.unmarshal( expectedSources ) ).thenReturn( "show result" );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( requestMock, atLeastOnce() ).getParameter( "query" );
    verify( modelUtilMock, atLeastOnce() ).search( queryList );
    assertEquals( "show result", result );
  }

  @Test
  public void testHandleRequestShowEmptyQueryParameter() throws CoreException {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/show" );
    when( requestMock.getParameter( "query" ) ).thenReturn( "" );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( requestMock, atLeastOnce() ).getParameter( "query" );
    verify( modelUtilMock, never() ).search( Arrays.asList( new String[]{
      ""
    } ) );
    assertEquals( "", result );
  }

  @Test
  public void testHandleRequestShowNullQueryParameter() throws CoreException {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/show" );
    when( requestMock.getParameter( "query" ) ).thenReturn( null );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( requestMock, atLeastOnce() ).getParameter( "query" );
    verify( modelUtilMock, never() ).search( Arrays.asList( new String[]{
      null
    } ) );
    assertEquals( "", result );
  }

  @Test
  public void testHandleRequestSearch() throws CoreException {
    List<Source> expectedSources = new ArrayList<Source>();
    expectedSources.add( source );
    List<String> queryList = new ArrayList<String>();
    queryList.add( "te" );
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/search" );
    when( requestMock.getParameter( "query" ) ).thenReturn( "te" );
    when( modelUtilMock.search( queryList ) ).thenReturn( expectedSources );
    when( marshalService.unmarshal( expectedSources ) ).thenReturn( "search result unmarshalled" );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( requestMock, atLeastOnce() ).getParameter( "query" );
    verify( modelUtilMock, atLeastOnce() ).search( queryList );
    verify( marshalService, atLeastOnce() ).unmarshal( expectedSources );
    assertEquals( "search result unmarshalled", result );
  }

  @Test
  public void testHandleRequestSearchNullQueryParameter() throws CoreException {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/search" );
    when( requestMock.getParameter( "query" ) ).thenReturn( null );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( requestMock, atLeastOnce() ).getParameter( "query" );
    verify( modelUtilMock, never() ).search( Arrays.asList( new String[]{
      ""
    } ) );
    assertEquals( "", result );
  }

  @Test
  public void testHandleRequestSearchEmptyQueryParameter() throws CoreException {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/search" );
    when( requestMock.getParameter( "query" ) ).thenReturn( "" );
    String result = requestHandler.handleRequest( requestMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( requestMock, atLeastOnce() ).getParameter( "query" );
    verify( modelUtilMock, never() ).search( Arrays.asList( new String[]{
      ""
    } ) );
    assertEquals( "", result );
  }
}

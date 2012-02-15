package org.eclipse.rtp.configurator.rest.provider.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.rtp.core.RuntimeProvisioningService;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.junit.Before;
import org.junit.Test;

public class DeleteRequestHandlerTest {

  private Source source;
  private Source source2;
  private SourceVersion sourceVersion;
  private SourceVersion sourceVersion2;
  List<Source> sources;
  private DeleteRequestHandler deleteRequestHandler;

  @Before
  public void setUp() throws Exception {
    initModel();
    createMocketdeleteRequestHandler();
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

  private void createMocketdeleteRequestHandler() {
    deleteRequestHandler = new DeleteRequestHandler() {

      @Override
      protected List<Source> getSources() {
        return sources;
      }
    };
  }

  @Test
  public void testHandleRequestInstallStatusOK() {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/test/1.0.0" );
    RuntimeProvisioningService provisioningServiceMock = mock( RuntimeProvisioningService.class );
    List<SourceVersion> sourceVersionsToDelete = Arrays.asList( new SourceVersion[]{
      sourceVersion
    } );
    when( provisioningServiceMock.remove( sourceVersionsToDelete ) ).thenReturn( Status.OK_STATUS );
    IStatus status = deleteRequestHandler.handleRequest( requestMock, provisioningServiceMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( provisioningServiceMock, atLeastOnce() ).remove( sourceVersionsToDelete );
    assertEquals( Status.OK_STATUS, status );
  }

  @Test
  public void testHandleRequestInstallNoVersionProvided() {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/test/" );
    RuntimeProvisioningService provisioningServiceMock = mock( RuntimeProvisioningService.class );
    IStatus status = deleteRequestHandler.handleRequest( requestMock, provisioningServiceMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( provisioningServiceMock, never() ).install( sourceVersion );
    assertEquals( Status.CANCEL_STATUS, status );
  }

  @Test
  public void testHandleRequestInstallNoNameProvided() {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "/1.0.0/" );
    RuntimeProvisioningService provisioningServiceMock = mock( RuntimeProvisioningService.class );
    IStatus status = deleteRequestHandler.handleRequest( requestMock, provisioningServiceMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( provisioningServiceMock, never() ).install( sourceVersion );
    assertEquals( Status.CANCEL_STATUS, status );
  }

  @Test
  public void testHandleRequestInstallSourceNotExists() {
    HttpServletRequest requestMock = mock( HttpServletRequest.class );
    when( requestMock.getPathInfo() ).thenReturn( "test3/1.0.0/" );
    RuntimeProvisioningService provisioningServiceMock = mock( RuntimeProvisioningService.class );
    IStatus status = deleteRequestHandler.handleRequest( requestMock, provisioningServiceMock );
    verify( requestMock, atLeastOnce() ).getPathInfo();
    verify( provisioningServiceMock, never() ).install( sourceVersion );
    assertEquals( Status.CANCEL_STATUS, status );
  }
}

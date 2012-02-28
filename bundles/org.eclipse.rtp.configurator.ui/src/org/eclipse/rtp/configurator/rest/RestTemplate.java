package org.eclipse.rtp.configurator.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.eclipse.rtp.core.model.internal.SourceUnMarshallerImpl;

public class RestTemplate {

  SourceUnMarshaller unMarshaller = new SourceUnMarshallerImpl();
  private String urlBase = "http://localhost:8888";

  public void setUrlBase( String urlBase ) {
    this.urlBase = urlBase;
  }

  @SuppressWarnings( "unchecked" )
  public <T> List<T> getForEntitiesAsList( String string, Class<T> clazz ) {
    GetMethod method = new GetMethod( urlBase + string );
    try {
      executeMethod( method );
      SourceProvider provider = getProvider( method );
      return ( List<T> )provider.getSources();
    } finally {
      method.releaseConnection();
    }
  }

  private void executeMethod( HttpMethodBase method ) {
    int statusCode;
    try {
      statusCode = new HttpClient().executeMethod( method );
      if( statusCode != HttpStatus.SC_OK ) {
        throw new IllegalStateException( "Error querying REST interface." );
      }
    } catch( HttpException e ) {
      throw new IllegalStateException( "Error querying REST interface." );
    } catch( IOException e ) {
      throw new IllegalStateException( "Error querying REST interface." );
    }
  }

  public void put( String string ) {
    PutMethod method = new PutMethod( urlBase + string );
    executeMethod( method );
  }

  public void get( String string ) {
    GetMethod method = new GetMethod( urlBase + string );
  }

  public void delete( String string ) {
    DeleteMethod method = new DeleteMethod( urlBase + string);
  }

  private SourceProvider getProvider( HttpMethodBase method ) {
    InputStream responseStream = null;
    try {
      try {
        responseStream = method.getResponseBodyAsStream();
      } catch( IOException e ) {
        throw new IllegalStateException( "Error querying REST interface." );
      }
      return this.unMarshaller.marshal( responseStream );
    } finally {
      if( responseStream != null ) {
        try {
          responseStream.close();
        } catch( IOException e ) {
          // closing input stream failed.
        }
      }
    }
  }
}

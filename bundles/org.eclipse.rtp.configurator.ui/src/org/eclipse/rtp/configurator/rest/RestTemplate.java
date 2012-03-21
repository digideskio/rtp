/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
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
import org.eclipse.rtp.core.util.ModelUtil;

public class RestTemplate {

  SourceUnMarshaller unMarshaller;
  private final String urlBase;

  public RestTemplate( String newIntanceURI ) {
    this.urlBase = newIntanceURI;
    unMarshaller = ModelUtil.getSourceUnMarshaller();
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
    executeMethod( method );
  }

  public void delete( String string ) {
    DeleteMethod method = new DeleteMethod( urlBase + string );
    executeMethod( method );
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

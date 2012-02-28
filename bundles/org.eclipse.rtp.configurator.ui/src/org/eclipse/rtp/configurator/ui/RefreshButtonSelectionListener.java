/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

final class RefreshButtonSelectionListener extends SelectionAdapter {

    private final ComponentsTab componentsTab;

    RefreshButtonSelectionListener( ComponentsTab componentsTab ) {
      this.componentsTab = componentsTab;
    }

    @Override
    public void widgetSelected( SelectionEvent e ) {
      // update local model

      GetMethod method = null;
      List<Source> sources = null;
      try {
        // TODO create REST call
        HttpClient client = new HttpClient();
        // TODO - check configured port
        String uri = "http://localhost:8888/rt/list";
        method = new GetMethod( uri );

        // Execute the method.
//          int statusCode = client.executeMethod(method);
        int statusCode = HttpStatus.SC_OK;

        if (statusCode != HttpStatus.SC_OK) {
          System.err.println("Method failed: " + method.getStatusLine());
          return;
        }

        // Read the response body.
//          byte[] responseBody = method.getResponseBody();
        
        // TODO remove dummy data if service is available

        // Deal with the response.
        // Use caution: ensure correct character encoding and is not binary data
//          String result = new String(responseBody);
//          System.out.println(result);
        // TODO translate JSON to internal model
        
        // TODO use test data from example-sources.json (in model.test)
        InputStream dummyDataStream = this.getClass().getResourceAsStream( "/example-sources.json" );
        // TODO - close stream

        SourceProvider provider = this.componentsTab.unMarshaller.marshal( dummyDataStream );
        sources = provider.getSources();
        
        this.componentsTab.refresh(sources);
//        } catch (HttpException e1) {
//          System.err.println("Fatal protocol violation: " + e1.getMessage());
//          e1.printStackTrace();
//      } catch (IOException e2) {
//        System.err.println("Fatal transport error: " + e2.getMessage());
//        e2.printStackTrace();
      } finally {
        // Release the connection.
        if( method != null ) {
          method.releaseConnection();
        }
      }  
      // TODO update ContentProvider
//        contentProvider.inputChanged( viewer, oldInput, newInput );
    }

  }
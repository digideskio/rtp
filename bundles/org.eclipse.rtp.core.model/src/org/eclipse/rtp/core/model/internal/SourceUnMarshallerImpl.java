/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.core.model.internal;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceUnMarshaller;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class SourceUnMarshallerImpl implements SourceUnMarshaller {

  @Override
  public SourceProvider marshal( InputStream sources ) {
    Gson gson = new Gson();
    Reader streamReader = new InputStreamReader( sources );
    JsonReader reader = new JsonReader( streamReader );
    return gson.fromJson( reader, SourceProvider.class );
  }
  
  @Override
  public String unmarshal( List<Source> sources) {
    SourceProvider sourceProvider = createSourceProvider( sources );
    return new Gson().toJson( sourceProvider );
  }

  private SourceProvider createSourceProvider( List<Source> sources ) {
    SourceProvider sourceProvider = new SourceProvider();
    for( Source source : sources ) {
      sourceProvider.addSource( source );
    }
    return sourceProvider;
  }
}

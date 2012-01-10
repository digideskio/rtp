package org.eclipse.rtp.configurator.service.provider.internal.util;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.eclipse.rtp.core.model.internal.SourceUnMarshallerImpl;

public class Fixture {

  public static InputStream readExampleSources() throws IOException {
    return Fixture.class.getResourceAsStream( "/example-sources.json" );
  }

  public static SourceProvider getSourceProvider( InputStream inputStream ) {
    SourceUnMarshaller marshaller = new SourceUnMarshallerImpl();
    return marshaller.marshal( inputStream );
  }
}

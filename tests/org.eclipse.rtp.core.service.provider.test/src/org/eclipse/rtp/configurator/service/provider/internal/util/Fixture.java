package org.eclipse.rtp.configurator.service.provider.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.rtp.core.model.SourceProvider;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.osgi.framework.Bundle;

public class Fixture {

  public static InputStream readExampleSources() throws IOException {
    Bundle bundle = Platform.getBundle( "org.eclipse.rtp.core.service.provider" );
    URL unResolvedUrl = FileLocator.find( bundle, new Path( "data/example-sources.json" ), null );
    URL testDataUrl = FileLocator.resolve( unResolvedUrl );
    File testDataFile = new Path( testDataUrl.getFile() ).toFile();
    return new FileInputStream( testDataFile );
  }

  public static SourceProvider getSourceProvider( InputStream inputStream ) {
    SourceUnMarshaller marshaller = ConfiguratorModelUtil.getSourceUnMarshaller();
    return marshaller.marshal( inputStream );
  }
}

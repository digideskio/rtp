package org.eclipse.rtp.configurator.console.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rtp.core.util.ModelUtil;

public class CommandUtil {

  public SourceVersion getSourceVersions( List<String> parameter ) {
    List<Source> sources = getSources();
    List<String> sourceName = new ArrayList<String>();
    sourceName.add( getSourceNameParameter( parameter ) );
    ModelUtil modelUtil = getModelUtil();
    List<Source> searchSources = modelUtil.searchSources( sourceName, sources );
    SourceVersion sourceVersion = modelUtil.searchSourceVerions( getVersionParameter( parameter ),
                                                                 searchSources );
    return sourceVersion;
  }

  private String getSourceNameParameter( List<String> parameters ) {
    return parameters.isEmpty()
                               ? ""
                               : parameters.get( 0 );
  }

  public List<SourceVersion> getSourceVersionsToUninstall( List<String> anyListOf ) {
    List<SourceVersion> result = new ArrayList<SourceVersion>();
    List<Source> sources = getSources();
    ModelUtil modelUtil = getModelUtil();
    List<Source> sourceToUinstall = modelUtil.searchSources( anyListOf, sources );
    for( Source source : sourceToUinstall ) {
      result.addAll( source.getVersions() );
    }
    return result;
  }

  protected List<Source> getSources() {
    List<Source> sources = ModelUtil.getSourceProvider().getSources();
    return sources;
  }

  protected ModelUtil getModelUtil() {
    return new ModelUtil();
  }

  private String getVersionParameter( List<String> parameters ) {
    return parameters.size() > 1
                                ? parameters.get( 1 )
                                : "";
  }
}

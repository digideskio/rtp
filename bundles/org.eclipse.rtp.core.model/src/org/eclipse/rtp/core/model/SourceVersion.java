/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.core.model;

import java.util.Collections;
import java.util.List;

public class SourceVersion {

  private String version;
  private String repositoryUrl;
  private List<Feature> features;

  SourceVersion() {
    // only for Gson
  }

  public SourceVersion( String version, String repositoryUrl, String description, String infoUrl ) {
    this.version = version;
    this.repositoryUrl = repositoryUrl;
    this.features = Collections.emptyList();
  }

  public String getVersion() {
    return version;
  }

  public String getRepositoryUrl() {
    return repositoryUrl;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append( "Version: " + version + "\n" );
    result.append( "Repository URL: " + repositoryUrl + "\n" );
    return result.toString();
  }
}

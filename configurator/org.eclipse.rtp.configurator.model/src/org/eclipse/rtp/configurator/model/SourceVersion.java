/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.configurator.model;


public class SourceVersion {

  private String version;
  private String repositoryUrl;
  private String id;

  SourceVersion() {
    // only for Gson
  }
  
  public SourceVersion( String id, String version, String repositoryUrl, String description, String infoUrl ) {
    this.id = id;
    this.version = version;
    this.repositoryUrl = repositoryUrl;
  }

  public String getVersion() {
    return version;
  }

  public String getRepositoryUrl() {
    return repositoryUrl;
  }
  
  public String getId(){
    return id;
  }
  
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append( "Version: " + version + "\n" );
    result.append( "Repository URL: " + repositoryUrl + "\n" );
    return result.toString();
  }
}

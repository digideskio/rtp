/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.core.model;

import java.util.ArrayList;
import java.util.List;

public class Source {

  private String name;
  private String description;
  private String infoUrl;
  private List<SourceVersion> versions;

  Source() {
    // only for Gson
  }

  public Source( String name, String description, String infoUrl ) {
    this.name = name;
    this.description = description;
    this.infoUrl = infoUrl;
    this.versions = new ArrayList<SourceVersion>();
  }

  public String getName() {
    return name;
  }

  public void addVersion( SourceVersion version ) {
    versions.add( version );
  }

  public List<SourceVersion> getVersions() {
    return new ArrayList<SourceVersion>( versions );
  }

  public String getDescription() {
    return description;
  }

  public String getInfoUrl() {
    return infoUrl;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append( "Name: " + name + "\n" );
    result.append( "Description: " + description + "\n" );
    result.append( "Information URL: " + infoUrl + "\n" );
    return result.toString();
  }
}

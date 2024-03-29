/******************************************************************************* 
* Copyright (c) 2012 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.core.model;

import java.io.InputStream;
import java.util.List;


public interface SourceUnMarshaller {

  SourceProvider marshal( InputStream sources );
  
  String unmarshal( List<Source> sources);
}

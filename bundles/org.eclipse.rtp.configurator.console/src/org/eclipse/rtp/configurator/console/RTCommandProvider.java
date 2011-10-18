/******************************************************************************* 
* Copyright (c) 2011 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/
package org.eclipse.rtp.configurator.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

public class RTCommandProvider implements CommandProvider {

  public static final String FAIL = "fail";
  private static final String INSTALL = "install";
  private static final String UPDATE = "update";
  private static final String REMOVE = "remove";
  private static final String SEARCH = "search";
  private static final String SHOW = "show";
  private static final String LIST = "list";
  private static final String UPDATE_WORLD = "update-world";
  private static final String REFRESH = "refresh";
  private CommandDelegate delegate;

  public RTCommandProvider( CommandDelegate delegate ) {
    this.delegate = delegate;
  }
  
  public RTCommandProvider() {
    this.delegate = new CommandDelegateImpl();
  }

  public Object _rt( CommandInterpreter interpreter ) {
    String operation = interpreter.nextArgument();
    String result = null;
    if( operation != null ) {
      distributeOperations( operation, interpreter );
    } else {
      result = FAIL;
    }
    return result;
  }

  private void distributeOperations( String operation, CommandInterpreter interpreter ) {
    List<String> parameter = extractParameter( interpreter ); 
    if( operation.equals( INSTALL ) ) {
      delegate.install( parameter );
    } else if( operation.equals( UPDATE ) ) {
      delegate.update( parameter );
    } else if( operation.equals( SEARCH ) ) {
      delegate.search( parameter );
    } else if( operation.equals( SHOW ) ) {
      delegate.show( parameter );
    } else if( operation.equals( LIST ) ) {
      delegate.list();
    } else if( operation.equals( UPDATE_WORLD ) ) {
      delegate.updateWorld();
    } else if( operation.equals( REFRESH ) ) {
      delegate.refresh();
    } else if( operation.equals( REMOVE ) ) {
      delegate.remove( parameter );
    } else {
      delegate.unsupportedOperation( operation );
    }
  }

  private List<String> extractParameter( CommandInterpreter interpreter ) {
    List<String> parameter = new ArrayList<String>();
    String argument;
    while( ( argument = interpreter.nextArgument() ) != null ) {
      parameter.add( argument );
    }
    return parameter;
  }

  @Override
  public String getHelp() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "---RT Commands---\n" );
    return buffer.toString();
  }
  
}

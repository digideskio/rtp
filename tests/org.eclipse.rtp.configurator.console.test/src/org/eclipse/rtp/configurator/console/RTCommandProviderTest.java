/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.console;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.junit.Before;
import org.junit.Test;

public class RTCommandProviderTest {

  private CommandDelegate fakeDelegate;
  private RTCommandProvider provider;
  private CommandInterpreter fakeInterpreter;
  private String nullArg;

  @Before
  public void fakeInterpreter() {
    fakeDelegate = mock( CommandDelegate.class );
    provider = new RTCommandProvider( fakeDelegate );
    fakeInterpreter = mock( CommandInterpreter.class );
    nullArg = null;
  }

  @Test
  public void failWithoutParam() {
    Object result = provider._rt( fakeInterpreter );
    assertEquals( RTCommandProvider.FAIL, result );
  }

  @Test
  public void withInvalidInstruction() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "asadf", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).unsupportedOperation( anyString() );
  }

  @Test
  public void withInstall() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "install", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).install( anyListOf( String.class ) );
  }

  @Test
  public void withUpdate() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "update", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).update( anyListOf( String.class ) );
  }

  @Test
  public void withRemove() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "remove", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).remove( anyListOf( String.class ) );
  }

  @Test
  public void withSearch() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "search", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).search( anyListOf( String.class ) );
  }

  @Test
  public void withShow() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "show", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).show( anyListOf( String.class ) );
  }

  @Test
  public void withList() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "list", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).list();
  }

  @Test
  public void withUpdateWorld() {
    when( fakeInterpreter.nextArgument() ).thenReturn( "update-world", nullArg );
    provider._rt( fakeInterpreter );
    verify( fakeDelegate ).updateWorld();
  }
}

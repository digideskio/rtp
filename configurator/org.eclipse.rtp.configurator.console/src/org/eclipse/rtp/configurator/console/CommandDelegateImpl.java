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

import java.util.List;

import org.eclipse.rtp.configurator.core.IConfiguratorService;


public class CommandDelegateImpl implements CommandDelegate {

  private static IConfiguratorService configurationService;

  public static void setUp(IConfiguratorService service){
    CommandDelegateImpl.configurationService = service;
  }
  
  public static void shutDown(IConfiguratorService service){
    CommandDelegateImpl.configurationService = null;
  }
  
  @Override
  public void unsupportedOperation( String operation ) {
    /*
     * Should print out a message with a help how to use the rt command.
     */
  }

  @Override
  public void install( List<String> parameter ) {
    /*
     * Should install an iu to the latest version. The name of the iu ist the first list entry.
     * If the secodn entry is not a OSGI.version than the latest should be installed.
     */
    configurationService.install( parameter );
  }

  @Override
  public void update( List<String> anyListOf ) {
    /*
     * Updates a specific componennt. Same parameter as install
     */
    configurationService.update( anyListOf );
  }

  @Override
  public void remove( List<String> anyListOf ) {
    /*
     * removes a spcific compoment. Same parameter as install
     */
    configurationService.remove( anyListOf );
  }

  @Override
  public void search( List<String> anyListOf ) {
    /*
     * Should list the available components in the rtp repos which where searched. 
     * A phonetic search should be done.
     */
    configurationService.search( anyListOf );
  }

  @Override
  public void show( List<String> anyListOf ) {
    /*
     * Show details of a specific component e.g. Dependencies, available version, size and so on.
     * Only one parameter = existing name of component
     */
    configurationService.show( anyListOf );
  }

  @Override
  public void list() {
    /*
     * Should list all IUs in the rtp repos.
     */
    configurationService.list();
  }

  @Override
  public void updateWorld() {
    /*
     * Update the whole system to the latest version. No Parameter
     */
  }

  @Override
  public void refresh() {
    /*
     * Should refresh the avalaible repos.
     */
    configurationService.refresh();
  }
  
}

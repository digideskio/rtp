/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 * 
 * Contributors:
 *     EclipseSource - initial API and implementation
 *     SAP AG - fix for bug 382043
 *******************************************************************************/
package org.eclipse.rtp.configurator.console;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.rtp.configurator.console.internal.CommandUtil;
import org.eclipse.rtp.core.RuntimeProvisioningService;
import org.eclipse.rtp.core.model.SourceVersion;

public class CommandDelegateImpl implements CommandDelegate {

  private static RuntimeProvisioningService configurationService;
  private static CommandUtil commandUtil;
  private final static String TAB = "\t"; //$NON-NLS-1$
  private final static String NEW_LINE = "\r\n"; //$NON-NLS-1$

  public static void setUp( RuntimeProvisioningService service ) {
    CommandDelegateImpl.configurationService = service;
    commandUtil = new CommandUtil();
  }

  public static void shutDown( RuntimeProvisioningService service ) {
    CommandDelegateImpl.configurationService = null;
  }

  @Override
  public void unsupportedOperation( String operation ) {
    /*
     * Should print out a message with a help how to use the rt command.
     */
    String help = getHelp();
    System.out.println( help );
  }

  @Override
  public String getHelp() {
    StringBuilder help = new StringBuilder();
    addRTHeader( help );
    addHeader( "Update Commands", help );
    addCommand( "install",
                "Installs a feature. The name of the feature is the first entry. If "
                    + "the secodn entry is not an OSGI version than the latest should be installed.",
                help );
    addCommand( "update",
                "Updates a feature. The name of the feature is the first entry. The feature is updated to its latest version.",
                help );
    addCommand( "update-world", "Updates all installed feature to their latest version.", help );
    addCommand( "remove", "the first entry is the feature which should be removed.", help );
    addHeader( "Search Commands", help );
    addCommand( "list", "Lists all available features in the rtp repos.", help );
    addCommand( "show",
                "Shows details of a specific component e.g. dependencies, available versions, size and so on. The feature name is the first entry.",
                help );
    addCommand( "search",
                "Lists the available components in the rtp repos which where searched. A phonetic search should is done.",
                help );
    return help.toString();
  }

  private void addRTHeader( StringBuilder help ) {
    help.append( "---RT Commands---" ); //$NON-NLS-1$
    help.append( NEW_LINE );
  }

  private void addHeader( String header, StringBuilder help ) {
    help.append( "  -" ); //$NON-NLS-1$
    help.append( header );
    help.append( "-" ); //$NON-NLS-1$
    help.append( NEW_LINE );
  }

  private void addCommand( String command, String description, StringBuilder help ) {
    help.append( TAB );
    help.append( command );
    help.append( " - " ); //$NON-NLS-1$
    help.append( description );
    help.append( NEW_LINE );
  }

  @Override
  public void install( List<String> parameter ) {
    /*
     * Should install an iu to the latest version. The name of the iu ist the first list entry. If
     * the secodn entry is not a OSGI.version than the latest should be installed.
     */
    SourceVersion sourceVersion = commandUtil.getSourceVersions( parameter );
    System.out.println( "Loading repository: " + sourceVersion.getRepositoryUrl() );
    System.out.println( "Repository loaded: " + sourceVersion.getRepositoryUrl() );
    System.out.println( "Installation started" );
    IStatus status = configurationService.install( sourceVersion );
    if( status.isOK() ) {
      System.out.println( "Installation successful" );
    } else {
      System.out.println( "Installation failed" );
    }
  }

  @Override
  public void update( List<String> anyListOf ) {
    /*
     * Updates a specific componennt. Same parameter as install
     */
    System.out.println( "Searching for updates" );
    System.out.println( "Update started" );
    configurationService.update( anyListOf );
    System.out.println( "Update successful" );
  }

  @Override
  public void remove( List<String> anyListOf ) {
    /*
     * removes a spcific compoment. Same parameter as install
     */
    List<SourceVersion> sourceVersionsToUninstall = commandUtil.getSourceVersionsToUninstall( anyListOf );
    configurationService.remove( sourceVersionsToUninstall );
  }

  @Override
  public void search( List<String> anyListOf ) {
    /*
     * Should list the available components in the rtp repos which where searched. A phonetic search
     * should be done.
     */
    List<String> search = configurationService.search( anyListOf );
    printList( search );
  }

  @Override
  public void show( List<String> anyListOf ) {
    /*
     * Show details of a specific component e.g. Dependencies, available version, size and so on.
     * Only one parameter = existing name of component
     */
    List<String> show = configurationService.show( anyListOf );
    printList( show );
  }

  /**
   * Lists all IUs in the rtp repos.
   */
  @Override
  public void list() {
    try {
      List<String> list = configurationService.list();
      printList( list );
    } catch( CoreException e ) {
      IStatus status = e.getStatus();
      System.out.println( status );
    }
  }

  private void printList( List<String> list ) {
    for( String iu : list ) {
      System.out.println( iu );
    }
  }

  @Override
  public void updateWorld() {
    /*
     * Update the whole system to the latest version. No Parameter
     */
    System.out.println( "Update started" );
    configurationService.updateWorld();
    System.out.println( "Update successful" );
  }
}

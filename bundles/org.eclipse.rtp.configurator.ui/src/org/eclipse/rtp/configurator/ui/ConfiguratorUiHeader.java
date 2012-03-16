/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.rtp.configurator.rest.IEventService;
import org.eclipse.rtp.configurator.rest.RestTemplate;
import org.eclipse.rtp.configurator.ui.internal.event.ConfigurationEvent;
import org.eclipse.rtp.configurator.ui.internal.event.EventingServiceUtil;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.widgets.DialogUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Constructs the Configurator UI header.
 * 
 * @author fluffi
 */
public class ConfiguratorUiHeader {

  private Label configurationUriLabel;
  private Composite header;
  private Image logo;

  public void createHeader( final Display display, Shell shell ) {
    header = UiHelper.createGridComposite( shell, 4, false );
    logo = new Image( display, getClass().getResourceAsStream( "/images/rtp-icon.png" ) );
    Label label = new Label( header, SWT.CENTER );
    label.setImage( logo );
    configurationUriLabel = new Label( header, SWT.NONE );
    configurationUriLabel.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    Label headerLabel = UiHelper.createLabel( header, 1, new UIBrandingWeb().getTitle() );
    headerLabel.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    createConfigurationButton( display, header );
  }

  private void createConfigurationButton( final Display display, Composite header ) {
    final Button rtpInstanceManagementButton = new Button( header, SWT.NONE );
    rtpInstanceManagementButton.setText( "manage" );
    addConfiguratinListener( display, rtpInstanceManagementButton );
  }

  private void addConfiguratinListener( final Display display,
                                        final Button rtpInstanceManagementButton )
  {
    rtpInstanceManagementButton.addSelectionListener( new SelectionAdapter() {

      @SuppressWarnings( "synthetic-access" )
      @Override
      public void widgetSelected( SelectionEvent e ) {
        showInputDialog();
      }
    } );
  }

  private void showInputDialog() {
    final InputDialog inputDialog = new InputDialog( Display.getCurrent().getActiveShell(),
                                                     "RTP Instance Configuration",
                                                     "Enter instance URI",
                                                     "",
                                                     null )
    {

      @SuppressWarnings( "synthetic-access" )
      @Override
      protected void okPressed() {
        super.okPressed();
        if( isValidLocation( getValue() ) ) {
          configurationUriLabel.setText( getValue() );
          fireConfigurationChagned( getValue() );
        } else {
          showWarningDialog();
        }
      }
    };
    inputDialog.setBlockOnOpen( false );
    inputDialog.open();
  }

  private boolean isValidLocation( String configurationURI ) {
    boolean result = false;
    try {
      // do a test rest call to test the entered URI.
      RestTemplate restTemplate = new RestTemplate( configurationURI );
      restTemplate.getForEntitiesAsList( "/rt/list", Source.class );
      result = true;
    } catch( Exception e ) {
      result = false;
    }
    return result;
  }

  private void showWarningDialog() {
    MessageBox infoDialog = new MessageBox( Display.getDefault().getActiveShell(),
                                            SWT.OK | SWT.ICON_WARNING | SWT.APPLICATION_MODAL );
    infoDialog.setText( "Invalide Location" );
    infoDialog.setMessage( "No instance found." );
    DialogUtil.open( infoDialog, null );
  }

  private void fireConfigurationChagned( String value ) {
    ConfigurationEvent configurationEvent = new ConfigurationEvent( value, RWT.getSessionStore()
      .getId() );
    IEventService eventService = EventingServiceUtil.getEventService();
    eventService.fireConfigurationEvent( configurationEvent );
  }

  public void dispose() {
    if( logo != null ) {
      logo.dispose();
    }
  }
}

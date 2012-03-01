/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.Collection;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.rtp.configurator.ui.internal.event.ConfigurationEvent;
import org.eclipse.rtp.configurator.ui.internal.event.IConfigurationListener;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * Constructs the Configurator UI header.
 * 
 * @author fluffi
 */
public class ConfiguratorUiHeader {

  private Label configurationUriLabel;
  private Composite header;

  public void createHeader( final Display display, Shell shell ) {
    header = UiHelper.createGridComposite( shell, 5, false );
    Image logo = new Image( display, getClass().getResourceAsStream( "/images/rtp-icon.png" ) );
    Label label = new Label( header, SWT.CENTER );
    label.setImage( logo );
    configurationUriLabel = new Label( header, SWT.NONE );
    configurationUriLabel.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, false, false ) );
    Label headerLabel = UiHelper.createLabel( header, 2, new UIBrandingWeb().getTitle() );
    headerLabel.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, false ) );
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

      @Override
      public void widgetSelected( SelectionEvent e ) {
        final InputDialog inputDialog = new InputDialog( Display.getCurrent().getActiveShell(),
                                                         "RTP Instance Configuration",
                                                         "Enter instance URI",
                                                         "",
                                                         null )
        {

          @Override
          protected void okPressed() {
            super.okPressed();
            configurationUriLabel.setText( getValue() );
            header.layout( true );
            fireConfigurationChagned( getValue() );
          }

          private void fireConfigurationChagned( String value ) {
            try {
              ConfigurationEvent configurationEvent = new ConfigurationEvent( value,
                                                                              RWT.getSessionStore()
                                                                                .getId() );
              Bundle bundle = Platform.getBundle( "org.eclipse.rtp.configurator.ui" );
              Collection<ServiceReference<IConfigurationListener>> serviceReferences = bundle.getBundleContext()
                .getServiceReferences( IConfigurationListener.class, null );
              for( ServiceReference<IConfigurationListener> serviceReference : serviceReferences ) {
                IConfigurationListener service = bundle.getBundleContext()
                  .getService( serviceReference );
                service.configurationchanged( configurationEvent );
                bundle.getBundleContext().ungetService( serviceReference );
              }
            } catch( InvalidSyntaxException e ) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        };
        inputDialog.setBlockOnOpen( false );
        inputDialog.open();
      }
    } );
  }
}

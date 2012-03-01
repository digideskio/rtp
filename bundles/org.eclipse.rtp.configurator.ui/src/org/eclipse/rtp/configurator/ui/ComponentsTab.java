/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rtp.configurator.rest.RestTemplate;
import org.eclipse.rtp.configurator.ui.internal.event.IConfigurationEvent;
import org.eclipse.rtp.configurator.ui.internal.event.IConfigurationListener;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;

public class ComponentsTab extends AbstractTabContribution {

  private final SourcesContentProvider contentProvider = new SourcesContentProvider();
  private RestTemplate restTemplate;
  protected static String[] comboLabels = new String[]{
    "all", "installed", "uninstalled"
  };
  private final Map<String, String> listMapping = new HashMap<String, String>() {

    {
      put( comboLabels[ 0 ], "/rt/list" );
      put( comboLabels[ 1 ], "/rt/list/installed" );
      put( comboLabels[ 2 ], "/rt/list/uninstaller" );
    }
  };
  TreeViewer viewer;
  private Combo combo;
  private Button addSource;
  private Button removeSource;
  private Button updateWorld;
  private List<Source> sources;
  private List<Source> installedSources;
  private Display display;

  @Override
  public String getTitle() {
    return "Runtime Components";
  }

  @Override
  @SuppressWarnings( "serial" )
  protected void populateControl( Display display, Composite composite ) {
    this.display = display;
    Composite tab = UiHelper.createGreedyGridComposite( composite, 1, true );
    Composite tabToolbarComposite = UiHelper.createGridComposite( tab, 5, false );
    Label searchLabel = new Label( tabToolbarComposite, SWT.NONE );
    searchLabel.setText( "Search: " );
    final Text filterText = UiHelper.createText( tabToolbarComposite, 1, "Please enter filter" );
    final ComponentsFilter filter = new ComponentsFilter();
    filterText.addKeyListener( new KeyAdapter() {

      @Override
      public void keyReleased( KeyEvent ke ) {
        filter.setSearchText( filterText.getText() );
        viewer.refresh();
      }
    } );
    Label filterLabel = new Label( tabToolbarComposite, SWT.NONE );
    filterLabel.setText( "Filter: " );
    combo = UiHelper.createComboBox( tabToolbarComposite, 1, comboLabels );
    combo.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        refresh();
      }
    } );
    Button refreshButton = UiHelper.createPushButton( tabToolbarComposite, "refresh" );
    refreshButton.addSelectionListener( new RefreshButtonSelectionListener( this ) );
    Composite treeComposite = UiHelper.createGreedyGridComposite( tab, 1, true );
    viewer = UiHelper.createTreeViewer( new Composite( treeComposite, SWT.NONE ) );
    viewer.addFilter( filter );
    ISelectionChangedListener listener = new ISelectionChangedListener() {

      @Override
      public void selectionChanged( SelectionChangedEvent event ) {
        Object eventSource = event.getSource();
        TreeViewer viewer = ( TreeViewer )eventSource;
        ISelection selection = viewer.getSelection();
        TreeSelection treeSelection = ( TreeSelection )selection;
        Object selectedElement = treeSelection.getFirstElement();
        updateButtons( selectedElement );
      }
    };
    viewer.addSelectionChangedListener( listener );
    viewer.setContentProvider( contentProvider );
    SourcesLabelProvider labelProvider = new SourcesLabelProvider( this );
    labelProvider.init( display );
    viewer.setLabelProvider( labelProvider );
    viewer.setInput( new ArrayList<Source>() );
    Composite provisioningActionsComposite = UiHelper.createGridComposite( tab, 2, false );
    addInstallButton( provisioningActionsComposite );
    addUninstallButton( provisioningActionsComposite );
    addUpdateWorldButton( provisioningActionsComposite );
    addConfigurationListener();
  }

  private void addUninstallButton( Composite tab ) {
    removeSource = UiHelper.createPushButton( tab, 1, "remove" );
    removeSource.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        restTemplate.delete( "/rt/uninstall" + generateSelectedSourceUri() );
      }
    } );
  }

  private void addUpdateWorldButton( Composite tab ) {
    updateWorld = UiHelper.createPushButton( tab, 2, "updateWorld" );
    updateWorld.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        restTemplate.put( "/rt/updateworld" );
      }
    } );
  }

  private void addInstallButton( Composite tab ) {
    addSource = UiHelper.createPushButton( tab, 1, "add" );
    addSource.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        String uri = "/rt/install" + generateSelectedSourceUri();
        restTemplate.put( uri );
      }
    } );
  }

  void updateButtons( Object selectedElement ) {
    SourceVersion sourceVersion;
    if( selectedElement instanceof Source ) {
      sourceVersion = ( ( Source )selectedElement ).getVersions().get( 0 );
    } else {
      sourceVersion = ( SourceVersion )selectedElement;
    }
    if( isInstalled( sourceVersion ) ) {
      addSource.setEnabled( false );
      removeSource.setEnabled( true );
    } else {
      addSource.setEnabled( true );
      removeSource.setEnabled( false );
    }
  }

  public boolean isInstalled( SourceVersion sourceVersion ) {
    boolean result = false;
    for( int i = 0; i < installedSources.size() && result == false; i++ ) {
      Source source = installedSources.get( i );
      result = source.getVersions().contains( sourceVersion );
    }
    return result;
  }

  String generateSelectedSourceUri() {
    String version = null;
    String name = null;
    Object selectedElement = viewer.getTree().getSelection()[ 0 ].getData();
    if( selectedElement instanceof Source ) {
      version = ( ( Source )selectedElement ).getVersions().get( 0 ).getVersion();
      name = ( ( Source )selectedElement ).getName();
    } else if( selectedElement instanceof SourceVersion ) {
      version = ( ( SourceVersion )selectedElement ).getVersion();
      name = getSourceVersionSource( ( SourceVersion )selectedElement ).getName();
    }
    String uri = "/" + name + "/" + version;
    return uri;
  }

  private Source getSourceVersionSource( SourceVersion sourceVersion ) {
    Source result = null;
    if( sources != null ) {
      for( Source source : sources ) {
        List<SourceVersion> versions = source.getVersions();
        for( SourceVersion version : versions ) {
          if( version.equals( sourceVersion ) ) {
            result = source;
          }
        }
      }
    }
    return result;
  }

  private void addConfigurationListener() {
    Bundle bundle = Platform.getBundle( "org.eclipse.rtp.configurator.ui" );
    IConfigurationListener configurationListener = new IConfigurationListener() {

      @Override
      public void configurationchanged( IConfigurationEvent event ) {
        restTemplate = new RestTemplate( event.getNewIntanceURI() );
        refresh();
      }
    };
    bundle.getBundleContext().registerService( IConfigurationListener.class,
                                               configurationListener,
                                               null );
  }

  public void refresh() {
    if( restTemplate != null && !display.isDisposed() ) {
      display.asyncExec( new Runnable() {

        @Override
        public void run() {
          int selectionIndex = combo.getSelectionIndex();
          sources = restTemplate.getForEntitiesAsList( listMapping.get( ComponentsTab.comboLabels[ selectionIndex ] ),
                                                       Source.class );
          installedSources = restTemplate.getForEntitiesAsList( listMapping.get( ComponentsTab.comboLabels[ 1 ] ),
                                                                Source.class );
          viewer.setInput( sources );
          addSource.setEnabled( false );
          removeSource.setEnabled( false );
        }
      } );
    }
  }
}

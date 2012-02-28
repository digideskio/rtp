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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rtp.configurator.rest.RestTemplate;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class ComponentsTab extends AbstractTabContribution {

  private SourcesContentProvider contentProvider = new SourcesContentProvider();
  RestTemplate restTemplate = new RestTemplate();
  protected static String[] comboLabels = new String[]{
    "all", "installed", "uninstalled"
  };
  private Map<String, String> listMapping = new HashMap<String, String>() {

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

  @Override
  public String getTitle() {
    return "Runtime Components";
  }

  @Override
  @SuppressWarnings( "serial" )
  protected void populateControl( Display display, Composite composite ) {
    Composite tab = UiHelper.createGreedyGridComposite( composite, 4 );
    Button refreshButton = UiHelper.createPushButton( tab, "refresh" );
    refreshButton.addSelectionListener( new RefreshButtonSelectionListener( this ) );
    final Text filterText = UiHelper.createText( tab, 1, "Please enter filter" );
    final ComponentsFilter filter = new ComponentsFilter();
    filterText.addKeyListener( new KeyAdapter() {

      @Override
      public void keyReleased( KeyEvent ke ) {
        filter.setSearchText( filterText.getText() );
        viewer.refresh();
      }
    } );
    UiHelper.createLabel( tab, 1, "Show:" );
    combo = UiHelper.createComboBox( tab, 1, comboLabels );
    combo.addSelectionListener( new SelectionListener() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        refresh();
      }

      @Override
      public void widgetDefaultSelected( SelectionEvent e ) {
      }
    } );
    viewer = UiHelper.createTreeViewer( tab );
    viewer.addFilter( filter );
    ISelectionChangedListener listener = new ISelectionChangedListener() {

      @Override
      public void selectionChanged( SelectionChangedEvent event ) {
        Object eventSource = event.getSource();
        TreeViewer viewer = ( TreeViewer )eventSource;
        ISelection selection = viewer.getSelection();
        TreeSelection treeSelection = ( TreeSelection )selection;
        Source source = ( Source )treeSelection.getFirstElement();
        updateButtons( source );
      }
    };
    viewer.addSelectionChangedListener( listener );
    viewer.setContentProvider( contentProvider );
    SourcesLabelProvider labelProvider = new SourcesLabelProvider();
    labelProvider.init( display );
    viewer.setLabelProvider( labelProvider );
    viewer.setInput( new ArrayList<Source>() );
    addInstallButton( tab );
    addUninstallButton( tab );
    addUodateWorldButton( tab );
  }

  private void addUninstallButton( Composite tab ) {
    removeSource = UiHelper.createPushButton( tab, 2, "remove" );
    removeSource.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        restTemplate.delete( "/rt/uninstall" + generateSelectedSourceUri() );
      }
    } );
  }

  private void addUodateWorldButton( Composite tab ) {
    updateWorld = UiHelper.createPushButton( tab, 4, "updateWorld" );
    updateWorld.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        restTemplate.delete( "/rt/updateworld" );
      }
    } );
    refresh();
  }

  private void addInstallButton( Composite tab ) {
    addSource = UiHelper.createPushButton( tab, 2, "add" );
    addSource.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        String uri = "/rt/install" + generateSelectedSourceUri();
        restTemplate.put( uri );
      }
    } );
  }

  void updateButtons( Source source ) {
    if( isInstalled( source ) ) {
      addSource.setEnabled( false );
      removeSource.setEnabled( true );
    } else {
      addSource.setEnabled( true );
      removeSource.setEnabled( false );
    }
  }

  private boolean isInstalled( Source source ) {
    List<Source> installedSources = new RestTemplate().getForEntitiesAsList( listMapping.get( ComponentsTab.comboLabels[ 1 ] ),
                                                                             Source.class );
    return installedSources.contains( source );
  }

  String generateSelectedSourceUri() {
    Source source = ( Source )viewer.getTree().getSelection()[ 0 ].getData();
    String version = source.getVersions().get( 0 ).getVersion();
    String uri = "/" + source.getName() + "/" + version;
    return uri;
  }

  public void refresh() {
    int selectionIndex = this.combo.getSelectionIndex();
    List<Source> sources = new RestTemplate().getForEntitiesAsList( listMapping.get( ComponentsTab.comboLabels[ selectionIndex ] ),
                                                                    Source.class );
    viewer.setInput( sources );
    addSource.setEnabled( false );
    removeSource.setEnabled( false );
  }
}

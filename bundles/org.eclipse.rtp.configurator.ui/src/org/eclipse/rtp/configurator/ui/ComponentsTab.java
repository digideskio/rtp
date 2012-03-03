/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rtp.configurator.ui.internal.event.EventingServiceUtil;
import org.eclipse.rtp.configurator.ui.internal.event.IConfigurationEvent;
import org.eclipse.rtp.configurator.ui.internal.event.IConfigurationListener;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceVersion;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ComponentsTab extends AbstractTabContribution {

  private final SourcesContentProvider contentProvider = new SourcesContentProvider();
  TreeViewer viewer;
  private Combo combo;
  private Button addSource;
  private Button removeSource;
  private Button updateWorld;
  private Display display;
  ComponentsTabContentUtil contentUtil;

  public ComponentsTab() {
    contentUtil = new ComponentsTabContentUtil();
  }

  @Override
  public String getTitle() {
    return "Runtime Components";
  }

  @Override
  @SuppressWarnings( "serial" )
  protected void populateControl( Display display, Composite composite ) {
    this.display = display;
    Composite tab = UiHelper.createGreedyGridComposite( composite, 1, true );
    final ComponentsFilter filter = createToolBar( tab );
    createTreeView( display, tab, filter );
    Composite provisioningActionsComposite = UiHelper.createGridComposite( tab, 2, false );
    addInstallButton( provisioningActionsComposite );
    addUninstallButton( provisioningActionsComposite );
    addUpdateWorldButton( provisioningActionsComposite );
    registerTabForConfigurationChanges();
  }

  private ComponentsFilter createToolBar( Composite tab ) {
    Composite tabToolbarComposite = UiHelper.createGridComposite( tab, 3, true );
    Composite searchComposite = new Composite( tabToolbarComposite, SWT.NONE );
    searchComposite.setLayout( new GridLayout( 2, false ) );
    searchComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    Label searchLabel = new Label( searchComposite, SWT.NONE );
    searchLabel.setText( "Search: " );
    final Text filterText = UiHelper.createText( searchComposite, 1, "Please enter filter" );
    final ComponentsFilter filter = new ComponentsFilter();
    filterText.addKeyListener( new KeyAdapter() {

      @Override
      public void keyReleased( KeyEvent ke ) {
        filter.setSearchText( filterText.getText() );
        viewer.refresh();
      }
    } );
    Composite filterComposite = new Composite( tabToolbarComposite, SWT.NONE );
    filterComposite.setLayout( new GridLayout( 2, false ) );
    filterComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    Label filterLabel = new Label( filterComposite, SWT.NONE );
    filterLabel.setText( "Filter: " );
    combo = UiHelper.createComboBox( filterComposite, 1, contentUtil.getComboLabels() );
    combo.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        refresh();
      }
    } );
    Composite refreshComposite = new Composite( tabToolbarComposite, SWT.NONE );
    refreshComposite.setLayout( new GridLayout( 2, true ) );
    refreshComposite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    new Label( refreshComposite, SWT.NONE );
    Button refreshButton = UiHelper.createPushButton( refreshComposite, "refresh" );
    refreshButton.addSelectionListener( new RefreshButtonSelectionListener( this ) );
    return filter;
  }

  private void createTreeView( Display display, Composite tab, final ComponentsFilter filter ) {
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
    SourcesLabelProvider labelProvider = new SourcesLabelProvider( contentUtil );
    labelProvider.init( display );
    viewer.setLabelProvider( labelProvider );
    viewer.setInput( new ArrayList<Source>() );
  }

  private void registerTabForConfigurationChanges() {
    IConfigurationListener configurationListenerService = EventingServiceUtil.getConfigurationListenerService();
    configurationListenerService.addInterestedView( RWT.getSessionStore().getId(), this );
  }

  private void addUninstallButton( Composite tab ) {
    removeSource = UiHelper.createPushButton( tab, 1, "remove" );
    removeSource.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        contentUtil.getRestTemplate().delete( "/rt/uninstall" + generateSelectedSourceUri() );
      }
    } );
  }

  private void addUpdateWorldButton( Composite tab ) {
    updateWorld = UiHelper.createPushButton( tab, 2, "updateWorld" );
    updateWorld.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        contentUtil.getRestTemplate().put( "/rt/updateworld" );
      }
    } );
  }

  private void addInstallButton( Composite tab ) {
    addSource = UiHelper.createPushButton( tab, 1, "add" );
    addSource.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        String uri = "/rt/install" + generateSelectedSourceUri();
        contentUtil.getRestTemplate().put( uri );
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
    if( contentUtil.isInstalled( sourceVersion ) ) {
      addSource.setEnabled( false );
      removeSource.setEnabled( true );
    } else {
      addSource.setEnabled( true );
      removeSource.setEnabled( false );
    }
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
      name = contentUtil.getSourceVersionSource( ( SourceVersion )selectedElement ).getName();
    }
    String uri = "/" + name + "/" + version;
    return uri;
  }

  public void configurationChanged( IConfigurationEvent event ) {
    contentUtil.setConfigurationURI( event.getNewIntanceURI() );
    refresh();
  }

  public void refresh() {
    if( contentUtil != null && !display.isDisposed() ) {
      display.asyncExec( new Runnable() {

        @Override
        public void run() {
          int selectionIndex = combo.getSelectionIndex();
          contentUtil.refresh( contentUtil.getComboLabels()[ selectionIndex ] );
          viewer.setInput( contentUtil.getSourcec() );
          addSource.setEnabled( false );
          removeSource.setEnabled( false );
        }
      } );
    }
  }

  @Override
  public void dispose() {
    IConfigurationListener configurationListenerService = EventingServiceUtil.getConfigurationListenerService();
    configurationListenerService.removeInterestedView( RWT.getSessionStore().getId() );
  }
}

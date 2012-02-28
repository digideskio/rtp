/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rtp.core.model.Source;
import org.eclipse.rtp.core.model.SourceUnMarshaller;
import org.eclipse.rtp.core.model.internal.SourceUnMarshallerImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class ComponentsTab extends AbstractTabContribution {

  // TODO use declarative service here
  SourceUnMarshaller unMarshaller = new SourceUnMarshallerImpl() ;

  TreeViewer viewer;

  private SourcesContentProvider contentProvider = new SourcesContentProvider();

  @Override
  public String getTitle() {
    return "Runtime Components";
  }

  @Override
  @SuppressWarnings( "serial" )
  protected void populateControl( Display display, Composite composite ) {
    Composite tab = UiHelper.createGreedyGridComposite( composite, 4 );

    Button refreshButton = UiHelper.createPushButton( tab, "refresh" );
    refreshButton.addSelectionListener( new RefreshButtonSelectionListener(this) );

    final Text filterText = UiHelper.createText( tab, 1, "Please enter filter" );
    final ComponentsFilter filter = new ComponentsFilter();
    filterText.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent ke) {
            filter.setSearchText(filterText.getText());
            viewer.refresh();
        }

    });

    UiHelper.createLabel( tab, 1, "Show:" );
    UiHelper.createComboBox( tab, 1, "all", "installed" );
    viewer = new TreeViewer( tab, SWT.H_SCROLL | SWT.V_SCROLL );
    
    viewer.addFilter( filter );
    
    Tree tree = viewer.getTree();
    tree.setHeaderVisible( true );
    tree.setLinesVisible( true );
    GridData layoutData = new GridData( SWT.FILL, SWT.FILL, true, true );
    layoutData.horizontalSpan = 4;
    tree.setLayoutData( layoutData );
    TreeColumn column0 = new TreeColumn( viewer.getTree(), SWT.LEFT );
    column0.setText( "Component" );
    column0.setAlignment( SWT.LEFT );
    column0.setWidth( 300 );
    TreeColumn column1 = new TreeColumn( viewer.getTree(), SWT.LEFT );
    column1.setText( "Version" );
    column1.setAlignment( SWT.LEFT );
    column1.setWidth( 260 );
    viewer.setContentProvider( contentProvider );
    SourcesLabelProvider labelProvider = new SourcesLabelProvider();
    labelProvider.init( display );
    viewer.setLabelProvider( labelProvider );
    viewer.setInput( new ArrayList<Source>() );
  }

  public void refresh( List<Source> sources ) {
    viewer.setInput( sources );
  }
}

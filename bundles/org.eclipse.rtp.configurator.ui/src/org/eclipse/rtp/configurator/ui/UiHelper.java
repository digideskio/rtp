/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: EclipseSource - initial API and
 * implementation
 *******************************************************************************/
package org.eclipse.rtp.configurator.ui;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class UiHelper {

  private UiHelper() {
  }

  public static Shell createShell( Display display ) {
    Shell shell = new Shell( display, SWT.NO_TRIM );
    shell.setMaximized( true );
    shell.setLayout( UiHelper.createGreedyGridLayout() );
    return shell;
  }

  public static Button createPushButton( Composite composite, String text ) {
    return createPushButton( composite, 1, text );
  }

  public static Button createPushButton( Composite composite, int horizontalSpan, String text ) {
    Button button = new Button( composite, SWT.PUSH );
    button.setLayoutData( UiHelper.createOneLinerGridData( horizontalSpan ) );
    button.setText( text );
    return button;
  }

  public static Label createLabel( Composite composite, int horizontalSpan, String text ) {
    Label label = new Label( composite, SWT.INHERIT_DEFAULT );
    label.setText( text );
    label.setLayoutData( UiHelper.createOneLinerGridData( horizontalSpan ) );
    return label;
  }

  public static TreeViewer createTreeViewer( Composite tab ) {
    TreeColumnLayout treeColumnLayout = new TreeColumnLayout();
    tab.setLayout( treeColumnLayout );
    tab.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    TreeViewer viewer = new TreeViewer( tab, SWT.H_SCROLL | SWT.V_SCROLL );
    Tree tree = viewer.getTree();
    tree.setHeaderVisible( true );
    tree.setLinesVisible( true );
    GridData layoutData = new GridData( SWT.FILL, SWT.FILL, true, true );
    layoutData.horizontalSpan = 4;
    tree.setLayoutData( layoutData );
    TreeColumn column0 = new TreeColumn( viewer.getTree(), SWT.LEFT );
    column0.setText( "Component" );
    column0.setAlignment( SWT.LEFT );
    treeColumnLayout.setColumnData( column0, new ColumnWeightData( 1, true ) );
    column0.setWidth( 300 );
    TreeColumn column1 = new TreeColumn( viewer.getTree(), SWT.LEFT );
    column1.setText( "Repository" );
    column1.setAlignment( SWT.LEFT );
    treeColumnLayout.setColumnData( column1, new ColumnWeightData( 4, true ) );
    TreeColumn column3 = new TreeColumn( viewer.getTree(), SWT.LEFT );
    column3.setText( "Status" );
    column3.setAlignment( SWT.LEFT );
    treeColumnLayout.setColumnData( column3, new ColumnWeightData( 1, true ) );
    return viewer;
  }

  public static Tree createTree( Composite composite ) {
    Tree tree = new Tree( composite, SWT.BORDER );
    tree.setLayoutData( UiHelper.createGreedyGridData() );
    tree.setHeaderVisible( true );
    tree.setLinesVisible( true );
    TreeColumn column0 = new TreeColumn( tree, SWT.LEFT );
    column0.setText( "Name" );
    column0.setAlignment( SWT.LEFT );
    column0.setWidth( 300 );
    TreeColumn column1 = new TreeColumn( tree, SWT.LEFT );
    column1.setText( "Description" );
    column1.setAlignment( SWT.LEFT );
    column1.setWidth( 260 );
    return tree;
  }

  public static GridData createGreedyGridData() {
    return new GridData( SWT.FILL, SWT.FILL, true, true );
  }

  public static GridData createOneLinerGridData( int horizontalSpan ) {
    GridData gridData = new GridData( SWT.FILL, SWT.NONE, true, false );
    gridData.horizontalSpan = horizontalSpan;
    return gridData;
  }

  public static Composite createGreedyGridComposite( Composite parent,
                                                     int numColumns,
                                                     boolean equalWidth )
  {
    Composite composite = new Composite( parent, SWT.INHERIT_DEFAULT );
    composite.setLayout( UiHelper.createGridLayout( numColumns, equalWidth ) );
    composite.setLayoutData( UiHelper.createGreedyGridData() );
    return composite;
  }

  public static Composite createGridComposite( Composite parent, int numColumns, boolean equalWidth )
  {
    Composite composite = new Composite( parent, SWT.INHERIT_DEFAULT );
    composite.setLayout( UiHelper.createGridLayout( numColumns, equalWidth ) );
    composite.setLayoutData( UiHelper.createOneLinerGridData( numColumns ) );
    return composite;
  }

  private static Layout createGridLayout( int numColumns, boolean equalWidth ) {
    GridLayout layout = new GridLayout( numColumns, equalWidth );
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    return layout;
  }

  public static Layout createGreedyGridLayout() {
    return createGridLayout( 1, true );
  }

  public static Text createText( Composite composite, String toolTip ) {
    return createText( composite, 1, toolTip );
  }

  public static Text createText( Composite parent, int horizontalSpan, String toolTip ) {
    Text text = new Text( parent, SWT.BORDER );
    text.setToolTipText( toolTip );
    text.setLayoutData( createOneLinerGridData( horizontalSpan ) );
    return text;
  }

  public static Combo createComboBox( Composite parent, int horizontalSpan, String... labels ) {
    Combo combo = new Combo( parent, SWT.READ_ONLY );
    combo.setItems( labels );
    combo.setLayoutData( createOneLinerGridData( horizontalSpan ) );
    combo.setText( labels[ 0 ] );
    return combo;
  }
}

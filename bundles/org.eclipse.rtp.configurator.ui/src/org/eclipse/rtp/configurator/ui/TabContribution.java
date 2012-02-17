/******************************************************************************* 
* Copyright (c) 2012 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rtp.configurator.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * A tab contribution is intended to be used inside an SWT tab.
 */
public interface TabContribution {

	/**
	 * Title shown in the enclosing tab folder.
	 * @return String to be shown in the enclosing tab folder.
	 */
	String getTitle();

	/**
	 * Provide the content for the tab item.
	 * @param display of the application.
	 * @param parent of the content to provide.
	 * @return the control to put into the tab item.
	 */
	Control getControl(Display display, Composite parent);

}

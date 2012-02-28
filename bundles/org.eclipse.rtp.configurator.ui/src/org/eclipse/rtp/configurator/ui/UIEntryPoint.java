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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UIEntryPoint implements IEntryPoint {

  private List<ContributionTab> contributions = new ArrayList<ContributionTab>();

  @Override
  public int createUI() {
    Display display = new Display();
    final Shell shell = UiHelper.createShell(display);

    new ConfiguratorUiHeader().createHeader(display, shell);
    contributions.add( new ComponentsTab());
    contributions.add( new ProvisioningTab());
    new ConfiguratorUiBody().createBody(shell, contributions);
    new ConfiguratorUiFooter().createFooter(shell);

    shell.open();
    return 0;
  }
}

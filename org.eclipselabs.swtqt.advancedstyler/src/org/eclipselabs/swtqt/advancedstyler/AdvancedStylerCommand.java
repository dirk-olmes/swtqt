/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.swtqt.advancedstyler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.PlatformUI;

/**
 *
 */
public class AdvancedStylerCommand extends AbstractHandler {

	private boolean isStyled = false;
	
	private AdvancedStylerWindow stylerWindow;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (stylerWindow == null || stylerWindow.isDisposed()) {
			Rectangle bounds = PlatformUI.getWorkbench().getDisplay().getActiveShell().getBounds();
			stylerWindow = new AdvancedStylerWindow(PlatformUI.getWorkbench().getDisplay().getActiveShell(), PlatformUI.getWorkbench().getDisplay(), bounds.x + bounds.width + 35, bounds.y);
		}
		stylerWindow.show();
		return null;
	}
}

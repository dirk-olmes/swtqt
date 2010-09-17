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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 *
 */
public class UIControlsFactory {
	private static final String STYLESHEET_KEY = "stylesheet";

	public static Button createCheckbox(Composite panel, String text) {
		Button button = new Button(panel, SWT.CHECK);
		button.setText(text);
		button.setData(STYLESHEET_KEY, "QCheckBox { spacing: 2px; padding-right: 2px; padding-bottom: 1px;}");
		return button;
	}

	public static Button createButton(Composite panel, String text) {
		Button button = new Button(panel, SWT.PUSH);
		button.setData(STYLESHEET_KEY, "QPushButton { padding: 5px 5px 4px 5px; }");
		button.setText(text);
		return button;
	}

	public static Button createRadioButton(Composite panel, String text) {
		Button button = new Button(panel, SWT.RADIO);
		button.setText(text);
		button.setData(STYLESHEET_KEY, "QRadioButton { padding-top: 1px; padding-right: 2px; spacing: 2px; }");
		return button;
	}

	public static Control createRadioButton(Composite panel, String text, boolean checked) {
		Button button = createRadioButton(panel, text);
		button.setSelection(checked);
		return button;
	}

	public static Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.None);
		label.setText(text);
		return label;
	}

	public static Control createText(Composite parent, boolean editable) {
		Text text = new Text(parent, SWT.BORDER);
		text.setEditable(editable);
		return text;
	}

	public static Control createSeperator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		return separator;
	}

	public static Combo createCombobox(Composite parent, String[] items) {
		Combo combo = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setItems(items);
		combo.setData(STYLESHEET_KEY, "QComboBox{ padding: 2px 12px 3px 2px; }");
		return combo;
	}

}

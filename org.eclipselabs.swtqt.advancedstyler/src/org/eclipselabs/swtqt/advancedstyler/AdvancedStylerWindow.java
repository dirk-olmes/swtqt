package org.eclipselabs.swtqt.advancedstyler;

import org.eclipselabs.swtqt.advancedstyler.CssUtil.Style;

import net.ffxml.swtforms.builder.DefaultFormBuilder;
import net.ffxml.swtforms.layout.FormLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class AdvancedStylerWindow {

	private Display display;
	private Shell shell;
	private Text textEditor;
	private Combo combo;
	private final Shell mainFrame;
	private RGB[] colorSlots = new RGB[5];

	public AdvancedStylerWindow(Shell mainFrame, Display display, int x, int y) {
		this.colorSlots[0] = new RGB(255, 255, 255);
		this.colorSlots[1] = new RGB(0, 0, 0);
		this.mainFrame = mainFrame;
		this.display = display;
		shell = new Shell(display);
		initShell(x, y);
		createForm();
		textEditor.setText(loadStyleFromFile());
	}

	public boolean isDisposed() {
		return shell.isDisposed();
	}

	protected void initShell(int x, int y) {
		shell.setText("AdvancedStyler");
		shell.setBounds(x, y, 360, 650);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
	}

	public void show() {
		shell.setVisible(true);
	}

	protected void updateGlobalStyle() {
		String styleText = textEditor.getText();
		if (colorSlots[0] != null && colorSlots[1] != null) {
			for (int i = 0; i < colorSlots.length; i++) {
				if (colorSlots[i] != null) {
					String hexString = convertRgb2Hex(colorSlots[i]);
					while (styleText.contains("$color" + i + "$")) {
						styleText = styleText.replace("$color" + i + "$", hexString);
					}
				}
			}
		}
		display.setData("stylesheet", styleText);
		//		mainFrame.pack();
	}

	private String convertRgb2Hex(RGB color) {
		return "#" + toHex(color.red) + toHex(color.green) + toHex(color.blue);
	}

	private String toHex(int color) {
		String hex = Integer.toHexString(color);
		if (hex.length() < 2) {
			return "0" + hex;
		}
		return hex;
	}

	protected void resetGlobalStyle() {
		display.setData("stylesheet", "");
		textEditor.setText(loadStyleFromFile());
	}

	private void createForm() {
		Composite canvas = new Composite(shell, SWT.NONE);

		String cols = "left:pref, 5px, fill:100px:grow";
		String rows = "p, 10px, fill:300px:grow, 10px, p";
		FormLayout layout = new FormLayout(cols, rows);

		DefaultFormBuilder builder = new DefaultFormBuilder(layout, canvas);
		builder.setDefaultDialogBorder();

		String[] items = new String[Style.values().length];
		for (int i = 0; i < Style.values().length; i++) {
			items[i] = Style.values()[i].name();
		}

		combo = UIControlsFactory.createCombobox(canvas, items);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reloadStyle();
			}
		});

		builder.append(UIControlsFactory.createLabel(canvas, "Style"));
		builder.append(combo);
		builder.nextLine(2);

		textEditor = createTextEditor(canvas);

		builder.append(textEditor, 3);
		builder.nextLine(2);

		builder.append(createControlComposite(canvas), 3);
	}

	private String loadStyleFromFile() {
		int sel = combo.getSelectionIndex();
		Style style = Style.values()[sel];
		String out = CssUtil.loadStyle(style);
		return out;
	}

	private Composite createControlComposite(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout("p,10px,p,10px,p,10px:grow", "40px");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout, panel);

		builder.append(createReloadButton(panel));
		builder.append(createResetButton(panel));
		builder.append(createApplyButton(panel));
		//		builder.append(createColorSelectButton(panel, "start", 0));
		//		builder.append(createColorSelectButton(panel, "stop", 1));
		//		//		FormDebugUtils.debugLayout(panel);

		return panel;
	}

	private Button createReloadButton(Composite parent) {
		Button reset = createButton(parent, "Reload");
		reset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reloadStyle();
			}
		});
		return reset;
	}

	private Button createResetButton(Composite parent) {
		Button reset = createButton(parent, "Reset");
		reset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetGlobalStyle();
			}
		});
		return reset;
	}

	private Button createApplyButton(Composite parent) {
		Button update = createButton(parent, "Apply");
		update.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateGlobalStyle();
			}
		});
		return update;
	}

	private Button createButton(Composite parent, String text) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		return button;
	}

	private Text createTextEditor(Composite parent) {
		Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		return text;
	}

	/**
	 *
	 */
	private void reloadStyle() {
		String style = loadStyleFromFile();
		textEditor.setText(style);
	}
}

/**
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextInput;

public class Heights {

	public static final int CELL_HEIGHT = 28;
	public static final int TEXTFIELD_HEIGHT = 24;
	public static final int COMBOBOX_HEIGHT = 24;
	public static final Heights INSTANCE = new Heights(0);
	private int inc;

	public Heights(int inc) {
		this.inc = inc;
	}

	public void decorate(IComboBox textField) {
		decorateHeight(textField);
		styleColor(textField);
	}

	public void styleColor(IColored label) {
		// Styles.instance().style(colored, Style.Element.INPUT,
		// Style.Element.BACKGROUND);
		label.color().rgb(249, 249, 249);
	}

	private IBorder styleInputBorder(IBordered bordered) {
		// Styles.instance().style(bordered, Style.Element.INPUT,
		// Style.Element.BORDER);
		IBorder border = bordered.border();
		border.color().rgb(211, 211, 211);
		return border;
	}

	public void decorateHeight(IComboBox textField) {
		textField.height(inc + COMBOBOX_HEIGHT);
		styleInputBorder((IBordered) textField);
	}

	public void decorate(ITextInput<?> textField) {
		decorateHeight(textField);
		styleColor(textField);
	}

	public void decorate(ITextArea textField) {
		styleColor(textField);
	}

	public void decorateHeight(ITextInput<?> textField) {
		textField.height(inc + TEXTFIELD_HEIGHT);
		styleInputBorder((IBordered) textField);
	}

	public void decorate(IPasswordField textField) {
		decorateHeight(textField);
		styleColor(textField);
	}

	public void decorateHeight(IPasswordField textField) {
		textField.height(inc + TEXTFIELD_HEIGHT);
		styleInputBorder((IBordered) textField);
	}

	public void decorate(IGridPanel.IGridCell cell) {
		cell.height(inc + CELL_HEIGHT);
	}

	public void decorate(IHorizontalPanel panel) {
		panel.height(inc + CELL_HEIGHT);
	}

	public void valuePanel(ICheckBox valuePanel) {
		// valuePanel.height(inc + TEXTFIELD_HEIGHT);
	}

	public void decorate(ILabel label) {
		label.height(inc + TEXTFIELD_HEIGHT);
	}

	public void decorate(IPanel<?> panel) {
		decorateHeight(panel);
		styleColor(panel);
	}

	public void decorateHeight(IPanel<?> panel) {
		panel.height(inc + COMBOBOX_HEIGHT);
	}

	public void decorate(ICheckBox c) {
		c.height(inc + COMBOBOX_HEIGHT);
	}

	public IBorder decorateBorder(IBordered border) {
		return styleInputBorder(border);
	}

	public void decorateHeight(IButton b) {
		b.height(inc + COMBOBOX_HEIGHT);
	}
}

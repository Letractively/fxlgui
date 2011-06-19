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
import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.style.Styles;

public class Heights {

	public static final int CELL_HEIGHT = 28;
	public static final int TEXTFIELD_HEIGHT = 24;
	public static final int COMBOBOX_HEIGHT = 24;
	private int inc;

	public Heights(int inc) {
		this.inc = inc;
	}

	public void decorate(IComboBox textField) {
		decorateHeight(textField);
		background(textField);
	}

	private void background(IColored colored) {
		Styles.instance().input().field().background(colored);
	}

	private void border(IBordered bordered) {
		Styles.instance().input().field().border(bordered);
	}

	public void decorateHeight(IComboBox textField) {
		textField.height(inc + COMBOBOX_HEIGHT);
		border(textField);
	}

	public void decorate(ITextField textField) {
		decorateHeight(textField);
		background(textField);
	}

	public void decorate(ITextArea textField) {
		background(textField);
	}

	public void decorateHeight(ITextField textField) {
		textField.height(inc + TEXTFIELD_HEIGHT);
		border(textField);
	}

	public void decorate(IPasswordField textField) {
		decorateHeight(textField);
		background(textField);
	}

	public void decorateHeight(IPasswordField textField) {
		textField.height(inc + TEXTFIELD_HEIGHT);
		border(textField);
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
		background(panel);
	}

	public void decorateHeight(IPanel<?> panel) {
		panel.height(inc + COMBOBOX_HEIGHT);
	}

	public void decorate(ICheckBox c) {
		c.height(inc + COMBOBOX_HEIGHT);
	}

	public void decorateBorder(IBordered border) {
		border(border);
	}
}

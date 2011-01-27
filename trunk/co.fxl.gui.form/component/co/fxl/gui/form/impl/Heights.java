/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.form.impl;

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextField;

public class Heights {

	private static final int CELL_HEIGHT = 36;
	private static final int TEXTFIELD_HEIGHT = 32;
	private static final int COMBOBOX_HEIGHT = 24;
	private int inc;

	public Heights(int inc) {
		this.inc = inc;
	}

	public void decorate(IComboBox textField) {
		textField.height(inc + COMBOBOX_HEIGHT);
	}

	public void decorate(ITextField textField) {
		textField.height(inc + TEXTFIELD_HEIGHT);
	}

	public void decorate(IPasswordField textField) {
		textField.height(inc + TEXTFIELD_HEIGHT);
	}

	public void decorate(IGridPanel.IGridCell cell) {
		cell.height(inc + CELL_HEIGHT);
	}

	public void decorate(IHorizontalPanel panel) {
		panel.height(inc + CELL_HEIGHT);
	}

	public void valuePanel(ICheckBox valuePanel) {
//		valuePanel.height(inc + TEXTFIELD_HEIGHT);
	}

	public void decorate(ILabel label) {
		label.height(inc + TEXTFIELD_HEIGHT);
	}
}

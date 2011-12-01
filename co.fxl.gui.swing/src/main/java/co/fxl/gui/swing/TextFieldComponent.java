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
package co.fxl.gui.swing;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JTextField;

final class TextFieldComponent extends JTextField {

	static final int MIN_HEIGHT_TEXT_COMPONENT = 20;
	static final int MIN_WIDTH_TEXT_COMPONENT = 40;
	private static final long serialVersionUID = 1L;

	@Override
	public Dimension getMinimumSize() {
		Dimension d = super.getMinimumSize();
		if (d == null)
			d = new Dimension();
		if (d.height == 0)
			d.height = MIN_HEIGHT_TEXT_COMPONENT;
		if (d.width == 0)
			d.width = MIN_WIDTH_TEXT_COMPONENT;
		return d;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		if (d == null)
			d = new Dimension();
		if (d.width == 0)
			d.width = 200;
		return d;
	}

	@Override
	public Insets getInsets() {
		return SwingContainer.INSETS;
	}
}
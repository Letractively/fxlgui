/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import javax.swing.JTextArea;

final class TextAreaComponent<T> extends JTextArea {

	private static final long serialVersionUID = 1L;
	private static final int MIN_HEIGHT_TEXTAREA_COMPONENT = 20;

	@Override
	public Dimension getMinimumSize() {
		Dimension d = super.getMinimumSize();
		if (d.height < MIN_HEIGHT_TEXTAREA_COMPONENT)
			d.height = MIN_HEIGHT_TEXTAREA_COMPONENT;
		return d;
	}

	@Override
	public Insets getInsets() {
		return SwingContainer.INSETS;
	}
}
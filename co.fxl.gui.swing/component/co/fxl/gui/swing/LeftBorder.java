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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;

class LeftBorder extends AbstractBorder implements SwingConstants {

	private static final long serialVersionUID = 3170444957341182701L;
	private SwingBorder borderStyle;
	private boolean top;

	public LeftBorder(SwingBorder borderStyle, boolean top) {
		this.borderStyle = borderStyle;
		this.top = top;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Color oldColor = g.getColor();
		g.setColor(borderStyle.borderColor);
		g.drawLine(x, y, x, y + height - 1);
		g.setColor(oldColor);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(top ? 1 : 0, 0, 0, top ? 0 : 1);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		return new Insets(top ? 1 : 0, 0, 0, top ? 0 : 1);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}
}
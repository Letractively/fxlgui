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

import javax.swing.border.Border;

class DottedBorder implements Border {

	private final Insets insets = new Insets(1, 1, 1, 1);
	private final int length = 5;
	private final int space = 3;

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		g.setColor(Color.RED);
		for (int i = 0; i < width; i += length) {
			g.drawLine(i, y, i + length, y);
			g.drawLine(i, height - 1, i + length, height - 1);
			i += space;
		}
		for (int i = 0; i < height; i += length) {
			g.drawLine(0, i, 0, i + length);
			g.drawLine(width - 1, i, width - 1, i + length);
			i += space;
		}
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return insets;
	}
}
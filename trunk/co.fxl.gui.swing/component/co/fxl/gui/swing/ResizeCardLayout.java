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

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

class ResizeCardLayout extends CardLayout {

	private static final long serialVersionUID = 1L;

	private final SwingCardPanel swingCardLayout;

	ResizeCardLayout(SwingCardPanel swingCardLayout) {
		this.swingCardLayout = swingCardLayout;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int w = 0;
			int h = 0;
			for (int i = 0; i < ncomponents; i++) {
				Component comp = parent.getComponent(i);
				if (this.swingCardLayout.chosen != null
						&& this.swingCardLayout.chosen != comp)
					continue;
				Dimension d = comp.getPreferredSize();
				if (d.width > w) {
					w = d.width;
				}
				if (d.height > h) {
					h = d.height;
				}
			}
			return new Dimension(insets.left + insets.right + w, insets.top
					+ insets.bottom + h);
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int w = 0;
			int h = 0;
			for (int i = 0; i < ncomponents; i++) {
				Component comp = parent.getComponent(i);
				if (this.swingCardLayout.chosen != null
						&& this.swingCardLayout.chosen != comp)
					continue;
				Dimension d = comp.getMinimumSize();
				if (d.width > w) {
					w = d.width;
				}
				if (d.height > h) {
					h = d.height;
				}
			}
			return new Dimension(insets.left + insets.right + w, insets.top
					+ insets.bottom + h);
		}
	}
}
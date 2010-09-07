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

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Rectangle;

final class HorizontalLayoutManager extends FlowLayout {

	private static final long serialVersionUID = 1L;
	private final SwingHorizontalPanel panel;

	HorizontalLayoutManager(SwingHorizontalPanel panel) {
		this.panel = panel;
	}

	@Override
	public void layoutContainer(Container container) {
		super.layoutContainer(container);
		if (panel.stretch != null && container.getComponentCount() > 0) {
			Component c = container
					.getComponent(container.getComponentCount() - 1);
			if (c == panel.stretch) {
				Rectangle b = c.getBounds();
				b.width = c.getParent().getWidth() - b.x;
				c.setBounds(b);
			}
		}
	}
}
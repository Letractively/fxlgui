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
package co.fxl.gui.swing;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IScrollPane;

class SwingScrollPane extends SwingElement<JScrollPane, IScrollPane> implements
		IScrollPane {

	SwingScrollPane(SwingContainer<JScrollPane> container) {
		super(container);
		container.component
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		container.component
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container.component.setOpaque(false);
		container.component.setBackground(null);
		container.component.setBorder(null);
	}

	@Override
	public IContainer viewPort() {
		return new SwingContainer<JComponent>(container.parent) {

			void setComponent(JComponent component) {
				super.component = component;
				JViewport viewport = container.component.getViewport();
				viewport.setOpaque(false);
				viewport.setBackground(null);
				viewport.add(component);
			}
		};
	}
}

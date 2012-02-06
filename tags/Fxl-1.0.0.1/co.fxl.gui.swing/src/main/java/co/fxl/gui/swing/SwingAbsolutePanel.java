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

import java.awt.Point;

import javax.swing.JComponent;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.IElement;

class SwingAbsolutePanel extends SwingPanel<IAbsolutePanel> implements
		IAbsolutePanel {

	private int x;
	private int y;

	SwingAbsolutePanel(SwingContainer<PanelComponent> container) {
		super(container);
		setLayout(null);
	}

	@Override
	public IAbsolutePanel addResizeListener(IResizeListener listener) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IAbsolutePanel offset(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public IAbsolutePanel offset(IElement<?> element, int x, int y) {
		JComponent component = (JComponent) element.nativeElement();
		super.add(component);
		component.setBounds(x, y, component.getPreferredSize().width,
				component.getPreferredSize().height);
		return this;
	}

	@Override
	public void add(JComponent component) {
		super.add(component);
		component.setLocation(new Point(x, y));
	}
}

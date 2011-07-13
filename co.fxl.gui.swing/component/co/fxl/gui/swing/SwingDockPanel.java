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

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;

class SwingDockPanel extends SwingPanel<IDockPanel> implements IDockPanel {

	private List<String> positions = new LinkedList<String>(
			Arrays.asList(new String[] { BorderLayout.CENTER,
					BorderLayout.NORTH, BorderLayout.WEST, BorderLayout.EAST,
					BorderLayout.SOUTH }));
	private BorderLayout layoutManager;

	SwingDockPanel(SwingContainer<PanelComponent> container) {
		super(container);
		layoutManager = new BorderLayout();
		setLayout(layoutManager);
	}

	private void setPosition(String position) {
		// boolean removed =
		positions.remove(position);
		// if (!removed)
		// throw new MethodNotImplementedException(position);
		positions.add(0, position);
	}

	@Override
	public IContainer center() {
		setPosition(BorderLayout.CENTER);
		return add();
	}

	@Override
	public IContainer left() {
		setPosition(BorderLayout.WEST);
		return add();
	}

	@Override
	public IContainer top() {
		setPosition(BorderLayout.NORTH);
		return add();
	}

	@Override
	public IContainer bottom() {
		setPosition(BorderLayout.SOUTH);
		return add();
	}

	@Override
	public IContainer right() {
		setPosition(BorderLayout.EAST);
		return add();
	}

	@Override
	public void add(JComponent component) {
		String position = positions.remove(0);
		container.component.add(component, position);
	}

	@Override
	public IDockPanel spacing(int pixel) {
		container.component.getInsets().set(pixel, pixel, pixel, pixel);
		layoutManager.setHgap(pixel);
		layoutManager.setVgap(pixel);
		return this;
	}
}

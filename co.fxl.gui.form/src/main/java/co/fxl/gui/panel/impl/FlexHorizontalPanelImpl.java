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
package co.fxl.gui.panel.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.panel.api.IFlexHorizontalPanel;

class FlexHorizontalPanelImpl implements IFlexHorizontalPanel<Object> {

	private IHorizontalPanel panel;
	private List<Object> iDs = new LinkedList<Object>();

	FlexHorizontalPanelImpl(IContainer container) {
		panel = container.panel().horizontal();
	}

	@Override
	public IFlexHorizontalPanel<Object> addSpace(int space) {
		panel.addSpace(space);
		return this;
	}

	@Override
	public IContainer add() {
		return panel.add();
	}

	@Override
	public IContainer add(Object iD) {
		iDs.add(iD);
		throw new UnsupportedOperationException();
	}

	@Override
	public IFlexHorizontalPanel<Object> addPanel(Object iD) {
		return new FlexHorizontalPanelImpl(add(iD));
	}

	@Override
	public List<Object> order() {
		return iDs;
	}
}

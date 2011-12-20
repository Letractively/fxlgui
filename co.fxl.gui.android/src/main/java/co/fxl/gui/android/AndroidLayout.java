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
package co.fxl.gui.android;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IFlowPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;

public class AndroidLayout implements ILayout {

	public AndroidContainer container;

	public AndroidLayout(AndroidContainer container) {
		this.container = container;
	}

	@Override
	public IAbsolutePanel absolute() {
		throw new MethodNotImplementedException();
	}

	@Override
	public ICardPanel card() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDockPanel dock() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IGridPanel grid() {
		return (IGridPanel) (container.element = new AndroidGridPanel(container));
	}

	@Override
	public IHorizontalPanel horizontal() {
		return (IHorizontalPanel) (container.element = new AndroidHorizontalPanel(
				container));
	}

	@Override
	public IPanel<?> plugIn(Class<?> layoutType) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IVerticalPanel vertical() {
		return (IVerticalPanel) (container.element = new AndroidVerticalPanel(
				container));
	}

	@Override
	public IFlowPanel flow() {
		throw new MethodNotImplementedException();
	}
}

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
package co.fxl.gui.table.flex.swing;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.swing.PanelComponent;
import co.fxl.gui.swing.SwingContainer;
import co.fxl.gui.swing.SwingPanel;
import co.fxl.gui.table.flex.IFlexGridPanel;

public class SwingFlexGridPanel extends SwingPanel<IFlexGridPanel> implements
		IFlexGridPanel {

	private class FlexCell implements IFlexCell {

		private int x;
		private int y;

		FlexCell(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public IFlexCell width(int columns) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IFlexCell height(int rows) {
			throw new MethodNotImplementedException();
		}

		@Override
		public IContainer container() {
			throw new MethodNotImplementedException();
		}

	}

	@SuppressWarnings("unchecked")
	SwingFlexGridPanel(IContainer container) {
		super((SwingContainer<PanelComponent>) container);
	}

	@Override
	public IFlexCell cell(int x, int y) {
		return new FlexCell(x, y);
	}

}

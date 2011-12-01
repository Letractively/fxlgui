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
package co.fxl.gui.style.gplus;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.api.IStyle.ITable;

public class GPlusTable implements ITable {

	@Override
	public IColumnSelection columnSelection() {
		return new IColumnSelection() {

			@Override
			public IColumnSelection panel(IPanel<?> b, boolean visible) {
				if (visible)
					b.color().lightgray();
				return this;
			}

			@Override
			public IColumnSelection label(ILabel b, boolean visible) {
				if (visible)
					b.font().color().mix().gray().black();
				return this;
			}

		};
	}

	@Override
	public ITable statusPanel(IPanel<?> statusPanel) {
		statusPanel.color().remove();
		statusPanel.border().remove();
		return this;
	}

}
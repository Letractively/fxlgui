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
package co.fxl.gui.impl;

import co.fxl.gui.api.IContainer;

public class ColoredComboBox extends ComboBoxAdp {

	public interface IColorAdapter {

		String color(String value);
	}

	private static final boolean ACTIVE = false;

	// private IGridPanel grid;
	// private ILabel label;
	// private IColorAdapter colorAdapter;
	// private IImage button;

	public ColoredComboBox(IContainer container) {
		super(container.comboBox());
		// container.element().remove();
		// grid = container.panel().grid();
		// grid.column(0).expand();
		// label = grid.cell(0, 0).label();
		// button = grid.cell(1, 0).image().resource("more_small_black.png");
	}

	public ColoredComboBox colorAdapter(final IColorAdapter colorAdapter) {
		if (ACTIVE)
			addUpdateListener(new IUpdateListener<String>() {
				@Override
				public void onUpdate(String value) {
					String color = colorAdapter.color(value);
					if (color != null) {
						color().rgb(ColorTemplate.parse(color));
						font().color().white();
					} else {
						color().white();
						font().color().black();
					}
				}
			});
		// this.colorAdapter = colorAdapter;
		return this;
	}
}

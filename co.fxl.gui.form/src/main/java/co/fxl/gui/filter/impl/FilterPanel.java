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
package co.fxl.gui.filter.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.WidgetTitle;

interface FilterPanel {

	public interface ICell {

		IComboBox comboBox();

		RangeField horizontal();

		ITextField textField();

		IDockPanel dock();
	}

	interface FilterGrid {

		FilterGrid rowInc(int rowInc);

		Heights heights();

		ICell cell(int row);

		void title(int filterIndex, String name);

		void register(ITextField tf);

		void resize(int size);

		void show(FilterPart<?> firstConstraint);

		boolean addTooltips();

		void notifyComboBoxChange(boolean clickableClear);

		void clearRowIterator();

		void updateFilters();
	}

	ILabel addTitle(String string);

	IClickable<?> addHyperlink(String imageResource, String string);

	ViewComboBox viewComboBox();

	IContainer top();

	FilterGrid filterGrid();

	void visible();

	WidgetTitle widgetTitle();

	void clearRowIterator();

	void linksVisible(boolean b);
}

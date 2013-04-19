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

import java.util.List;

import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;

class ComboBoxIntegerFilter extends ComboBoxFilterTemplate<Integer> {

	Integer text;
	private Integer defaultValue;

	ComboBoxIntegerFilter(FilterGrid panel, String name, List<Object> values,
			int filterIndex) {
		super(panel, name, values, filterIndex);
		this.defaultValue = (Integer) values.get(0);
	}

	@Override
	public boolean update() {
		String trim = input.text().trim();
		if (trim.equals(""))
			text = defaultValue;
		else
			text = Integer.valueOf(trim);
		return text != null;
	}

	@Override
	public void clear() {
		text(String.valueOf(defaultValue));
		text = defaultValue;
	}

	@Override
	public boolean applies(Integer value) {
		return true;
	}

	@Override
	public IFilterConstraint asConstraint() {
		int before = text;
		update();
		if (text != before)
			panel.clearRowIterator();
		return new SizeConstraint(text);
	}

	void set(int size) {
		text(String.valueOf(size));
	}
}

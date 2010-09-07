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
package co.fxl.gui.table.filter.impl;

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

class StringFilter extends FilterTemplate<String> {

	private ITextField textField;
	private String text;

	StringFilter(int columnIndex, IGridPanel panel, String name, int filterIndex) {
		super(columnIndex, panel, name, filterIndex);
		this.textField = panel.add().textField().height(HEIGHT);
		textField.width(162);
	}

	@Override
	public boolean update() {
		text = textField.text().trim();
		if (text.equals(""))
			text = null;
		return text != null;
	}

	@Override
	public void clear() {
		textField.text("");
		text = null;
	}

	@Override
	public boolean applies(String value) {
		return value.startsWith(text);
	}

	@Override
	public void addUpdateListener(final FilterListener l) {
		textField.addUpdateListener(new IUpdateListener<String>() {

			@Override
			public void onUpdate(String value) {
				l.onActive(!textField.text().trim().equals(""));
			}
		});
	}

	@Override
	public Object[] values() {
		update();
		return new Object[] { text };
	}
}

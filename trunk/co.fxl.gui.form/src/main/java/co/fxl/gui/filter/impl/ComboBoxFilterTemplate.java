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

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.filter.api.IFilterWidget.IFilter.IGlobalValue;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.form.impl.Validation;

abstract class ComboBoxFilterTemplate<T> extends FilterTemplate<IComboBox, T> {

	FilterGrid panel;
	boolean updateListeningActive = true;
	boolean directApply = true;
	private IGlobalValue v;

	ComboBoxFilterTemplate(FilterGrid panel, String name, List<Object> values,
			int filterIndex, IGlobalValue v) {
		super(panel, name, filterIndex);
		this.v = v;
		this.panel = panel;
		input = new FilterComboBoxAdp(panel.cell(filterIndex).comboBox()
				.width(WIDTH_COMBOBOX_CELL));
		panel.heights().decorate(input);
		for (Object object : values) {
			input.addText(string(object));
		}
	}

	private String string(Object object) {
		if (object == null)
			return "";
		return String.valueOf(object);
	}

	void text(String valueOf) {
		updateListeningActive = false;
		input.text(String.valueOf(valueOf));
		updateListeningActive = true;
	}

	@Override
	public final void validate(Validation validation) {
		if (v != null)
			return;
		validation.linkInput(input);
		input.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				if (updateListeningActive && directApply)
					panel.notifyComboBoxChange(clearClickable());
			}
		});
	}

	@Override
	public final void addUpdateListener(final FilterListener l) {
		input.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				if (updateListeningActive)
					l.onActive(!input.text().trim().equals(""));
			}
		});
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		throw new UnsupportedOperationException();
	}

	boolean clearClickable() {
		return true;
	}

}

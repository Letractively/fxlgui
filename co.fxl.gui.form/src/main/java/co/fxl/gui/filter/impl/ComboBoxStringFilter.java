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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IFilter.IGlobalValue;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.FieldTypeImpl;

class ComboBoxStringFilter extends ComboBoxFilterTemplate<String> {

	private String text;
	private List<IUpdateListener<String>> updateListeners = new LinkedList<IUpdateListener<String>>();
	private IGlobalValue v;
	private FieldTypeImpl type;
	private boolean ignore;

	ComboBoxStringFilter(final FilterGrid panel, String name,
			FieldTypeImpl type, List<Object> values, int filterIndex,
			final IGlobalValue v) {
		super(panel, name, values, filterIndex, v);
		this.type = type;
		this.v = v;
		if (v != null)
			input.text(v.value() != null ? v.value() : "");
		input.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(final String value) {
				if (v != null) {
					if (ignore)
						return;
					if (!equals(value, v.value()))
						v.value(value, new CallbackTemplate<Void>() {
							@Override
							public void onSuccess(Void result) {
								panel.updateFilters();
								panel.refresh();
							}
						});
				} else
					notifyListeners(value);
			}

			private boolean equals(String value, String value2) {
				if (value == null)
					return value2 == null;
				return value.equals(value2);
			}

			private void notifyListeners(String value) {
				if (updateListeningActive)
					for (IUpdateListener<String> l : updateListeners)
						l.onUpdate(value);
			}
		});
	}

	@Override
	public void updateFilter() {
		if (v != null) {
			ignore = true;
			input.clear();
			input.addText((String) null);
			for (Object t : type.getConstraints()) {
				input.addText((String) t);
			}
			String value = v.value();
			input.text(value);
			ignore = false;
		}
	}

	@Override
	public boolean update() {
		String t = input.text();
		text = t != null ? t.trim() : null;
		if (text != null && text.equals(""))
			text = null;
		return v == null && text != null;
	}

	@Override
	boolean clearClickable() {
		return v == null;
	}

	@Override
	public void clear() {
		if (v != null)
			return;
		text("");
		text = null;
	}

	@Override
	public boolean applies(String value) {
		return value.startsWith(text);
	}

	@Override
	public IFilterConstraint asConstraint() {
		update();
		return new StringPrefixConstraint(name, text);
	}

	@Override
	boolean fromConstraint(IFilterConstraints constraints) {
		if (v != null) {
			return false;
		}
		if (constraints.isAttributeConstrained(name)) {
			String prefix = constraints.stringValue(name);
			text(prefix);
			return true;
		} else
			return false;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		updateListeners.add(listener);
		return this;
	}
}

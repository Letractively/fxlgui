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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.filter.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter.IAdapter;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.filter.impl.FilterPanel.ICell;
import co.fxl.gui.form.impl.Validation;
import co.fxl.gui.impl.DummyCallback;

class RelationFilter extends StringFilter {

	List<Object> values = new LinkedList<Object>();
	private IClickListener clear = new IClickListener() {
		@Override
		public void onClick() {
			clear(true);
		}
	};
	private IAdapter<Object, Object> adapter;
	private IImage remove;
	private FilterWidgetImpl widget;
	private IClickListener cl;
	List<Object> preset;

	RelationFilter(FilterWidgetImpl widget, FilterGrid grid, String name,
			int filterIndex, List<Object> preset,
			IAdapter<Object, Object> adapter, IClickListener cl) {
		super(grid, name, filterIndex);
		this.widget = widget;
		this.adapter = adapter;
		input.text(toString(preset)).editable(false);
		input.font().weight().bold().color().gray();
		this.cl = cl;
		remove.addClickListener(clear);
		this.preset = preset;
	}

	private void clear(boolean notifyListeners) {
		values = null;
		RelationFilter.super.clear();
		input.editable(true).width(RelationFilter.super.width()).font()
				.weight().plain().color().black();
		if (!notifyListeners)
			return;
		widget.apply(DummyCallback.voidInstance());
		if (cl != null)
			cl.onClick();
	}

	@Override
	int width() {
		return WIDTH_SINGLE_CELL - 20;
	}

	@Override
	ITextField textField(ICell c, int filterIndex) {
		IDockPanel dock = c.dock();
		remove = dock.right().panel().vertical().addSpace(4).add().image()
				.resource(co.fxl.gui.impl.Icons.CANCEL).size(16, 16);
		return dock.center().textField();
	}

	@Override
	public String toString() {
		return toString(preset);
	}

	String toString(List<Object> preset) {
		return toString(adapter, preset, values);
	}

	static String toString(IAdapter<Object, Object> adapter,
			List<Object> preset, List<Object> values) {
		StringBuilder b = new StringBuilder();
		boolean hasMore = false;
		for (Object e : preset) {
			if (b.length() != 0)
				b.append(", ");
			// if (b.length() < 20) {
			b.append(adapter.name(e));
			// } else
			// hasMore = true;
			values.add(adapter.id(e));
		}
		// if (b.length() >= 20) {
		// b.replace(20, b.length(), "");
		// hasMore = true;
		// }
		return b.toString() + (hasMore ? ", ..." : "");
	}

	@Override
	public IFilterConstraint asConstraint() {
		if (values != null)
			return new RelationConstraint(name, values, toString());
		else
			return super.asConstraint();
	}

	@Override
	boolean fromConstraint(IFilterConstraints constraints) {
		if (values != null) {
			if (!constraints.isRelationConstrained(name)) {
				// TODO ... clear(false);
			}
			return false;
		} else if (constraints.isAttributeConstrained(name)) {
			return super.fromConstraint(constraints);
		} else if (constraints.isRelationConstrained(name)) {
			return true;
		} else
			throw new UnsupportedOperationException();
	}

	@Override
	public boolean applies(String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		clear(false);
	}

	@Override
	public void validate(Validation validation) {
		validation.linkInput(input);
	}

}

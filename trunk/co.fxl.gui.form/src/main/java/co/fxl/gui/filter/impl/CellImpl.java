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
package co.fxl.gui.filter.impl;

import java.util.Date;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.filter.impl.FilterPanel.ICell;
import co.fxl.gui.form.impl.DateField;
import co.fxl.gui.form.impl.Validation;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.Heights;

class CellImpl implements ICell {

	static final class ExpliciteRangeField implements RangeField {

		private final ITextField tf1;
		private final ITextField tf2;
		private FilterWidgetImpl widget;

		ExpliciteRangeField(FilterWidgetImpl widget, IContainer cell,
				boolean isDateField) {
			this.widget = widget;
			IHorizontalPanel p = cell.panel().horizontal();
			tf1 = addTextField(p, isDateField);
			p.add().label().text("-").margin().left(4).right(4);
			tf2 = addTextField(p, isDateField);
		}

		ITextField addTextField(IHorizontalPanel p, boolean isDateField) {
			boolean useDateField = isDateField && !Env.is(Env.SWING);
			ITextField textField = useDateField ? new DateField(p.add()) : p
					.add().textField();
			ITextField textField1 = textField
					.width(FilterTemplate.WIDTH_RANGE_CELL
							- (useDateField ? 24 : 0));
			Heights.INSTANCE.decorate(textField1);
			if (useDateField)
				textField.font().pixel(9);
			widget.register(textField1);
			return textField1;
		}

		@Override
		public String text() {
			return tf1.text();
		}

		@Override
		public String upperBoundText() {
			return tf2.text();
		}

		@Override
		public void upperBoundText(String text) {
			tf2.text(text);
		}

		@Override
		public void addUpdateListener(IUpdateListener<String> listener) {
			tf1.addUpdateListener(listener);
		}

		@Override
		public void upperBoundAddUpdateListener(IUpdateListener<String> listener) {
			tf2.addUpdateListener(listener);
		}

		@Override
		public void validation(Validation validation, Class<?> type) {
			if (type.equals(Date.class)) {
				validation.validateDate(tf1);
				validation.validateDate(tf2);
			} else if (type.equals(Long.class)) {
				validation.linkInput(tf1);
				validation.linkInput(tf2);
			}
		}

		@Override
		public void text(String text) {
			tf1.text(text);
		}
	}

	private final FilterPanelImpl filterPanelImpl;
	private IGridCell cell;

	CellImpl(FilterPanelImpl filterPanelImpl, IGridCell cell) {
		this.filterPanelImpl = filterPanelImpl;
		this.filterPanelImpl.widget.heights.decorate(cell);
		this.cell = cell;
	}

	@Override
	public IComboBox comboBox() {
		return cell.comboBox();
	}

	@Override
	public RangeField horizontal(boolean isDateField) {
		return new ExpliciteRangeField(filterPanelImpl.widget, cell,
				isDateField);
	}

	@Override
	public ITextField textField() {
		return cell.textField();
	}

	@Override
	public IDockPanel dock() {
		return cell.panel().dock();
	}
}
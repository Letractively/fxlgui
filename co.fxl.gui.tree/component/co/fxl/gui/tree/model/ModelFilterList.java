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
package co.fxl.gui.tree.model;

import co.fxl.gui.api.ILayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.tree.api.IFilterList;
import co.fxl.gui.tree.api.IProperty;

class ModelFilterList<T> implements IFilterList<T> {

	private IFilterWidget filterWidget;

	ModelFilterList(ModelFilterTreeWidget<T> widget, ILayout layout) {
		filterWidget = (IFilterWidget) layout.vertical().add()
				.widget(IFilterWidget.class);
		filterWidget.addSizeFilter();
		filterWidget.addFilterListener(widget);
	}

	@Override
	public IFilter addFilter() {
		return filterWidget.addFilter();
	}

	@Override
	public IFilterList<T> addPropertyFilter(IProperty<T, ?> property) {
		throw new MethodNotImplementedException();
	}

	void apply() {
		filterWidget.visible(true);
		filterWidget.apply();
	}

	@Override
	public IFilterList<T> constraints(IFilterConstraints constraints) {
		filterWidget.constraints(constraints);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IRelationFilter<T, ?> addRelationFilter() {
		return (IRelationFilter<T, ?>) filterWidget.addRelationFilter();
	}

	@Override
	public IFilterList<T> addConfiguration(String configuration) {
		filterWidget.addConfiguration(configuration);
		return this;
	}
}

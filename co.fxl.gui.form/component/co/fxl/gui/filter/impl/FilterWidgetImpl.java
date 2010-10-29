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

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.Validation;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter.IAdapter;

class FilterWidgetImpl implements IFilterWidget {

	private static final List<Object> DEFAULT_SIZES = Arrays
			.asList(new Object[] { new Integer(20), 50, 100, 500, 1000 });

	private class ClearClickListener implements IClickListener {

		@Override
		public void onClick() {
			if (holdFilterClicks)
				return;
			for (FilterPart<?> filter : filters) {
				filter.clear();
			}
			clear.clickable(false);
			apply.clickable(false);
			notifyListeners();
		}
	}

	private class ApplyClickListener implements IClickListener {

		@Override
		public void onClick() {
			if (holdFilterClicks)
				return;
			validation.update();
			clear.clickable(true);
			notifyListeners();
		}
	}

	private List<FilterPart<?>> filters = new LinkedList<FilterPart<?>>();
	IClickable<?> apply;
	IClickable<?> clear;
	private List<Boolean> activeFlags = new LinkedList<Boolean>();
	private IGridPanel grid;
	boolean holdFilterClicks = false;
	Validation validation = new Validation();
	private List<IFilterListener> listeners = new LinkedList<IFilterListener>();
	private List<FilterImpl> filterList = new LinkedList<FilterImpl>();
	private ComboBoxIntegerFilter sizeFilter;
	private boolean addSizeFilter = false;
	private IFilterConstraints constraints;

	FilterWidgetImpl(ILayout panel) {
		WidgetTitle title = new WidgetTitle(panel);
		title.addTitle("Filter");
		apply = title.addHyperlink("Apply");
		validation.linkClickable(apply);
		clear = title.addHyperlink("Clear");
		apply.addClickListener(new ApplyClickListener());
		apply.clickable(false);
		clear.addClickListener(new ClearClickListener());
		clear.clickable(false);
		this.grid = title.content().panel().grid().indent(3);
	}

	@SuppressWarnings("unchecked")
	void notifyListeners() {
		List<FilterTemplate<Object>> activeFilters = new LinkedList<FilterTemplate<Object>>();
		for (FilterPart<?> filter : filters) {
			boolean active = filter.update();
			if (active)
				activeFilters.add((FilterTemplate<Object>) filter);
		}
		FilterConstraintsImpl constraints = new FilterConstraintsImpl();
		for (FilterTemplate<Object> filter : activeFilters) {
			constraints.add(filter.asConstraint());
		}
		if (sizeFilter != null)
			constraints.add(sizeFilter.asConstraint());
		for (IFilterListener listener : listeners) {
			listener.onApply(constraints);
		}
	}

	private FilterPart<?> addFilter(Class<?> contentType, String name,
			List<Object> values, List<Object> preset,
			IAdapter<Object, Object> adapter) {
		FilterPart<?> filter;
		if (preset != null) {
			filter = new RelationFilter(grid, name, filters.size(), preset,
					adapter);
		} else if (!values.isEmpty()) {
			if (contentType.equals(String.class)) {
				filter = new ComboBoxStringFilter(grid, name, values, filters
						.size());
			} else if (contentType.equals(Integer.class)) {
				filter = new ComboBoxIntegerFilter(grid, name, values, filters
						.size());
			} else if (contentType.equals(IImage.class)) {
				filter = new ComboBoxStringFilter(grid, name, values, filters
						.size());
			} else
				throw new MethodNotImplementedException();
		} else if (contentType.equals(String.class))
			filter = new StringFilter(grid, name, filters.size());
		else if (contentType.equals(Date.class))
			filter = new DateFilter(grid, name, filters.size());
		else if (contentType.equals(Integer.class))
			filter = new NumberFilter(grid, name, filters.size());
		else
			throw new MethodNotImplementedException(contentType.getName());
		activeFlags.add(false);
		filter.validate(validation);
		filters.add(filter);
		return filter;
	}

	@Override
	public IFilter addFilter() {
		FilterImpl filter = new FilterImpl();
		filterList.add(filter);
		return filter;
	}

	@Override
	public IRelationFilter<Object, Object> addRelationFilter() {
		RelationFilterImpl filter = new RelationFilterImpl();
		filterList.add(filter);
		return filter;
	}

	@Override
	public IFilterWidget addFilterListener(IFilterListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IFilterWidget constraints(IFilterConstraints constraints) {
		this.constraints = constraints;
		return this;
	}

	@Override
	public IFilterWidget visible(boolean visible) {
		for (FilterImpl filter : filterList) {
			List<Object> list = new LinkedList<Object>(Arrays
					.asList(filter.type.values));
			if (!list.isEmpty())
				list.add(0, "");
			List<Object> preset = null;
			IAdapter<Object, Object> adapter = null;
			if (filter instanceof RelationFilterImpl) {
				RelationFilterImpl rf = (RelationFilterImpl) filter;
				preset = rf.preset;
				adapter = rf.adapter;
			}
			addFilter(filter.type.type, filter.name, list, preset, adapter);
		}
		if (addSizeFilter) {
			sizeFilter = (ComboBoxIntegerFilter) addFilter(Integer.class,
					"Size", DEFAULT_SIZES, null, null);
			sizeFilter.validate(validation);
		}
		boolean constrained = false;
		if (constraints != null) {
			for (FilterPart<?> f : filters) {
				FilterTemplate<?> ft = (FilterTemplate<?>) f;
				boolean c = ft.fromConstraint((IFilterConstraints) constraints);
				constrained = constrained | c;
				if (f instanceof RelationFilter)
					constrained = true;
			}
			if (constraints.size() != (Integer) DEFAULT_SIZES.get(0)) {
				sizeFilter.set(constraints.size());
				constrained = true;
			}
		}
		for (FilterPart<?> f : filters) {
			if (f instanceof RelationFilter)
				constrained = true;
		}
		if (constrained) {
			apply.clickable(true);
			clear.clickable(true);
		}
		return this;
	}

	@Override
	public IFilterWidget addSizeFilter() {
		addSizeFilter = true;
		return this;
	}

	@Override
	public IFilterWidget holdFilterClicks(boolean hold) {
		holdFilterClicks = hold;
		return this;
	}

	@Override
	public IFilterWidget apply() {
		notifyListeners();
		return this;
	}
}

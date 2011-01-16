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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.Validation;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter.IAdapter;

class FilterWidgetImpl implements IFilterWidget, IUpdateListener<String> {
	
	// TODO Swing: cells are too small

	private static final List<Object> DEFAULT_SIZES = Arrays
			.asList(new Object[] { 50, 100, 500, 1000, 5000 });

	private class ClearClickListener implements IClickListener {

		@Override
		public void onClick() {
			if (holdFilterClicks)
				return;
			for (List<FilterImpl> l : filterList.values()) {
				Iterator<FilterImpl> it = l.iterator();
				while (it.hasNext()) {
					if (it.next() instanceof RelationFilterImpl) {
						it.remove();
					}
				}
			}
			for (FilterPart<?> filter : guiFilterElements) {
				filter.clear();
			}
			clear.clickable(false);
			apply.clickable(false);
			if (!configuration.equals(firstConfiguration)
					&& configurationComboBox != null)
				configurationComboBox.text(firstConfiguration);
			else
				update();
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

	private List<FilterPart<?>> guiFilterElements = new LinkedList<FilterPart<?>>();
	IClickable<?> apply;
	IClickable<?> clear;
	private List<Boolean> activeFlags = new LinkedList<Boolean>();
	private IGridPanel grid;
	boolean holdFilterClicks = false;
	Validation validation = new Validation();
	private List<IFilterListener> listeners = new LinkedList<IFilterListener>();
	private Map<String, List<FilterImpl>> filterList = new HashMap<String, List<FilterImpl>>();
	private ComboBoxIntegerFilter sizeFilter;
	private boolean addSizeFilter = false;
	private IFilterConstraints constraints;
	private IVerticalPanel mainPanel;
	private IContainer gridContainer;
	private IComboBox configurationComboBox;
	private String firstConfiguration = IFilterConstraints.COMMON;
	private String configuration = IFilterConstraints.COMMON;
	private boolean showConfiguration = true;

	FilterWidgetImpl(IContainer panel) {
		WidgetTitle title = new WidgetTitle(panel.panel());
		title.addTitle("Filter");
		apply = title.addHyperlink("Update");
		validation.linkClickable(apply);
		clear = title.addHyperlink("Clear");
		apply.addClickListener(new ApplyClickListener());
		// apply.clickable(false);
		clear.addClickListener(new ClearClickListener());
		clear.clickable(false);
		mainPanel = title.content().panel().vertical();
	}

	@Override
	public void onUpdate(String value) {
		configuration = value;
		if (filterList.get(configuration) != null)
			update();
	}

	@Override
	public IFilterWidget addConfiguration(String config) {
		if (configurationComboBox == null && showConfiguration) {
			configurationComboBox = mainPanel.add().comboBox();
			configurationComboBox.addUpdateListener(this);
//			configurationComboBox.height(FilterTemplate.HEIGHT);
			mainPanel.addSpace(4);
		}
		if (configurationComboBox != null)
			configurationComboBox.addText(config);
		if (firstConfiguration.equals(IFilterConstraints.COMMON))
			firstConfiguration = config;
		configuration = config;
		return this;
	}

	@SuppressWarnings("unchecked")
	void notifyListeners() {
		List<FilterTemplate<Object>> activeFilters = new LinkedList<FilterTemplate<Object>>();
		for (FilterPart<?> filter : guiFilterElements) {
			boolean active = filter.update();
			if (active)
				activeFilters.add((FilterTemplate<Object>) filter);
		}
		FilterConstraintsImpl constraints = new FilterConstraintsImpl(
				configuration);
		for (FilterTemplate<Object> filter : activeFilters) {
			constraints.add(filter.asConstraint());
		}
		if (sizeFilter != null)
			constraints.add(sizeFilter.asConstraint());
		for (IFilterListener listener : listeners) {
			listener.onApply(constraints);
		}
	}

	private void remove(String name) {
		for (String cfg : filterList.keySet()) {
			List<FilterImpl> filters = filterList.get(cfg);
			Iterator<FilterImpl> it = filters.iterator();
			while (it.hasNext()) {
				FilterImpl n = it.next();
				if (n.name.equals(name))
					it.remove();
			}
		}
		update();
	}

	private FilterPart<?> addFilter(Class<?> contentType, final String name,
			List<Object> values, List<Object> preset,
			IAdapter<Object, Object> adapter) {
		assert name != null : contentType.getName();
		FilterPart<?> filter;
		if (preset != null) {
			filter = new RelationFilter(this, grid, name,
					guiFilterElements.size(), preset, adapter,
					new IClickListener() {
						@Override
						public void onClick() {
							remove(name);
						}
					});
		} else if (!values.isEmpty()) {
			if (contentType.equals(String.class)) {
				filter = new ComboBoxStringFilter(grid, name, values,
						guiFilterElements.size());
			} else if (contentType.equals(Integer.class)) {
				filter = new ComboBoxIntegerFilter(grid, name, values,
						guiFilterElements.size());
			} else if (contentType.equals(IImage.class)) {
				filter = new ComboBoxStringFilter(grid, name, values,
						guiFilterElements.size());
			} else if (contentType.equals(Date.class)) {
				throw new MethodNotImplementedException(contentType.getName());
			} else
				throw new MethodNotImplementedException(contentType.getName());
		} else if (contentType.equals(String.class))
			filter = new StringFilter(grid, name, guiFilterElements.size());
		else if (contentType.equals(Date.class))
			filter = new DateFilter(grid, name, guiFilterElements.size());
		else if (contentType.equals(Integer.class)
				|| contentType.equals(Long.class))
			filter = new NumberFilter(grid, name, guiFilterElements.size());
		else if (contentType.equals(Boolean.class))
			filter = new BooleanFilter(grid, name, guiFilterElements.size());
		else
			throw new MethodNotImplementedException(contentType.getName());
		activeFlags.add(false);
		filter.validate(validation);
		guiFilterElements.add(filter);
		return filter;
	}

	@Override
	public IFilter addFilter() {
		FilterImpl filter = new FilterImpl();
		addFilterImpl(filter);
		return filter;
	}

	private void addFilterImpl(FilterImpl filter) {
		List<FilterImpl> l = filterList.get(configuration);
		if (l == null) {
			l = new LinkedList<FilterImpl>();
			filterList.put(configuration, l);
		}
		l.add(filter);
	}

	@Override
	public IRelationFilter<Object, Object> addRelationFilter() {
		RelationFilterImpl filter = new RelationFilterImpl();
		addFilterImpl(filter);
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
	public IFilterWidget addLiveFilterListener(IFilterListener listener) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFilterWidget visible(boolean visible) {
		if (configurationComboBox != null)
			validation.linkInput(configurationComboBox);
		configuration = firstConfiguration;
		return update();
	}

	private IFilterWidget update() {
		if (gridContainer == null) {
			gridContainer = mainPanel.add();
		} else
			gridContainer.clear();
		grid = gridContainer.panel().vertical().spacing(3).add().panel().grid()
				.spacing(3);
		guiFilterElements.clear();
		addFilters4Configuration(IFilterConstraints.COMMON);
		if (!configuration.equals(IFilterConstraints.COMMON))
			addFilters4Configuration(configuration);
		if (addSizeFilter) {
			sizeFilter = (ComboBoxIntegerFilter) addFilter(Integer.class,
					"Size", DEFAULT_SIZES, null, null);
			sizeFilter.validate(validation);
		}
		boolean constrained = false;
		if (constraints != null) {
			for (FilterPart<?> f : guiFilterElements) {
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
		for (FilterPart<?> f : guiFilterElements) {
			if (f instanceof RelationFilter)
				constrained = true;
		}
		// constrained |= !firstConfiguration.equals(configuration);
		apply.clickable(constrained);
		clear.clickable(constrained);
		return this;
	}

	private void addFilters4Configuration(String cfg) {
		List<FilterImpl> l = filterList.get(cfg);
		if (l != null)
			for (FilterImpl filter : l) {
				List<Object> list = new LinkedList<Object>(filter.type.values);
				if (!list.isEmpty())
					list.add(0, "");
				List<Object> preset = null;
				IAdapter<Object, Object> adapter = null;
				if (filter instanceof RelationFilterImpl) {
					RelationFilterImpl rf = (RelationFilterImpl) filter;
					preset = rf.preset;
					adapter = rf.adapter;
				}
				addFilter(filter.type.clazz, filter.name, list, preset, adapter);
			}
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

	@Override
	public IFilterWidget showConfiguration(boolean show) {
		showConfiguration = show;
		return this;
	}

	@Override
	public IFilterWidget setConfiguration(String config) {
		onUpdate(config);
		return this;
	}
}

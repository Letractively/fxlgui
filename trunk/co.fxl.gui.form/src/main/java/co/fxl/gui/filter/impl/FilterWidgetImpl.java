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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.api.ISuggestField.ISource.ISuggestion;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter.IAdapter;
import co.fxl.gui.filter.api.ISuggestionAdp;
import co.fxl.gui.filter.impl.FilterPanel.FilterGrid;
import co.fxl.gui.form.impl.Validation;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.FieldTypeImpl;
import co.fxl.gui.impl.Heights;
import co.fxl.gui.impl.Icons;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.input.impl.TooltipTextInput;
import co.fxl.gui.style.api.IStyle.IFilterPanel;
import co.fxl.gui.style.impl.Style;

public class FilterWidgetImpl implements IFilterWidget, IUpdateListener<String> {

	// TODO SWING-FXL: Look: cells are too small

	private static final boolean DIRECT_COMBOBO_CHANGE = true;
	static String MAX_ROWS = "Page size";

	private class ClearClickListener extends LazyClickListener {

		private ClearClickListener() {
			super(noDiscardChangesDialog);
		}

		@Override
		public void onAllowedClick() {
			clearClick();
		}
	}

	private class ApplyClickListener extends LazyClickListener {

		private ApplyClickListener() {
			super(noDiscardChangesDialog);
		}

		@Override
		public void onAllowedClick() {
			onApplyClick(true);
		}
	}

	private List<FilterPart<?>> guiFilterElements = new LinkedList<FilterPart<?>>();
	IClickable<?> apply;
	IClickable<?> clear;
	private List<Boolean> activeFlags = new LinkedList<Boolean>();
	private FilterGrid grid;
	boolean holdFilterClicks = false;
	Validation validation = new Validation(true);
	private List<IFilterListener> listeners = new LinkedList<IFilterListener>();
	private Map<String, List<FilterImpl>> filterList = new HashMap<String, List<FilterImpl>>();
	private ComboBoxIntegerFilter sizeFilter;
	private boolean addSizeFilter = false;
	private IFilterConstraints constraints;
	private FilterPanel mainPanel;
	private ViewComboBox configurationComboBox;
	private String firstConfiguration = IFilterConstraints.COMMON;
	private String configuration = IFilterConstraints.COMMON;
	private boolean showConfiguration = true;
	IFilterPanel heights = Style.instance().filter();
	private ClearClickListener clearClickListener;
	private FilterPanel title;
	private boolean hyperlinksAdded;
	private boolean noDiscardChangesDialog;
	private ILabel titleLabel;

	FilterWidgetImpl() {
	}

	FilterWidgetImpl(IContainer panel) {
		setUp(panel);
	}

	IElement<?> setUp(IContainer panel) {
		title = newFilterPanel(panel);
		titleLabel = title.addTitle(title());
		mainPanel = title;
		return panel.element();
	}

	FilterPanel newFilterPanel(IContainer panel) {
		return new FilterPanelImpl(this, panel);
	}

	@Override
	public void refreshButton(IClickListener clickListener) {
		title.refreshButton(clickListener);
	}

	@Override
	public void onUpdate(String value) {
		update(value, false);
		// else
		// throw new UnsupportedOperationException("filter configuration "
		// + value + " has not been set up");
	}

	private void update(String value, boolean isInit) {
		configuration = value;
		if (value == null)
			return;
		if (filterList.get(configuration) != null)
			update(isInit);
	}

	@Override
	public IFilterWidget addConfiguration(String config) {
		if (configurationComboBox == null && showConfiguration) {
			configurationComboBox = mainPanel.viewComboBox();
			configurationComboBox.addUpdateListener(this);
			// configurationComboBox.height(FilterTemplate.HEIGHT);
		}
		if (configurationComboBox != null)
			configurationComboBox.addText(config);
		if (firstConfiguration.equals(IFilterConstraints.COMMON))
			firstConfiguration = config;
		configuration = config;
		return this;
	}

	void notifyListeners(ICallback<Void> cb) {
		IFilterConstraints constraints = constraints();
		listeners.get(0).onApply(constraints, cb);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public IFilterConstraints constraints() {
		List<FilterTemplate> activeFilters = new LinkedList<FilterTemplate>();
		for (FilterPart<?> filter : guiFilterElements) {
			boolean active = filter.update();
			if (guiFilterElements.indexOf(filter) == 0 && showConfiguration)
				active = false;
			if (active)
				activeFilters.add((FilterTemplate) filter);
		}
		FilterConstraintsImpl constraints = new FilterConstraintsImpl(
				configuration);
		if (!addSizeFilter)
			constraints.clearSizeFilter();
		if (this.constraints != null && this.constraints.sortOrder() != null) {
			constraints.sortOrder(this.constraints.sortOrder());
			constraints.sortDirection(this.constraints.sortDirection());
		}
		if (this.constraints != null && this.constraints.rowIterator() != null) {
			// if (this.constraints.size() != constraints.size())
			// this.constraints.clearRowIndex();
			constraints.rowIterator(this.constraints.rowIterator());
		}
		for (FilterTemplate filter : activeFilters) {
			constraints.add(filter.asConstraint());
		}
		if (sizeFilter != null)
			constraints.add(sizeFilter.asConstraint());
		else
			constraints.clearSizeFilter();
		assert listeners.size() <= 1;
		return constraints;
	}

	private void remove(String name) {
		for (String cfg : filterList.keySet()) {
			List<FilterImpl> filters = filterList.get(cfg);
			Iterator<FilterImpl> it = filters.iterator();
			while (it.hasNext()) {
				FilterImpl n = it.next();
				if (n.name.equals(name)) {
					it.remove();
				}
			}
		}
		update();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	FilterPart addFilter(Class contentType, final String name,
			boolean hasConstraints, FieldTypeImpl type, List values,
			List preset, IAdapter adapter, FilterImpl f) {
		assert name != null : contentType.getName();
		FilterPart filter;
		if (preset != null) {
			filter = new RelationFilter(this, grid, name,
					guiFilterElements.size(), preset, adapter,
					new IClickListener() {
						@Override
						public void onClick() {
							remove(name);
						}
					});
		} else if (hasConstraints) {
			if (contentType.equals(String.class)) {
				filter = new ComboBoxStringFilter(grid, name, type, values,
						guiFilterElements.size(), f.value, f.required,
						type.nullValue);
			} else if (contentType.equals(Integer.class)) {
				filter = new ComboBoxIntegerFilter(grid, name, values,
						guiFilterElements.size());
			} else if (contentType.equals(IImage.class)) {
				filter = new ComboBoxStringFilter(grid, name, type, values,
						guiFilterElements.size(), null, f.required, null);
			} else if (contentType.equals(Date.class)) {
				throw new UnsupportedOperationException(contentType.getName());
			} else
				throw new UnsupportedOperationException(contentType.getName());
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
			throw new UnsupportedOperationException(contentType.getName());
		activeFlags.add(false);
		filter.validate(validation);
		guiFilterElements.add(filter);
		filter.notifyCreate();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public IFilterWidget visible(boolean visible) {
		// if (configurationComboBox != null)
		// validation.linkInput(configurationComboBox);
		configuration = firstConfiguration;
		return update(true);
	}

	private IFilterWidget update() {
		return update(false);
	}

	@SuppressWarnings("rawtypes")
	private IFilterWidget update(boolean isInit) {
		grid = mainPanel.filterGrid();
		addHyperlinks();
		guiFilterElements.clear();
		addFilters4Configuration(IFilterConstraints.COMMON);
		if (configuration != null
				&& !configuration.equals(IFilterConstraints.COMMON))
			addFilters4Configuration(configuration);
		if (addSizeFilter) {
			sizeFilter = (ComboBoxIntegerFilter) addFilter(Integer.class,
					MAX_ROWS, true, null, DEFAULT_SIZES, null, null, null);
			sizeFilter.validate(validation);
		}
		boolean constrained = false;
		FilterPart<?> firstConstraint = null;
		if (constraints != null) {
			for (FilterPart<?> f : guiFilterElements) {
				FilterTemplate ft = (FilterTemplate) f;
				boolean c = ft.fromConstraint((IFilterConstraints) constraints);
				constrained = constrained | c;
				if (f instanceof RelationFilter)
					constrained = true;
				if (constrained && firstConstraint == null) {
					firstConstraint = f;
				}
			}
			if (constraints.size() != (Integer) DEFAULT_SIZES.get(0)
					&& constraints.size() != Integer.MAX_VALUE
					&& constraints.size() != -1) {
				if (sizeFilter != null) {
					sizeFilter.set(constraints.size());
					validation.update();
				}
				constrained = true;
				if (constrained && firstConstraint == null) {
					firstConstraint = sizeFilter;
				}
			}
		}
		for (FilterPart<?> f : guiFilterElements) {
			if (f instanceof RelationFilter)
				constrained = true;
		}
		// constrained |= !firstConfiguration.equals(configuration);
		boolean clickable = constrained && !isInit;
		apply.clickable(clickable);
		clearClickable(constrained);
		mainPanel.visible();
		grid.show(firstConstraint);
		return this;
	}

	void clearClickable(boolean constrained) {
		clear.clickable(constrained);
	}

	private void addHyperlinks() {
		if (!hyperlinksAdded) {
			apply = title
					.addHyperlink(
							Style.instance().filter()
									.acceptImage(title.isMiniFilter()), Style
									.instance().filter().acceptTitle(), true);
			validation.linkClickable(apply);
			clear = title.addHyperlink(Icons.CANCEL, Style.instance().filter()
					.clearTitle(), false);
			apply.addClickListener(new ApplyClickListener());
			clear.addClickListener(clearClickListener = new ClearClickListener());
			clearClickable(false);
			hyperlinksAdded = true;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addFilters4Configuration(String cfg) {
		List<FilterImpl> l = filterList.get(cfg);
		if (l != null) {
			grid.resize(l.size() + (addSizeFilter ? 1 : 0));
			for (FilterImpl filter : l) {
				List<Object> list = new LinkedList<Object>(
						filter.type.getConstraints());
				if (!filter.required && !list.isEmpty())
					list.add(0, "");
				List<Object> preset = null;
				IAdapter<Object, Object> adapter = null;
				if (filter instanceof RelationFilterImpl) {
					RelationFilterImpl rf = (RelationFilterImpl) filter;
					preset = rf.preset;
					adapter = rf.adapter;
				}
				FilterPart<?> fp = addFilter(filter.type.clazz, filter.name,
						filter.type.hasConstraints(), filter.type, list,
						preset, adapter, filter);
				holdFilterClicks = true;
				if (filter.text != null) {
					((ComboBoxStringFilter) fp).input.text(filter.text);
				}
				if (!filter.directApply)
					((ComboBoxFilterTemplate) fp).directApply = false;
				holdFilterClicks = false;
				if (filter.updateListener != null)
					fp.addUpdateListener(filter.updateListener);
			}
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
	public IFilterWidget apply(ICallback<Void> cb) {
		notifyListeners(cb);
		return this;
	}

	@Override
	public IFilterWidget clear() {
		clearClickListener.onAllowedClick();
		return this;
	}

	@Override
	public IFilterWidget showConfiguration(boolean show) {
		showConfiguration = show;
		return this;
	}

	@Override
	public IFilterWidget setConfiguration(String config) {
		if (configuration == null && config == null)
			return this;
		if (configuration != null && configuration.equals(config))
			return this;
		update(config, true);
		return this;
	}

	void register(ITextField tf) {
		tf.addKeyListener(new IClickListener() {

			@Override
			public void onClick() {
				if (apply.clickable()) {
					onApplyClick(true);
				}
			}
		}).enter();
	}

	void onApplyClick(boolean clickableClear) {
		if (holdFilterClicks)
			return;
		validation.update();
		if (clickableClear) {
			IFilterConstraints c = constraints();
			clearClickable(c != null && c.isSpecified());
		}
		notifyListeners(DummyCallback.voidInstance());
	}

	@Override
	public IFilterWidget addConfigurationListener(IUpdateListener<String> l) {
		mainPanel.viewComboBox().addUpdateListener(l);
		return this;
	}

	@Override
	public IFilterWidget addCancelListener(IClickListener cancelListener) {
		title.addHyperlink("cancel.png", "Cancel", false).addClickListener(
				cancelListener);
		return this;
	}

	@Override
	public IFilterWidget firstConfiguration(String firstConfiguration) {
		this.firstConfiguration = firstConfiguration;
		configuration = firstConfiguration;
		return this;
	}

	@Override
	public IFilterWidget noDiscardChangesDialog(boolean noDiscardChangesDialog) {
		this.noDiscardChangesDialog = noDiscardChangesDialog;
		return this;
	}

	@Override
	public WidgetTitle widgetTitle() {
		return title.widgetTitle();
	}

	@Override
	public IFilterWidget clearRowIndex() {
		if (constraints != null) {
			constraints.clearRowIndex();
		}
		return this;
	}

	void notifyComboBoxChange(boolean clickableClear) {
		if ((apply.clickable() || alwaysClickable()) && DIRECT_COMBOBO_CHANGE)
			onApplyClick(clickableClear);
	}

	boolean alwaysClickable() {
		return false;
	}

	@Override
	public IFilterWidget suggestionAdp(final ISuggestionAdp suggestionAdp) {
		if (suggestionAdp != null) {
			titleLabel.text(title());
			ISuggestField sf = title.top().suggestField();
			Heights.INSTANCE.decorate(sf);
			final TooltipTextInput t = new TooltipTextInput(sf,
					suggestionAdp.text());
			sf.tooltip(IFilterWidget.WILDCARD_TOOLTIP);
			sf.source(suggestionAdp);
			sf.height(26);
			sf.addSuggestionListener(new IUpdateListener<ISuggestion>() {
				@Override
				public void onUpdate(ISuggestion value) {
					t.clear();
					suggestionAdp.onUpdate(value);
				}
			});
			sf.margin().top(6).left(8).right(2).bottom(-2);// .right(-8);
			// sf.color().white();
		}
		return this;
	}

	String title() {
		return Style.instance().filter()
				.title(this instanceof MiniFilterWidgetImpl);
	}

	@Override
	public IFilterWidget updateFilters() {
		for (FilterPart<?> f : guiFilterElements) {
			f.updateFilter();
		}
		return this;
	}

	void clearClick() {
		if (holdFilterClicks)
			return;
		title.linksVisible(false);
		if (constraints != null)
			constraints.clear();
		for (List<FilterImpl> l : filterList.values()) {
			Iterator<FilterImpl> it = l.iterator();
			while (it.hasNext()) {
				if (it.next() instanceof RelationFilterImpl) {
					it.remove();
				}
			}
		}
		for (int i = guiFilterElements.size() - 1; i >= 0; i--) {
			guiFilterElements.get(i).clear();
		}
		clearClickable(false);
		apply.clickable(false);
		if (configuration != null
				&& !configuration.equals(firstConfiguration)
				&& configurationComboBox != null)
			configurationComboBox.text(firstConfiguration);
		else
			update();
		validation.update();
		notifyListeners(new CallbackTemplate<Void>() {
			@Override
			public void onSuccess(Void result) {
				title.linksVisible(true);
			}
		});
	}

	// @Override
	// public void hideApply() {
	// apply.clickable(false);
	// }

}

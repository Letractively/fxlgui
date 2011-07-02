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
package co.fxl.gui.mdt.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.impl.IFieldType;
import co.fxl.gui.impl.INavigationListener;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.NavigationView;
import co.fxl.gui.impl.SplitLayout;
import co.fxl.gui.mdt.api.IComboBoxLink;
import co.fxl.gui.mdt.api.IDeletableList;
import co.fxl.gui.mdt.api.IMDTFilterList;
import co.fxl.gui.mdt.api.IMasterDetailTableWidget;
import co.fxl.gui.mdt.api.IN2MRelation;
import co.fxl.gui.mdt.api.INavigationLink;
import co.fxl.gui.mdt.api.INavigationLink.INavigationLinkListener;
import co.fxl.gui.mdt.api.IPropertyGroup;
import co.fxl.gui.mdt.api.IPropertyPage;
import co.fxl.gui.mdt.api.IRelation;
import co.fxl.gui.mdt.api.IStateMemento;
import co.fxl.gui.mdt.api.IViewConfiguration;
import co.fxl.gui.mdt.api.IViewConfiguration.ActionType;
import co.fxl.gui.mdt.api.IViewConfiguration.ViewType;
import co.fxl.gui.tree.api.ITreeWidget;
import co.fxl.gui.tree.api.ITreeWidget.IViewID;

class MasterDetailTableWidgetImpl implements IMasterDetailTableWidget<Object>,
		IFilterListener {

	// TODO FEATURE: Option: Usability: In general: include an editable table
	// using:
	// http://gwt.google.com/samples/Showcase/Showcase.html#!CwCellSampler

	// TODO FEATURE: Option: Usability: Include context menu showing all
	// commands (top) and navigation buttons and view options (grid, details,
	// etc.)
	// http://stackoverflow.com/questions/3372096/why-doesnt-this-right-click-capture-in-gwt-work-in-ie
	// [maybe use the same widget as for grid view filter query tree selection]

	List<PropertyGroupImpl> propertyGroups = new LinkedList<PropertyGroupImpl>();
	List<RelationImpl> relations = new LinkedList<RelationImpl>();
	List<N2MRelationImpl> n2MRelations = new LinkedList<N2MRelationImpl>();
	boolean isDefaultPropertyGroup = false;
	IContent<Object> source;
	MDTFilterListImpl filterList = new MDTFilterListImpl(this);
	String title;
	IFilterConstraints constraints;
	List<Object> navigationLinks = new LinkedList<Object>();
	boolean hasFilter = false;
	ILayout layout;
	IVerticalPanel mainPanel;
	IVerticalPanel sidePanel;
	List<PropertyPageImpl> propertyPages = new LinkedList<PropertyPageImpl>();
	List<String> creatableTypes = new LinkedList<String>();
	SplitLayout splitLayout;
	boolean hideDetailRoot = false;
	List<Object> selection = new LinkedList<Object>();
	// private IComboBox comboBoxConfiguration;
	private List<String> configurations = new LinkedList<String>();
	Listener listener;
	String configuration = null;
	// IRadioButton r2;
	private boolean showDetailViewByDefault = false;
	boolean allowCreate = true;
	boolean allowMultiSelection = true;
	// private IRadioButton r1;
	IFilterWidget filterWidget;
	ViewTemplate activeView;
	List<Object> registerOrder = new LinkedList<Object>();
	boolean showCommands = true;
	private boolean filterable = true;
	PropertyPageImpl overviewPage = null;
	boolean allowGridView = true;
	Map<String, String> creatableTypeIcons = new HashMap<String, String>();
	int rowsInTable = 0;
	boolean allowCutPaste = false;
	private INavigationListener navigationListener;
	boolean refreshOnSwitch2Grid = false;
	boolean switch2grid = false;
	IDeletableList<Object> queryList;
	boolean allowReorder;
	IViewID selectedRegister;
	List<String> hiddenColumns = new LinkedList<String>();
	Map<IViewID, Object> relationRegisterSelection = new HashMap<IViewID, Object>();
	LinkedList<Object> preselection = new LinkedList<Object>();
	ViewWidget views;
	private IVerticalPanel filterPanel;
	private boolean alwaysShowFilter;
	private boolean neverShowFilter;
	boolean allowDetailView = true;
	private List<IUpdateListener<IViewConfiguration>> configurationListeners = new LinkedList<IUpdateListener<IViewConfiguration>>();

	MasterDetailTableWidgetImpl(IContainer layout) {
		this.layout = layout.panel();
	}

	void setUpSidePanel() {
		if (sidePanel == null) {
			splitLayout = new SplitLayout(this.layout);
			mainPanel = splitLayout.mainPanel;
			sidePanel = splitLayout.sidePanel;
		}
	}

	@Override
	public IMasterDetailTableWidget<Object> sidePanel(IVerticalPanel panel) {
		mainPanel = layout.vertical();
		// addSpacing = 10;
		sidePanel = panel;
		return this;
	}

	// TODO FEATURE: Option: Usability: MDT: Include List-Views using
	// HTML-Templates, HTML has entityName.fieldName (+clickable, +modifieable),
	// fieldName, # (current index), #primaryEntity.name, #primaryEntity.image.
	// HTML-Templates can be created using a generic html editor (with
	// additional buttons for meta-model-references). References are replaced
	// according to the type of the meta-model-entity. Use history listeners to
	// intercept hyperlink-clicks. click to show entity, click to change value
	// of property to specific value, etc. List-View has HTML-Template,
	// associated with filter query.
	// Filter-Query: Attribute Has-List-View=true/false, if true (condition
	// rule):
	// visible: HTML Template. Sort-By- & Group-By-ComboBoxes @
	// Filter-Side-Widget if list-view is active

	// TODO FEATURE: Option: Usability: Allow choosing the filter query using a
	// ComboBoxTreeWidget, structured in a tree (like Google "more")
	// http://gwt.google.com/samples/Showcase/Showcase.html#!CwMenuBar

	void addViewWidget(IVerticalPanel sidePanel) {
		if (!allowGridView || !allowDetailView)
			return;
		views = new ViewWidget(sidePanel.add().panel(), configurations,
				configuration, !alwaysShowFilter, neverShowFilter);
		views.addUpdateListener(new IUpdateListener<IViewConfiguration>() {
			@Override
			public void onUpdate(IViewConfiguration value) {
				configuration = value.configuration();
				if (constraints != null) {
					constraints.configuration(configuration);
				}
				if (value.viewType().equals(ViewType.DETAILS)) {
					filterWidget.constraints(constraints);
					filterWidget.setConfiguration(configuration);
				}
				udpateFilterPanel();
				if (value.actionType().equals(ActionType.REFRESH)) {
					queryList = null;
					refresh(null);
				} else if (value.viewType().equals(ViewType.TABLE)) {
					if (listener instanceof TableView)
						notifyConfigurationListener(configuration);
					else {
						switch2grid = true;
						if (filterWidget != null)
							filterWidget.setConfiguration(configuration);
						Object show = null;
						if (!selection.isEmpty())
							show = selection.get(selection.size() - 1);
						showTableView(show);
					}
				} else if (value.viewType().equals(ViewType.DETAILS)) {
					Object show = null;
					if (!selection.isEmpty())
						show = selection.get(selection.size() - 1);
					showDetailView(show);
				}
				for (IUpdateListener<IViewConfiguration> l : configurationListeners)
					l.onUpdate(value);
			}
		});
		return;
		// WidgetTitle views = new WidgetTitle(sidePanel.add().panel(), true)
		// .space(2);
		// if (!allowGridView)
		// views.visible(false);
		// views.addTitle("VIEWS");
		// IClickable<?> hl = views.addHyperlink(Icons.REFRESH, "Refresh");
		// hl.clickable(true);
		// hl.addClickListener(new LazyClickListener() {
		// @Override
		// public void onAllowedClick() {
		// queryList = null;
		// refresh(null);
		// }
		// });
		// IVerticalPanel content =
		// views.content().panel().vertical().addSpace(6);
		// IHorizontalPanel h1 = content.add().panel().horizontal().align()
		// .begin().add().panel().horizontal().align().begin().addSpace(4);
		// r1 = h1.add().radioButton().text("Grid");
		// IHorizontalPanel h2 = content.add().panel().horizontal().align()
		// .begin().add().panel().horizontal().align().begin().addSpace(4);
		// r2 = h2.add().radioButton().text("Master-Detail");
		// content.addSpace(4);
		// r1.checked(true);
		// r1.font().weight().bold();
		// r1.addUpdateListener(new LazyUpdateListener<Boolean>() {
		//
		// @Override
		// public void onAllowedUpdate(Boolean value) {
		// switch2grid = true;
		// Object show = null;
		// if (!selection.isEmpty())
		// show = selection.get(selection.size() - 1);
		// showTableView(show);
		// }
		//
		// @Override
		// public void onCancelledUpdate(Boolean value) {
		// r2.checked(true);
		// }
		// });
		// if (!configurations.isEmpty()) {
		// h1.addSpace(4);
		// comboBoxConfiguration = h1.add().comboBox();
		// comboBoxConfiguration.width(228);
		// new Heights(0).decorate(comboBoxConfiguration);
		// for (String c : configurations) {
		// if (configuration == null)
		// configuration = c;
		// comboBoxConfiguration.addText(c);
		// }
		// comboBoxConfiguration.text(configuration);
		// comboBoxConfiguration
		// .addUpdateListener(new LazyUpdateListener<String>() {
		//
		// @Override
		// public void onAllowedUpdate(final String value) {
		// configuration = value;
		// r1.checked(true);
		// if (listener instanceof TableView)
		// notifyConfigurationListener(value);
		// else {
		// if (filterWidget != null)
		// filterWidget.setConfiguration(value);
		// Object show = null;
		// if (!selection.isEmpty())
		// show = selection.get(selection.size() - 1);
		// showTableView(show);
		// }
		// }
		// });
		// }
		// r2.addUpdateListener(new LazyUpdateListener<Boolean>() {
		// @Override
		// public void onAllowedUpdate(Boolean value) {
		// if (value) {
		// Object show = null;
		// if (!selection.isEmpty())
		// show = selection.get(selection.size() - 1);
		// showDetailView(show);
		// }
		// }
		// });
		// r2.font().weight().bold();
		// r1.group().add(r2);
	}

	void setUpFilter(String configuration) {
		filterPanel = sidePanel.add().panel().vertical();
		if (neverShowFilter
				|| (!alwaysShowFilter && (filterList.filters.isEmpty() || !filterable)))
			filterPanel.visible(false);
		filterWidget = (IFilterWidget) filterPanel.add().widget(
				IFilterWidget.class);
		filterWidget.showConfiguration(false);
		int index = 0;
		for (MDTFilterImpl filter : filterList.filters) {
			if (!filter.inTable)
				continue;
			String config = filterList.configuration2index.get(index++);
			if (config != null)
				filterWidget.addConfiguration(config);
			if (filter instanceof MDTRelationFilterImpl) {
				MDTRelationFilterImpl rfi = (MDTRelationFilterImpl) filter;
				@SuppressWarnings("unchecked")
				IRelationFilter<Object, Object> rf = (IRelationFilter<Object, Object>) filterWidget
						.addRelationFilter();
				rf.name(rfi.name);
				rf.adapter(rfi.adapter);
				rf.preset(rfi.preset);
			} else if (filter.property != null) {
				if (filter.property.displayInTable) {
					IFilter ftr = filterWidget.addFilter().name(
							filter.property.name);
					IFieldType f = ftr.type().type(filter.property.type.clazz);
					if (!filter.property.type.values.isEmpty())
						for (Object o : filter.property.type.values)
							f.addConstraint(o);
				}
			} else
				throw new MethodNotImplementedException(filter.name);
		}
		if (constraints != null)
			filterWidget.constraints(constraints);
		if (!filterList.filters.isEmpty() && filterable)
			filterWidget.addSizeFilter();
		filterWidget.addFilterListener(new IFilterListener() {

			@Override
			public void onApply(IFilterConstraints constraints) {
				if (!switch2grid)
					queryList = null;
				MasterDetailTableWidgetImpl.this.onApply(constraints);
			}
		});
		filterWidget.visible(true);
		if (configuration != null)
			filterWidget.setConfiguration(configuration);
		// filterWidget.apply();
	}

	void notifyConfigurationListener(String value) {
		listener.onUpdate(value);
	}

	void addNavigationLinks(IVerticalPanel sidePanel) {
		if (!navigationLinks.isEmpty()) {
			NavigationView t = new NavigationView(sidePanel.add().panel());
			if (navigationListener != null)
				t.navigationViewListener(navigationListener);
			for (Object link0 : navigationLinks) {
				if (link0 instanceof NavigationLinkImpl) {
					NavigationLinkImpl link = (NavigationLinkImpl) link0;
					ImageButton l = t.addHyperlink(link.imageResource).text(
							link.name);
					for (final INavigationLinkListener<Object> cl : link.listeners) {
						l.addClickListener(new LazyClickListener() {

							@Override
							public void onAllowedClick() {
								cl.onClick(selection);
							}
						});
					}
					link.setLabel(l);
				} else {
					ComboBoxLinkImpl cbl = (ComboBoxLinkImpl) link0;
					IUpdateable<String> cb = t.addComboBoxLink(cbl.name,
							cbl.text, cbl.texts.toArray(new String[0]));
					for (IUpdateListener<String> ul : cbl.listeners)
						cb.addUpdateListener(ul);
				}
			}
		}
	}

	@Override
	public IPropertyGroup<Object> addPropertyGroup(String name) {
		PropertyGroupImpl propertyGroup = new PropertyGroupImpl(name);
		registerOrder.add(propertyGroup);
		propertyGroups.add(propertyGroup);
		return propertyGroup;
	}

	@Override
	public IPropertyGroup<Object> defaultPropertyGroup() {
		if (isDefaultPropertyGroup) {
			for (IPropertyGroup<Object> g : propertyGroups)
				if (g.name().equals(DETAILS))
					return g;
			throw new MethodNotImplementedException();
		}
		isDefaultPropertyGroup = true;
		return addPropertyGroup(DETAILS);
	}

	@Override
	public IRelation<Object, ?> addRelation(String name) {
		for (IRelation<Object, ?> r : relations) {
			if (r.name().equals(name)) {
				return r.reset();
			}
		}
		RelationImpl relation = new RelationImpl(this, name);
		registerOrder.add(relation);
		relations.add(relation);
		return relation;
	}

	@Override
	public IN2MRelation<Object, ?> addN2MRelation(String name) {
		N2MRelationImpl relation = new N2MRelationImpl(name);
		registerOrder.add(relation);
		n2MRelations.add(relation);
		return relation;
	}

	@Override
	public IMasterDetailTableWidget<Object> content(IContent<Object> source) {
		this.source = source;
		return this;
	}

	@Override
	public IMDTFilterList<Object> filterList() {
		hasFilter = true;
		return filterList;
	}

	@Override
	public IMasterDetailTableWidget<Object> showDetailViewByDefault() {
		showDetailViewByDefault = true;
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> visible(boolean visible) {
		if (!visible)
			return this;
		setUpSidePanel();
		addNavigationLinks(sidePanel);
		addViewWidget(sidePanel);
		setUpFilter(configuration);
		sidePanel = sidePanel.add().panel().vertical();
		if (!showDetailViewByDefault)
			showTableView(null);
		else {
			if (views != null)
				views.showDetails();
			// TODO show filter widget with relation constraint
			showDetailView(null);
		}
		return this;
	}

	void showTableView(Object object) {
		if (filterPanel != null && !neverShowFilter)
			filterPanel.visible(true);
		if (views != null)
			views.showTable();
		clear();
		activeView = new TableView(this, object);
		activeView.updateLinks();
		((TableView) activeView).onDelete(null);
	}

	DetailView showDetailView(Object show) {
		clear();
		udpateFilterPanel();
		DetailView dView = (DetailView) (activeView = new DetailView(this,
				show, false, null));
		activeView.updateLinks();
		dView.refresh();
		return dView;
	}

	public void showDetailView(Object show, boolean create, String createType) {
		clear();
		udpateFilterPanel();
		DetailView dView = (DetailView) (activeView = new DetailView(this,
				show, create, createType));
		activeView.updateLinks();
		dView.refresh();
	}

	private void clear() {
		if (mainPanel != null) {
			mainPanel.clear();
			sidePanel.clear();
		}
	}

	@Override
	public IMasterDetailTableWidget<Object> title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public INavigationLink<Object, Object> addNavigationLink(String name) {
		NavigationLinkImpl link = new NavigationLinkImpl(name);
		navigationLinks.add(link);
		return link;
	}

	@Override
	public IPropertyPage<Object> addPropertyPage(String name) {
		PropertyPageImpl link = new PropertyPageImpl(name);
		registerOrder.add(link);
		propertyPages.add(link);
		return link;
	}

	@Override
	public IMasterDetailTableWidget<Object> addCreatableType(String type) {
		assert type != null;
		creatableTypes.add(type);
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> addCreatableType(String type,
			String imageResource) {
		assert type != null;
		creatableTypes.add(type);
		creatableTypeIcons.put(type, imageResource);
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> hideDetailRoot() {
		hideDetailRoot = true;
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> refresh(ICallback<Boolean> cb) {
		listener.onDelete(cb);
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> addConfiguration(
			String configuration) {
		configurations.add(configuration);
		return this;
	}

	IFilterConstraints constraints() {
		// if (configuration == null)
		// return null;
		return constraints;
	}

	@Override
	public IMasterDetailTableWidget<Object> allowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
		return this;
	}

	@Override
	public List<Object> selection() {
		return selection;
	}

	@Override
	public IMasterDetailTableWidget<Object> selection(List<Object> selection) {
		this.selection = selection;
		activeView.selection(selection);
		return this;
	}

	void notifyLinks(List<Object> selection) {
		for (Object l0 : navigationLinks) {
			if (!(l0 instanceof NavigationLinkImpl))
				continue;
			NavigationLinkImpl l = (NavigationLinkImpl) l0;
			l.notifySelection(selection);
		}
	}

	@Override
	public IMasterDetailTableWidget<Object> allowMultiSelection(
			boolean multiSelection) {
		allowMultiSelection = multiSelection;
		return this;
	}

	@Override
	public void onApply(IFilterConstraints constraints) {
		this.constraints = constraints;
		activeView.onApply(constraints);
	}

	@Override
	public IMasterDetailTableWidget<Object> showCommands(boolean showCommands) {
		this.showCommands = showCommands;
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> filterable(boolean filterable) {
		this.filterable = filterable;
		return this;
	}

	@Override
	public IPropertyPage<Object> overviewPage() {
		if (overviewPage == null)
			overviewPage = new PropertyPageImpl("Overview");
		return overviewPage;
	}

	@Override
	public IMasterDetailTableWidget<Object> allowGridView(boolean allowGridView) {
		this.allowGridView = allowGridView;
		if (!allowGridView)
			showDetailViewByDefault = true;
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> allowDetailView(
			boolean allowDetailView) {
		this.allowDetailView = allowDetailView;
		return this;
	}

	@Override
	public ITreeWidget<Object> tree() {
		return ((DetailView) activeView).tree;
	}

	@Override
	public String getActiveConfiguration() {
		return configuration;
	}

	@Override
	public IMasterDetailTableWidget<Object> allowCutPaste(boolean allowCutPaste) {
		this.allowCutPaste = allowCutPaste;
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> navigationListener(
			INavigationListener l) {
		navigationListener = l;
		return this;
	}

	@Override
	public IStateMemento getState() {
		return new StateMementoImpl(this, null);
	}

	@Override
	public IStateMemento getState(IStatePacker<Object> packer) {
		return new StateMementoImpl(this, packer);
	}

	@Override
	public IMasterDetailTableWidget<Object> setState(IStateMemento state) {
		StateMementoImpl s = (StateMementoImpl) state;
		showDetailViewByDefault = s.showDetailView;
		constraints = s.constraints;
		configuration = s.configuration;
		if (!s.isPacked)
			selection = s.selection;
		else {
			preselection = s.selection;
		}
		selectedRegister = s.registerSelection;
		if (selectedRegister != null)
			relationRegisterSelection.put(selectedRegister,
					s.relationRegisterSelection);
		hiddenColumns = s.hiddenColumns;
		return this;
	}

	@Override
	public IComboBoxLink addComboBoxLink(String name) {
		ComboBoxLinkImpl cbl = new ComboBoxLinkImpl(name);
		navigationLinks.add(cbl);
		return cbl;
	}

	@Override
	public IMasterDetailTableWidget<Object> refreshOnSwitch2Grid(
			boolean refreshOnSwitch2Grid) {
		this.refreshOnSwitch2Grid = refreshOnSwitch2Grid;
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> allowReorder(boolean allowReorder) {
		this.allowReorder = allowReorder;
		return this;
	}

	@Override
	public IStateMemento createNavigationState(
			boolean addRegisterFilterConstraints) {
		StateMementoImpl stateMementoImpl = new StateMementoImpl();
		if (addRegisterFilterConstraints && activeView instanceof DetailView)
			stateMementoImpl.constraints = ((DetailView) activeView).relationDecoratorFilterConstraints;
		return stateMementoImpl;
	}

	@Override
	public IMasterDetailTableWidget<Object> alwaysShowFilter() {
		this.alwaysShowFilter = true;
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> neverShowFilter() {
		this.neverShowFilter = true;
		return this;
	}

	private void udpateFilterPanel() {
		if (filterPanel != null)
			filterPanel.visible(!neverShowFilter
					&& (alwaysShowFilter || configuration != null));
	}

	@Override
	public IUpdateable<IViewConfiguration> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<IViewConfiguration> listener) {
		configurationListeners.add(listener);
		return this;
	}
}
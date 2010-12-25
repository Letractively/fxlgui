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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.SplitLayout;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.mdt.api.IFilterList;
import co.fxl.gui.mdt.api.IMasterDetailTableWidget;
import co.fxl.gui.mdt.api.IN2MRelation;
import co.fxl.gui.mdt.api.INavigationLink;
import co.fxl.gui.mdt.api.IPropertyGroup;
import co.fxl.gui.mdt.api.IPropertyPage;
import co.fxl.gui.mdt.api.IRelation;

class MasterDetailTableWidgetImpl implements IMasterDetailTableWidget<Object> {

	List<PropertyGroupImpl> propertyGroups = new LinkedList<PropertyGroupImpl>();
	List<RelationImpl> relations = new LinkedList<RelationImpl>();
	List<N2MRelationImpl> n2MRelations = new LinkedList<N2MRelationImpl>();
	boolean isDefaultPropertyGroup = false;
	IContent<Object> source;
	FilterListImpl filterList = new FilterListImpl();
	String title;
	IFilterConstraints constraints;
	List<NavigationLinkImpl> navigationLinks = new LinkedList<NavigationLinkImpl>();
	boolean hasFilter = false;
	ILayout layout;
	IVerticalPanel mainPanel;
	IVerticalPanel sidePanel;
	List<PropertyPageImpl> propertyPages = new LinkedList<PropertyPageImpl>();
	List<String> creatableTypes = new LinkedList<String>();
	SplitLayout splitLayout;
	boolean hideDetailRoot = false;

	MasterDetailTableWidgetImpl(IContainer layout) {
		this.layout = layout.panel();
	}

	@Override
	public IPropertyGroup<Object> addPropertyGroup(String name) {
		PropertyGroupImpl propertyGroup = new PropertyGroupImpl(name);
		propertyGroups.add(propertyGroup);
		return propertyGroup;
	}

	@Override
	public IPropertyGroup<Object> defaultPropertyGroup() {
		isDefaultPropertyGroup = true;
		return addPropertyGroup("Details");
	}

	@Override
	public IRelation<Object, ?> addRelation(String name) {
		RelationImpl relation = new RelationImpl(name);
		relations.add(relation);
		return relation;
	}

	@Override
	public IN2MRelation<Object, ?> addN2MRelation(String name) {
		N2MRelationImpl relation = new N2MRelationImpl(name);
		n2MRelations.add(relation);
		return relation;
	}

	@Override
	public IMasterDetailTableWidget<Object> content(IContent<Object> source) {
		this.source = source;
		return this;
	}

	@Override
	public IFilterList<Object> filterList() {
		hasFilter = true;
		return filterList;
	}

	@Override
	public IMasterDetailTableWidget<Object> visible(boolean visible) {
		if (!visible)
			return this;
		showTableView(null);
		return this;
	}

	void showTableView(Object object) {
		clear();
		new TableView(this, object);
	}

	DetailView showDetailView(Object show) {
		clear();
		return new DetailView(this, show);
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
	public INavigationLink<Object> addNavigationLink(String name) {
		NavigationLinkImpl link = new NavigationLinkImpl(name);
		navigationLinks.add(link);
		return link;
	}

	@Override
	public IPropertyPage<Object> addPropertyPage(String name) {
		PropertyPageImpl link = new PropertyPageImpl(name);
		propertyPages.add(link);
		return link;
	}

	@Override
	public IMasterDetailTableWidget<Object> addCreatableType(String type) {
		creatableTypes.add(type);
		return this;
	}

	@Override
	public IMasterDetailTableWidget<Object> hideDetailRoot() {
		hideDetailRoot = true;
		return this;
	}
}

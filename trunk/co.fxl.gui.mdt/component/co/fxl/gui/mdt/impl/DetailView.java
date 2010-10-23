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

import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.filter.api.IFieldType;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.form.api.IFormWidget;
import co.fxl.gui.mdt.api.IFilterList;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.IFilterTreeWidget.ISource;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;

class DetailView implements ISource<Object> {

	private MasterDetailTableWidgetImpl widget;
	private IFilterTreeWidget<Object> tree;
	private IFilterList<Object> filterList;

	@SuppressWarnings("unchecked")
	DetailView(final MasterDetailTableWidgetImpl widget, Object show) {
		this.widget = widget;
		tree = (IFilterTreeWidget<Object>) widget.splitLayout.mainPanel.add()
				.widget(IFilterTreeWidget.class);
		tree.title(widget.title);
		filterList = tree
				.filterList(widget.splitLayout.sidePanel.add().panel());
		for (FilterImpl filter : widget.filterList.filters) {
			if (filter.property != null) {
				IFieldType type = filterList.addFilter().name(
						filter.property.name).type().type(
						filter.property.type.type);
				type.selection(filter.property.type.values);
			} else
				throw new MethodNotImplementedException();
		}
		if (widget.constraints != null)
			filterList.constraints(widget.constraints);
		tree.addHyperlink("Grid").addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				widget.showTableView(tree.selection());
			}
		});
		addDetailViews();
		tree.source(this);
		tree.selection(show);
		tree.expand();
		tree.visible(true);
	}

	private void addDetailViews() {
		if (widget.isDefaultPropertyGroup) {
			tree.setDetailView(new IDecorator<Object>() {

				@Override
				public void clear(IVerticalPanel panel) {
					panel.clear();
				}

				@Override
				public void decorate(IVerticalPanel panel, final Object node) {
					panel.clear();
					IFormWidget form = (IFormWidget) panel.add().widget(
							IFormWidget.class);
					for (PropertyGroupImpl group : widget.propertyGroups) {
						for (PropertyImpl property : group.properties) {
							if (property.displayInDetailView) {
								if (property.type.type.equals(String.class)) {
									if (property.type.isLong) {
										form
												.addTextArea(property.name)
												.valueElement()
												.text(
														String
																.valueOf(property.adapter
																		.valueOf(node)));

									} else {
										form
												.addTextField(property.name)
												.valueElement()
												.text(
														String
																.valueOf(property.adapter
																		.valueOf(node)));
									}
								}
							}
						}
					}
					form.visible(true);
				}
			});
		} else {
			throw new MethodNotImplementedException();
		}
	}

	@Override
	public ITree<Object> query(IFilterConstraints constraints) {
		widget.constraints = constraints;
		return widget.source.queryTree(constraints);
	}
}

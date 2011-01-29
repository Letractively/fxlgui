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

import java.util.List;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.IFieldType;
import co.fxl.gui.api.template.SplitLayout;
import co.fxl.gui.filter.api.IFilterWidget;
import co.fxl.gui.filter.api.IFilterWidget.IFilter;
import co.fxl.gui.filter.api.IFilterWidget.IFilterListener;
import co.fxl.gui.filter.api.IFilterWidget.IRelationFilter;
import co.fxl.gui.table.api.ISelection.IMultiSelection.IChangeListener;

abstract class ViewTemplate implements IChangeListener<Object>, Listener,
		IFilterListener {

	MasterDetailTableWidgetImpl widget;
	IFilterWidget filterWidget;

	ViewTemplate(MasterDetailTableWidgetImpl widget) {
		this.widget = widget;
		widget.listener = this;
		if (widget.mainPanel == null) {
			if (widget.hasFilter) {
				widget.splitLayout = new SplitLayout(widget.layout);
				widget.mainPanel = widget.splitLayout.mainPanel;
				widget.sidePanel = widget.splitLayout.sidePanel;
			} else {
				IVerticalPanel v = widget.layout.vertical();
				widget.mainPanel = v.add().panel().vertical();
				widget.sidePanel = v.add().panel().vertical().visible(false);
				// .grid().cell(1, 0).align()
				// .end().panel().vertical().width(300).spacing(10);
				// widget.sidePanel.visible(false);
				// v.addSpace(10);
				// widget.sidePanel.color().rgb(240, 240, 240);
				// widget.sidePanel.border().color().lightgray();
			}
		}
	}

	abstract boolean isRelevant(NavigationLinkImpl link);

	@Override
	public void onChange(List<Object> selection) {
		widget.selection = selection;
		for (ILabel label : widget.labels) {
			label.clickable(!selection.isEmpty());
		}
	}

	void setUpFilter(String configuration) {
		if (widget.filterList.filters.isEmpty())
			return;
		widget.sidePanel.addSpace(0);// widget.addSpacing);
		filterWidget = (IFilterWidget) widget.sidePanel.add().widget(
				IFilterWidget.class);
		filterWidget.showConfiguration(false);
		int index = 0;
		for (MDTFilterImpl filter : widget.filterList.filters) {
			if (!filter.inTable)
				continue;
			String config = widget.filterList.configuration2index.get(index++);
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
		if (widget.constraints != null)
			filterWidget.constraints(widget.constraints);
		filterWidget.addSizeFilter();
		filterWidget.addFilterListener(this);
		filterWidget.visible(true);
		if (configuration != null)
			filterWidget.setConfiguration(configuration);
		// filterWidget.apply();
	}
}

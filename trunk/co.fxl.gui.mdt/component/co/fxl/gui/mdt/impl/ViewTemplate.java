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

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.NavigationView;
import co.fxl.gui.api.template.SplitLayout;
import co.fxl.gui.mdt.api.INavigationLink.INavigationLinkListener;
import co.fxl.gui.table.api.ISelection.IMultiSelection.IChangeListener;

abstract class ViewTemplate implements IChangeListener<Object> {

	MasterDetailTableWidgetImpl widget;
	private List<Object> selection;
	private List<ILabel> labels = new LinkedList<ILabel>();

	ViewTemplate(MasterDetailTableWidgetImpl widget) {
		this.widget = widget;
		if (widget.mainPanel == null) {
			if (widget.hasFilter) {
				SplitLayout splitLayout = new SplitLayout(widget.layout);
				widget.mainPanel = splitLayout.mainPanel;
				widget.sidePanel = splitLayout.sidePanel;
			} else {
				IVerticalPanel v = widget.layout.vertical();
				widget.mainPanel = v.add().panel().vertical();
				// v.addSpace(10);
				widget.sidePanel = v.add().panel().grid().cell(1, 0).align()
						.end().panel().vertical().width(300).spacing(10);
				// widget.sidePanel.color().rgb(240, 240, 240);
				// widget.sidePanel.border().color().lightgray();
			}
		}
	}

	void addNavigationLinks() {
		List<NavigationLinkImpl> links = new LinkedList<NavigationLinkImpl>();
		for (NavigationLinkImpl link : widget.navigationLinks) {
			if (isRelevant(link)) {
				links.add(link);
			}
		}
		if (!links.isEmpty()) {
			NavigationView t = new NavigationView(widget.sidePanel.add()
					.panel());
			for (NavigationLinkImpl link : links) {
				ILabel l = t.addHyperlink().text(link.name);
				for (final INavigationLinkListener<Object> cl : link.listeners) {
					l.addClickListener(new IClickListener() {

						@Override
						public void onClick() {
							cl.onClick(selection);
						}
					});
					l.clickable(false);
				}
				labels.add(l);
			}
		}
	}

	abstract boolean isRelevant(NavigationLinkImpl link);

	@Override
	public void onChange(List<Object> selection) {
		this.selection = selection;
		for (ILabel label : labels) {
			label.clickable(!selection.isEmpty());
		}
	}
}

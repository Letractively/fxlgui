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
package co.fxl.gui.table.masterdetail.impl;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.SplitLayout;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.table.masterdetail.api.IDetailView;

class DetailViewImpl implements IDetailView<Object>, IClickListener {

	private MasterDetailTableWidgetImpl widget;
	private WidgetTitle widgetTitle;
	private IDecorator<Object> decorator;
	private Object bo;
	private ILabel boTitle;
	private IVerticalPanel contentPanel;
	SplitLayout panel;
	private ViewGadget viewGadget;
	String name;

	DetailViewImpl(MasterDetailTableWidgetImpl widget, ILayout layout) {
		this.widget = widget;
		panel = new SplitLayout(layout);
		this.widgetTitle = new WidgetTitle(panel.mainPanel.add().panel());
		widgetTitle.foldable(false);
		widgetTitle.addTitle("Details.").font().weight().plain().pixel(18)
				.color().gray();
		widgetTitle.addHyperlink("Back to List").addClickListener(this);
	}

	private void initViewNavigation() {
		if (viewGadget == null) {
			viewGadget = new ViewGadget(widget, panel.sidePanel.add().panel());
			for (DetailViewImpl view : widget.views) {
				viewGadget.addView(view, view == this);
			}
		}
	}

	@Override
	public IDetailView<Object> title(String title) {
		ILabel label = widgetTitle.addTitle(title + ".");
		label.font().pixel(18);
		label.font().weight().plain().pixel(18).color().gray();
		return this;
	}

	@Override
	public IDetailView<Object> name(String title) {
		this.name = title;
		return this;
	}

	@Override
	public IDetailView<Object> decorator(IDecorator<Object> decorator) {
		this.decorator = decorator;
		return this;
	}

	void bo(Object object) {
		this.bo = object;
	}

	void show() {
		initViewNavigation();
		if (boTitle == null) {
			boTitle = widgetTitle.addTitle(bo.toString());
			boTitle.font().pixel(18);
		} else
			boTitle.text(bo.toString());
		IVerticalPanel content = contentPanel();
		content.clear();
		if (decorator != null)
			decorator.decorate(bo, content, panel.sidePanel);
		widget.cardPanel.show(panel.panel);
	}

	private IVerticalPanel contentPanel() {
		if (contentPanel == null) {
			contentPanel = widgetTitle.content().panel().vertical();
		}
		return contentPanel;
	}

	@Override
	public void onClick() {
		widget.showTable();
	}
}

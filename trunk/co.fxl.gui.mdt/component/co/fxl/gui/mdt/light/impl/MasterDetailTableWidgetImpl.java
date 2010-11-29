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
package co.fxl.gui.mdt.light.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.mdt.light.api.IDetailView;
import co.fxl.gui.mdt.light.api.IMasterDetailTableWidget;
import co.fxl.gui.mdt.light.api.ITableView;

class MasterDetailTableWidgetImpl implements IMasterDetailTableWidget<Object> {

	ICardPanel cardPanel;
	private TableViewImpl masterView;
	List<DetailViewImpl> views = new LinkedList<DetailViewImpl>();

	MasterDetailTableWidgetImpl(IContainer layout) {
		cardPanel = layout.panel().card();
		masterView = new TableViewImpl(this, nextCard());
		masterView.isMaster = true;
	}

	@Override
	public ITableView<Object> masterView() {
		return masterView;
	}

	@Override
	public IDetailView<Object> addDetail() {
		DetailViewImpl view = new DetailViewImpl(this, nextCard());
		views.add(view);
		return view;
	}

	ILayout nextCard() {
		return cardPanel.add().panel();
	}

	@Override
	public IMasterDetailTableWidget<Object> visible(boolean visible) {
		masterView.visible(true);
		return this;
	}

	void showDetails(Object object) {
		if (views.isEmpty())
			return;
		for (DetailViewImpl view : views)
			view.bo(object);
		views.get(0).show();
	}

	void showTable() {
		masterView.show();
	}
}

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
package co.fxl.gui.api.template;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.ILayout;

public class MetaViewList {

	private SplitLayout layout;
	List<ViewList> viewLists = new LinkedList<ViewList>();
	private ICardPanel card;

	public MetaViewList(ILayout layout) {
		this.layout = new SplitLayout(layout);
	}

	ICardPanel contentPanel() {
		if (card == null) {
			card = layout.mainPanel.add().panel().card();
		}
		return card;
	}

	private ILayout addSidePanel() {
		return layout.sidePanel.add().panel();
	}

	public ViewList addViewList() {
		ViewList viewList = new ViewList(this, addSidePanel());
		viewLists.add(viewList);
		return viewList;
	}

	public void selectFirst() {
		viewLists.get(0).views.get(0).onClick();
	}
}

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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;


import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IElement;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;

class GWTCardPanel extends GWTPanel<DeckPanel, ICardPanel> implements
		ICardPanel {

	private List<Widget> widgets = new LinkedList<Widget>();
	private GWTElement<Widget, ?> topElement;

	@SuppressWarnings("unchecked")
	GWTCardPanel(GWTContainer<?> container) {
		super((GWTContainer<DeckPanel>) container);
		super.container.setComponent(new DeckPanel());
	}

	@Override
	public ICardPanel clear() {
		widgets.clear();
		topElement = null;
		return super.clear();
	}

	@Override
	public void add(Widget widget) {
		if (!widgets.contains(widget)) {
			widgets.add(widget);
		}
		container.widget.add(widget);
		if (this.topElement != null
				&& this.topElement.container.widget == widget) {
			Integer index = widgets.indexOf(widget);
			container.widget.showWidget(index);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICardPanel show(IElement<?> panel) {
		this.topElement = (GWTElement<Widget, ?>) panel;
		if (this.topElement.container.widget != null) {
			Integer index = widgets.indexOf(topElement.container.widget);
			if (index == -1)
				throw new RuntimeException(
						"element is no component of card panel");
			container.widget.showWidget(index);
		}
		return this;
	}
}

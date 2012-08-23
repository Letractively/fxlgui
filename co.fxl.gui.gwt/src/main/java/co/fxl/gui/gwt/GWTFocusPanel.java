/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
 * This file is part of  GUI API.
 *  
 *  GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 *  GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with  GUI API.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.fxl.gui.gwt;

import co.fxl.gui.api.IFocusPanel;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTFocusPanel extends GWTPanel<FocusPanel, IFocusPanel> implements
		IFocusPanel {

	public GWTFocusPanel(GWTContainer<?> container) {
		this(container, true);
	}

	@Override
	GWTClickHandler<IFocusPanel> newGWTClickHandler(IClickListener clickListener) {
		return new GWTClickHandler<IFocusPanel>(this, clickListener);
	}

	@SuppressWarnings("unchecked")
	public GWTFocusPanel(GWTContainer<?> container, boolean decorate) {
		super((GWTContainer<FocusPanel>) container);
		if (decorate) {
			container.widget.setWidth("100%");
			removeOutline((FocusPanel) container.widget);
		}
	}

	@Override
	public void add(Widget widget) {
		widget.setWidth("100%");
		container.widget.add(widget);
	}

	public static FocusPanel create() {
		FocusPanel p = new FocusPanel();
		removeOutline(p);
		return p;
	}

	public static void removeOutline(Widget p) {
		if (GWTDisplay.isInternetExplorer()) {
			p.addStyleName("nooutlineIE");
		} else {
			p.addStyleName("nooutline");
		}
	}

	@Override
	public IFocusPanel outline(boolean outline) {
		if (outline) {
			if (GWTDisplay.isInternetExplorer()) {
				container.widget.removeStyleName("nooutlineIE");
			} else {
				container.widget.removeStyleName("nooutline");
			}
		} else {
			removeOutline(container.widget);
		}
		return this;
	}
}

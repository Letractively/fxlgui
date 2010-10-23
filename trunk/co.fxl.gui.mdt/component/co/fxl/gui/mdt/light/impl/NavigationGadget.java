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

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.template.WidgetTitle;

class NavigationGadget {

	private static final String LINK_PNG = "link.png";
	private static final boolean SHOW_NUMBERS = false;
	private static final boolean SHOW_TRIANGLE = true;
	private WidgetTitle widgetTitle;
	private IVerticalPanel panel;
	private int index = 1;

	NavigationGadget(ILayout layout) {
		widgetTitle = new WidgetTitle(layout);
		widgetTitle.addTitle("Navigation");
		panel = widgetTitle.content().panel().vertical().spacing(4);
	}

	ILabel addHyperlink() {
		IHorizontalPanel panel = this.panel.add().panel().horizontal().add()
				.panel().horizontal();
		if (SHOW_NUMBERS) {
			String s = String.valueOf(index++) + ".";
			panel.add().label().text(s).font().pixel(13).color().gray();
			panel.addSpace(4);
		} else if (SHOW_TRIANGLE) {
			panel.add().image().resource(LINK_PNG);
			panel.addSpace(4);
		}
		ILabel textLabel = panel.add().label().hyperlink();
		textLabel.font().pixel(13).weight().bold();
		return textLabel;
	}
}
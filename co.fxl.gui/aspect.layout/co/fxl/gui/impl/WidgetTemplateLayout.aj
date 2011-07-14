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
package co.fxl.gui.impl;

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.layout.impl.Layout;

privileged aspect WidgetTemplateLayout {

	ILabel around(WidgetTitle widgetTitle, String text, IHorizontalPanel panel) : 
	execution(private ILabel WidgetTitle.addHyperlinkLabel(String, IHorizontalPanel)) 
	&& args(text, panel)
	&& this(widgetTitle) 
	&& if (widgetTitle.commandsOnTop) {
		return Layout.instance().createButtonLabel(panel);
	}

	after() : 
	execution(public void ViewImpl.onAllowedClick()) {
		Layout.instance().actionMenu().showContent();
	}

	after(SplitLayout sl) : 
	execution(private void SplitLayout.init()) 
	&& this(sl) {
		ActionMenuAdp l = new ActionMenuAdp(sl);
		Layout.instance().actionMenu().listener(l);
	}
}

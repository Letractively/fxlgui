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
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.layout.impl.Layout;

privileged aspect WidgetTemplateLayout {

	ILabel around(WidgetTitle widgetTitle, String text, IHorizontalPanel iPanel) : 
	call(private ILabel WidgetTitle.addHyperlinkLabel(String, IHorizontalPanel)) 
	&& withincode(public CommandLink WidgetTitle.addHyperlink(String, String))
	&& args(text, iPanel)
	&& this(widgetTitle) 
	&& if(Layout.ENABLED) {
		return widgetTitle.commandsOnTop ? Layout.instance().createButtonLabel(iPanel) : widgetTitle.addHyperlinkLabel(text, iPanel);
	}

	after() : 
	execution(public void ViewImpl.onAllowedClick()) 
	&& if(Layout.ENABLED) {
		Layout.instance().actionMenu().showContent();
	}

	after(SplitLayout sl) : 
	execution(private void SplitLayout.init()) 
	&& this(sl) 
	&& if(Layout.ENABLED) {
		ActionMenuAdp l = new ActionMenuAdp(sl);
		Layout.instance().actionMenu().listener(l);
	}
}

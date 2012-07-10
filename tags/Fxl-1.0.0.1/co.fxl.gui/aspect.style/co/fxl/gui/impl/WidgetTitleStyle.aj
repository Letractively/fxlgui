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
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.impl.Style;

privileged aspect WidgetTitleStyle {

	after(WidgetTitle widgetTitle, IHorizontalPanel panel) :
	execution(public void WidgetTitle.styleWindowHeaderButton(IHorizontalPanel)) 
	&& args(panel) 
	&& this(widgetTitle) 
	&& if(Style.ENABLED) {
		Style.instance().window().button(panel, widgetTitle.sideWidget);
	}

	ILabel around(WidgetTitle widgetTitle, String text, IHorizontalPanel iPanel) :
	execution(private ILabel WidgetTitle.addHyperlinkLabel(String, IHorizontalPanel)) 
	&& this(widgetTitle) 
	&& args(text, iPanel) 
	&& if(Style.ENABLED) {
		return Style.instance().window().addCommandLabel(iPanel, text, widgetTitle.sideWidget);
	}

	after(WidgetTitle widgetTitle) :
	execution(public WidgetTitle.new(ILayout, boolean)) 
	&& this(widgetTitle) 
	&& if(Style.ENABLED) {
		Style.instance().window().main(widgetTitle.panel)
				.header(widgetTitle.headerPanel, widgetTitle.sideWidget);
		if (Style.instance().window().commandsOnTop())
			widgetTitle.commandsOnTop();
	}

	after(WidgetTitle widgetTitle) :
	execution(public WidgetTitle WidgetTitle.sideWidget(boolean)) 
	&& this(widgetTitle) 
	&& if(Style.ENABLED) {
		Style.instance().window().header(widgetTitle.headerPanel, widgetTitle.sideWidget);
	}
	
	after(WidgetTitle widgetTitle, String title) returning(ILabel label) :
	execution(public ILabel WidgetTitle.addTitle(String))
	&& this(widgetTitle)
	&& args(title) 
	&& if(Style.ENABLED) {
		Style.instance().window().title(label, title, widgetTitle.sideWidget);
	}

	after(IPanel<?> panel) :
	execution(public void WidgetTitle.styleFooter(IPanel<?>)) 
	&& args(panel) 
	&& if(Style.ENABLED) {
		Style.instance().window().footer(panel);
	}

}
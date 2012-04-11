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
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.CommandLink;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.style.impl.Style;

privileged aspect WidgetTitleStyle {

	ILabel around(WidgetTitle widgetTitle, String text, IHorizontalPanel iPanel) :
	call(private ILabel WidgetTitle.addHyperlinkLabel(String, IHorizontalPanel))
	&& withincode(public CommandLink WidgetTitle.addHyperlink(String, String))
	&& this(widgetTitle) 
	&& args(text, iPanel) 
	&& if(Style.ENABLED) {
		return Style.instance().window()
				.addCommandLabel(iPanel, text, widgetTitle.sideWidget);
	}

	IImage around(IImage more, String r) :
	call(public IImage IImage.resource(String))
	&& withincode(public ILabel WidgetTitle.addTitle(String))
	&& args(r)
	&& target(more)
	&& if(Style.ENABLED) {
		more.resource(Style.instance().window().moreImage());
		return more;
	}

	after(WidgetTitle widgetTitle, IHorizontalPanel iPanel) :
	execution(public void WidgetTitle.styleWindowHeaderButton(IHorizontalPanel)) 
	&& args(iPanel) 
	&& this(widgetTitle) 
	&& if(Style.ENABLED) {
		Style.instance().window().button(iPanel, widgetTitle.sideWidget);
	}

	after(WidgetTitle widgetTitle) :
	execution(public WidgetTitle.new(ILayout, boolean, boolean)) 
	&& this(widgetTitle) 
	&& if(Style.ENABLED) {
		Style.instance().window().main(widgetTitle.panel, widgetTitle.addBorder)
				.header(widgetTitle.headerPanel, widgetTitle.sideWidget);
		if (Style.instance().window().commandsOnTop())
			widgetTitle.commandsOnTop();
	}

	after(WidgetTitle widgetTitle) :
	execution(public WidgetTitle WidgetTitle.sideWidget(boolean)) 
	&& this(widgetTitle) 
	&& if(Style.ENABLED) {
		Style.instance().window()
				.header(widgetTitle.headerPanel, widgetTitle.sideWidget);
	}

	after(WidgetTitle widgetTitle) :
	execution(private void WidgetTitle.setUp()) 
	&& this(widgetTitle) 
	&& if(Style.ENABLED) {
		Style.instance().window()
				.header(widgetTitle.headerPanel, widgetTitle.sideWidget);
	}

	after(WidgetTitle widgetTitle, String title) returning(ILabel label) :
	execution(public ILabel WidgetTitle.addTitle(String))
	&& this(widgetTitle)
	&& args(title) 
	&& if(Style.ENABLED) {
		Style.instance().window().title(label, title, widgetTitle.sideWidget);
	}

	after(IPanel<?> vertical) :
	execution(public void WidgetTitle.styleFooter(IPanel<?>)) 
	&& args(vertical) 
	&& if(Style.ENABLED) {
		Style.instance().window().footer(vertical);
	}

}

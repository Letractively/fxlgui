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
package co.fxl.gui.gwt;

import co.fxl.gui.automation.api.IAutomationListener.Key;
import co.fxl.gui.automation.impl.Automation;
import co.fxl.gui.impl.Display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public class GWTTextInputKeyPressHandler implements KeyPressHandler,
		ClickHandler {

	private GWTElement element;

	GWTTextInputKeyPressHandler(GWTElement element) {
		this.element = element;
	}

	@Override
	public void onKeyPress(KeyPressEvent arg0) {
		if (Automation.ENABLED && !arg0.isAltKeyDown()) {
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					Automation.listener().notifyValueChange(element);
				}
			});
		}
	}

	@Override
	public void onClick(ClickEvent arg0) {
		if (arg0.isAltKeyDown())
			Automation.listener().notifyClick(element, Key.ALT);
	}

	static void create(GWTTextInput element) {
		GWTTextInputKeyPressHandler handler = new GWTTextInputKeyPressHandler(
				element);
		Widget widget = element.container.widget;
		((HasKeyPressHandlers) widget).addKeyPressHandler(handler);
		((HasClickHandlers) widget).addClickHandler(handler);
	}
}
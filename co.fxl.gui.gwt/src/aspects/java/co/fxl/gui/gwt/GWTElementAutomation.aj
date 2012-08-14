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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;

aspect GWTElementAutomation {

	@SuppressWarnings("rawtypes")
	after(GWTElement element) :
	execution(GWTElement.new(GWTContainer))
	&& this(element)
	&& if(Automation.ENABLED) {
		Automation.listener().notifyNew(element);
	}

	@SuppressWarnings("rawtypes")
	before(GWTElement element) :
	execution(void GWTElement.fireClickListenersDoubleClick(DoubleClickEvent))
	&& this(element)
	&& if(Automation.ENABLED) {
		Automation.listener().notifyClick(element, Key.DOUBLE);
	}

	@SuppressWarnings("rawtypes")
	before(GWTElement element) :
	execution(void GWTElement.fireClickListenersKeyPress(KeyPressEvent))
	&& this(element)
	&& if(Automation.ENABLED) {
		Automation.listener().notifyClick(element, Key.LEFT);
	}

	@SuppressWarnings("rawtypes")
	before(GWTElement element, ClickEvent event) :
	execution(void GWTElement.fireClickListenersSingleClick(ClickEvent))
	&& this(element)
	&& args(event)
	&& if(Automation.ENABLED) {
		Key key = event.isAltKeyDown() ? Key.ALT : Key.LEFT;
		Automation.listener().notifyClick(element, key);
	}

}

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
package co.fxl.gui.form.impl;

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.layout.impl.Layout;
import co.fxl.gui.api.IPanel;

privileged aspect LoginWidgetImplLayout {

	void around(LoginWidgetImpl widget, IPanel<?> liPanel) : 
	call(private void LoginWidgetImpl.addLoginForm(IPanel<?>)) 
	&& withincode(* LoginWidgetImpl.*(..))
	&& args(liPanel)
	&& this(widget) 
	&& if(Layout.ENABLED) {
		
		// TODO move both statements to LoginImpl
		
		liPanel = LogInDialog.addButton(widget, liPanel);
		widget.addLoginForm(liPanel);
	}

	void around() : 
	call(private void LoginWidgetImpl.addLoggedInAs(IHorizontalPanel)) 
	&& withincode(* LoginWidgetImpl.*(..)) 
	&& if(Layout.ENABLED) {
		
		// TODO conditional proceed
	}
}

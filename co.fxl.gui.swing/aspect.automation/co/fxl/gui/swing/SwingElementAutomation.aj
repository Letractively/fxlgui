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
package co.fxl.gui.swing;

import java.awt.event.MouseEvent;

import co.fxl.gui.automation.impl.Automation;

aspect SwingElementAutomation {

	@SuppressWarnings("rawtypes")
	after(SwingElement e) :
	execution(SwingElement.new(SwingContainer))
	&& this(e)
	&& if(Automation.ENABLED) {
		Automation.listener().notifyNew(e);
	}

	@SuppressWarnings("rawtypes")
	before(SwingElement e) :
	execution(void SwingElement.fireClickListeners(MouseEvent))
	&& this(e)
	&& if(Automation.ENABLED) {
		Automation.listener().notifyClick(e);
	}

}

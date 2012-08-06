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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import co.fxl.gui.automation.impl.Automation;
import co.fxl.gui.impl.Display;

aspect SwingTextInputAutomation {

	declare parents : SwingTextInput implements Runnable;

	public void SwingTextInput.run() {
		Automation.listener().notifyValueChange(this);
	}

	@SuppressWarnings("rawtypes")
	after(final SwingTextInput element) :
	execution(SwingTextInput.new(SwingContainer))
	&& this(element) {
		element.container.component.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (Automation.ENABLED) {
					Display.instance().invokeLater(element);
				}
			}
		});
	}

}

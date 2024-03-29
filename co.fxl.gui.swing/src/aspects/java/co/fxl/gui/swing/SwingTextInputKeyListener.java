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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import co.fxl.gui.automation.api.IAutomationListener.Key;
import co.fxl.gui.automation.impl.Automation;
import co.fxl.gui.impl.Display;

@SuppressWarnings("rawtypes")
public class SwingTextInputKeyListener extends KeyAdapter {

	private SwingTextInput element;

	SwingTextInputKeyListener(SwingTextInput element) {
		this.element = element;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (Automation.ENABLED && !e.isAltDown()) {
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					Automation.listener().notifyValueChange(element);
				}
			});
		}
	}

	static void create(final SwingTextInput element) {
		element.container.component
				.addKeyListener(new SwingTextInputKeyListener(element));
		element.container.component.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isAltDown()) {
					Automation.listener().notifyClick(element, Key.ALT);
				}
			}
		});
	}
}
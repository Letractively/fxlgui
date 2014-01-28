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
package co.fxl.gui.automation.impl;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.automation.api.IAutomationAdapter;
import co.fxl.gui.automation.api.IAutomationListener;
import co.fxl.gui.impl.Display;

public class Automation {

	public final static boolean ENABLED = false;
	private static IAutomationListener instance = new IAutomationListener() {

		@Override
		public void notifyNew(IElement<?> e) {
		}

		@Override
		public void notifyClick(IElement<?> e, Key key) {
		}

		@Override
		public void notifyClick(IGridPanel g, int x, int y, Key key) {
		}

		@Override
		public void notifyValueChange(IElement<?> e) {
		}
	};

	public static IAutomationListener listener() {
		return instance;
	}

	public static void listener(IAutomationListener l) {
		instance = l;
	}

	public static boolean isEnabled() {
		return ENABLED && Display.instance().supports(IAutomationAdapter.class);
	}

}

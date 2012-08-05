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

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;

public class ElementListener {

	public enum Key {

		CTRL, SHIFT, DOUBLE, RIGHT;
	}

	public interface IElementListener {

		void notifyNew(IElement<?> e);

		void notifyClick(IElement<?> e, Key... key);

		void notifyClick(IGridPanel g, int x, int y, Key... key);

		void notifyValueChange(IElement<?> e);
	}

	public static boolean active = false;
	private static IElementListener instance;

	public static IElementListener instance() {
		return instance;
	}

	public static void instance(IElementListener l) {
		instance = l;
	}

}
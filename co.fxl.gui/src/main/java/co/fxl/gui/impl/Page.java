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

import co.fxl.gui.impl.DiscardChangesDialog.DiscardChangesListener;

public class Page {

	private static Page active = null;
	private ContextMenu contextMenu;
	private Boolean discardChangesState;
	private DiscardChangesListener discardChangesListener;

	private Page() {
		activate();
	}

	public static Page instance() {
		assert active != null;
		return active;
	}

	public static Page newInstance() {
		if (active != null)
			active.deactivate();
		return new Page();
	}

	public void deactivate() {
		if (DiscardChangesDialog.active()) {
			discardChangesState = true;
			discardChangesListener = DiscardChangesDialog.listener;
			DiscardChangesDialog.active(false);
			DiscardChangesDialog.listener = null;
		}
	}

	public void activate() {
		if (discardChangesState != null) {
			DiscardChangesDialog.active(discardChangesState);
			DiscardChangesDialog.listener = discardChangesListener;
			discardChangesState = null;
			discardChangesListener = null;
		}
		active = this;
		if (contextMenu != null)
			contextMenu.activate();
	}

	public ContextMenu contextMenu() {
		if (contextMenu == null)
			contextMenu = ContextMenu.newInstance();
		return contextMenu;
	}
}

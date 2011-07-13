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

import co.fxl.gui.layout.api.IActionMenu;

class SplitLayoutActionMenuAdp implements IActionMenu.IActionMenuListener {

	private SplitLayout splitLayout;

	SplitLayoutActionMenuAdp(SplitLayout splitLayout) {
		this.splitLayout = splitLayout;
		splitLayout.sidePanel.spacing().left(10);
		int width = splitLayout.panel.width();
		splitLayout.cell0.width(width);
		splitLayout.cell1.width(width);
		onShowContent();
	}

	private void onShow(boolean b) {
		splitLayout.cell0.visible(b);
		splitLayout.cell1.visible(!b);
	}

	@Override
	public void onShowContent() {
		onShow(true);
	}

	@Override
	public void onShowMenu() {
		onShow(false);
	}
}

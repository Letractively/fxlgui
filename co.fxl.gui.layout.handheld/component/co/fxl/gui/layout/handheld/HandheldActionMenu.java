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
package co.fxl.gui.layout.handheld;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.impl.ImageButton;
import co.fxl.gui.layout.api.IActionMenu;

class HandheldActionMenu implements IActionMenu {

	private IClickListener contentClickListener;
	private IActionMenuListener listener;

	@Override
	public IActionMenu container(IContainer container) {
		IHorizontalPanel panel = container.panel().horizontal();
		final ImageButton table = new ImageButton(panel.add()).hyperlink()
				.text("Content");
		panel.addSpace(4).add().label().text("|").font().color().gray();
		panel.addSpace(4);
		final ImageButton actions = new ImageButton(panel.add()).hyperlink()
				.text("Menu");
		panel.addSpace(10);
		table.addClickListener(contentClickListener = new IClickListener() {
			@Override
			public void onClick() {
				table.clickable(false);
				actions.clickable(true);
				listener.onShowContent();
			}
		});
		actions.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				table.clickable(true);
				actions.clickable(false);
				listener.onShowMenu();
			}
		});
		table.clickable(false);
		return this;
	}

	@Override
	public IActionMenu showContent() {
		contentClickListener.onClick();
		return this;
	}

	@Override
	public IActionMenu listener(IActionMenuListener l) {
		this.listener = l;
		return this;
	}
}
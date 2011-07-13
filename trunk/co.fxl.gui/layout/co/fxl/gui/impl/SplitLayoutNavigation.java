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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IHorizontalPanel;

public class SplitLayoutNavigation {

	// TODO generic mini menu on Container

	public static IHorizontalPanel panel;
	private static IClickListener tableClickListener;

	public static void addNavigation(final SplitLayout sl) {
		sl.sidePanel.spacing().left(10);
		sl.cell1.visible(false);
		sl.cell0.width(sl.panel.width());
		if (panel != null) {
			panel.clear();
			final ImageButton table = new ImageButton(panel.add()).hyperlink()
					.text("Content");// .imageResource("grid.png");
			panel.addSpace(4).add().label().text("|").font().color().gray();
			panel.addSpace(4);
			final ImageButton actions = new ImageButton(panel.add())
					.hyperlink().text("Menu");// .imageResource("link.png");
			panel.addSpace(10);
			table.addClickListener(tableClickListener = new IClickListener() {
				@Override
				public void onClick() {
					table.clickable(false);
					actions.clickable(true);
					sl.cell0.visible(true).width(sl.panel.width());
					sl.cell1.visible(false);
				}
			});
			actions.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					table.clickable(true);
					actions.clickable(false);
					sl.cell1.visible(true).width(sl.panel.width());
					sl.cell0.visible(false);
				}
			});
			table.clickable(false);
		}
	}

	public static void showTable() {
		tableClickListener.onClick();
	}
}

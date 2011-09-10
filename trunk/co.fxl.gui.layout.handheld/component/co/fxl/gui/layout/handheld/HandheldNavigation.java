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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.layout.api.ILayout.INavigation;
import co.fxl.gui.layout.api.ILayout.INavigation.INavigationGroup.INavigationItem;

class HandheldNavigation implements INavigation, IClickListener {

	private List<INavigationGroup> groups = new LinkedList<INavigationGroup>();
	private ILinearPanel<?> panel;

	@Override
	public INavigation panel(ILinearPanel<?> panel) {
		this.panel = panel;
		panel.addSpace(4).add().label().text("More >>").hyperlink()
				.addClickListener(this);
		return this;
	}

	@Override
	public INavigation groups(List<INavigationGroup> groups) {
		this.groups = groups;
		return this;
	}

	@Override
	public INavigation group(INavigationGroup group) {
		groups.add(group);
		return this;
	}

	@Override
	public void onClick() {
		final IDialog dialog = panel.display().showDialog()
				.title("SELECT REGISTER");
		dialog.addButton().cancel().addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				dialog.visible(false);
			}
		});
		IVerticalPanel sp = dialog.container().panel().vertical().spacing(6)
				.add().panel().vertical().spacing(4);
		boolean isFirst = true;
		for (final INavigationGroup group : groups) {
			if (group.name() != null) {
				if (!isFirst)
					sp.addSpace(4);
				isFirst = false;
				String text = group.name();
				sp.add().label().text(text).font().weight().bold().pixel(11);
			}
			for (final INavigationItem item : group.items()) {
				sp.add().label().text(item.name()).hyperlink()
						.addClickListener(new IClickListener() {

							@Override
							public void onClick() {
								dialog.visible(false);
								item.active();
							}
						}).mouseLeft().font().pixel(13);
			}
		}

		// TODO ensure correct visibility

		dialog.visible(true);
	}
}

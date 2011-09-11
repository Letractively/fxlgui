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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.layout.api.ILayout.INavigation;
import co.fxl.gui.layout.api.ILayout.INavigation.INavigationGroup.INavigationItem;

class HandheldNavigation implements INavigation, IClickListener {

	private List<INavigationGroup> groups = new LinkedList<INavigationGroup>();
	private IPanel<?> panel;
	private Map<INavigationItem, Boolean> visible = new HashMap<INavigationItem, Boolean>();

	@Override
	public INavigation panel(IPanel<?> panel) {
		this.panel = panel;
		panel.add().panel().horizontal().addSpace(4).add().label()
				.text("More >>").hyperlink().addClickListener(this);
		return this;
	}

	@Override
	public INavigation groups(List<INavigationGroup> groups) {
		this.groups = groups;
		visible.clear();
		updateVisible(true);
		return this;
	}

	private void updateVisible(boolean init) {
		for (INavigationGroup g : groups) {
			boolean visible = false;
			for (INavigationItem i : g.items()) {
				if (init)
					this.visible.put(i, true);
				i.updateVisible(i.isActive());
				visible |= i.isActive();
			}
			if (g.name() != null)
				g.visible(visible);
		}
	}

	@Override
	public INavigation group(INavigationGroup group) {
		visible.clear();
		groups.clear();
		groups.add(group);
		updateVisible(true);
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
			if (!hasVisibleItem(group))
				continue;
			if (group.name() != null) {
				if (!isFirst)
					sp.addSpace(4);
				isFirst = false;
				String text = group.name();
				sp.add().label().text(text).font().weight().bold().pixel(11);
			}
			for (final INavigationItem item : group.items()) {
				if (visible.get(item))
					sp.add().label().text(item.name()).hyperlink()
							.addClickListener(new IClickListener() {

								@Override
								public void onClick() {
									dialog.visible(false);
									item.updateActive();
									updateVisible(false);
								}
							}).mouseLeft().font().pixel(13);
			}
		}
		dialog.visible(true);
	}

	private boolean hasVisibleItem(INavigationGroup group) {
		for (INavigationItem item : group.items())
			if (visible.get(item))
				return true;
		return false;
	}

	@Override
	public INavigation visible(INavigationItem item, boolean visible) {
		this.visible.put(item, visible);
		updateVisible(false);
		return this;
	}

	@Override
	public INavigation active(INavigationItem item, boolean active) {
		updateVisible(false);
		return this;
	}
}

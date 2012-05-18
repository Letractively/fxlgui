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
import co.fxl.gui.layout.api.INavigationGroupLayout;
import co.fxl.gui.layout.api.INavigationItemLayout;
import co.fxl.gui.layout.api.INavigationLayout;

class HandheldNavigation implements INavigationLayout, IClickListener {

	private List<INavigationGroupLayout> groups = new LinkedList<INavigationGroupLayout>();
	private IPanel<?> panel;
	private Map<INavigationItemLayout, Boolean> visible = new HashMap<INavigationItemLayout, Boolean>();

	@Override
	public INavigationLayout panel(IPanel<?> panel) {
		this.panel = panel;
		panel.add().panel().horizontal().addSpace(4).add().label()
				.text("More >>").hyperlink().addClickListener(this);
		return this;
	}

	@Override
	public INavigationLayout groups(List<INavigationGroupLayout> groups) {
		this.groups = groups;
		visible.clear();
		updateVisible(true);
		return this;
	}

	private void updateVisible(boolean init) {
		for (INavigationGroupLayout g : groups) {
			boolean visible = false;
			for (INavigationItemLayout i : g.items()) {
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
	public INavigationLayout group(INavigationGroupLayout group) {
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
		for (final INavigationGroupLayout group : groups) {
			if (!hasVisibleItem(group))
				continue;
			if (group.name() != null) {
				if (!isFirst)
					sp.addSpace(4);
				isFirst = false;
				String text = group.name();
				sp.add().label().text(text).font().weight().bold().pixel(11);
			}
			for (final INavigationItemLayout item : group.items()) {
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

	private boolean hasVisibleItem(INavigationGroupLayout group) {
		for (INavigationItemLayout item : group.items())
			if (visible.get(item))
				return true;
		return false;
	}

	@Override
	public INavigationLayout visible(INavigationItemLayout item, boolean visible) {
		this.visible.put(item, visible);
		updateVisible(false);
		return this;
	}

	@Override
	public INavigationLayout active(INavigationItemLayout item, boolean active) {
		updateVisible(false);
		return this;
	}
}

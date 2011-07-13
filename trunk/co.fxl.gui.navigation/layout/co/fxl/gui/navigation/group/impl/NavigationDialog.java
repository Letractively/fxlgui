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
package co.fxl.gui.navigation.group.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.layout.impl.Layout;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.group.api.INavigationWidget.INavigationListener;

public class NavigationDialog {

	public static void addButton(final NavigationWidgetImpl widget) {
		widget.masterPanel.addSpace(4).add().label().text("More >>")
				.hyperlink().addClickListener(new IClickListener() {
					@Override
					public void onClick() {
						final IDialog dialog = widget.masterPanel.display()
								.showDialog().title("SELECT REGISTER");
						dialog.addButton().cancel()
								.addClickListener(new IClickListener() {
									@Override
									public void onClick() {
										dialog.visible(false);
									}
								});
						IVerticalPanel sp = dialog.container().panel()
								.vertical().spacing(6).add().panel().vertical()
								.spacing(4);
						boolean isFirst = true;
						for (final NavigationGroupImpl group : widget.groups) {
							if (!isFirst)
								sp.addSpace(4);
							isFirst = false;
							String text = group.header.text();
							sp.add().label().text(text).font().weight().bold()
									.pixel(11);
							for (final NavigationItemImpl item : group.items) {
								sp.add().label().text(item.button.text())
										.hyperlink()
										.addClickListener(new IClickListener() {

											@Override
											public void onClick() {
												dialog.visible(false);
												item.active();
											}
										}).mouseLeft().font().pixel(13);
							}
						}
						dialog.visible(true);
					}
				}).mouseLeft();
		IContainer c = widget.hPanel.cell(1, 0).align().end().valign().center()
				.panel().horizontal().add();
		Layout.instance().actionMenu().container(c);
		widget.addNavigationListener(new INavigationListener() {
			@Override
			public void onBeforeNavigation(INavigationItem activeItem,
					boolean viaClick, ICallback<Void> cb) {
				NavigationItemImpl item = (NavigationItemImpl) activeItem;
				for (NavigationGroupImpl group : widget.groups) {
					group.visible(item.group == group);
				}
				for (NavigationItemImpl groupItem : item.group.items) {
					groupItem.basicPanel.visible(groupItem == item);
				}
				cb.onSuccess(null);
			}
		});
	}
}

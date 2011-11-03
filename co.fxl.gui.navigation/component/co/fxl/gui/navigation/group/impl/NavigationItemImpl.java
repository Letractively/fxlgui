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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.navigation.group.api.INavigationItem;

class NavigationItemImpl extends LazyClickListener implements INavigationItem {

	ILabel button;
	private IDecorator decorator;
	IHorizontalPanel buttonPanel;
	private NavigationWidgetImpl widget;
	private ILinearPanel<?> itemPanel;
	IHorizontalPanel basicPanel;
	NavigationGroupImpl group;
	private List<INavigationListener> listeners = new LinkedList<INavigationListener>();
	private IImage refresh;

	NavigationItemImpl(NavigationGroupImpl group) {
		this.group = group;
		widget = group.widget;
		itemPanel = group.itemPanel;
	}

	void initButtonPanel() {
		if (basicPanel == null) {
			basicPanel = itemPanel.add().panel().horizontal();
			basicPanel.addSpace(3);
			buttonPanel = basicPanel.add().panel().horizontal();
			buttonPanel.spacing(5).align().center();
			buttonPanel.addSpace(2);
			buttonPanel.border().width(1).style().noBottom();
			refresh = buttonPanel.add().image().resource("loading_white.gif")
					.visible(false);
			button = buttonPanel.add().label();
			button.font().pixel(14).weight().bold().color().white();
			buttonPanel.addSpace(3);
			buttonPanel.addClickListener(this);
			showLabelAsInactive();
		}
	}

	@Override
	public INavigationItem name(String name) {
		initButtonPanel();
		button.text(name);
		return this;
	}

	void showLabelAsInactive() {
		if (buttonPanel == null)
			return;
		button.font().color().white();
		buttonPanel.clickable(true);
		applyGradient(buttonPanel.color(), widget.colorInactive,
				widget.colorInactiveGradient);
		for (INavigationListener l : listeners)
			l.onActive(false);
	}

	@Override
	public IVerticalPanel addExtraPanel() {
		showLabelAsActive(false, null, false);
		return widget.panel1().clear();
	}

	@Override
	public INavigationItem initDecorator(IDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public void onAllowedClick() {
		active(true);
	}

	@Override
	public NavigationItemImpl active() {
		return updateActive();
	}

	public NavigationItemImpl updateActive() {
		return active(false);
	}

	static int c = 1;

	NavigationItemImpl active(boolean viaClick) {
		final NavigationItemImpl oldActive = widget.active;
		showLabelAsActive(viaClick, new CallbackTemplate<Void>() {
			@Override
			public void onSuccess(Void result) {
				IVerticalPanel panel0 = widget.flipPage().next().panel()
						.vertical();
				applyColor(panel0.color(), widget.colorActive);
				int width = buttonPanel.width();
				int height = buttonPanel.height();
				refresh.visible(true);
				button.visible(false);
				buttonPanel.size(width, height);
				decorator.decorate(panel0, new CallbackTemplate<Void>() {
					@Override
					public void onSuccess(Void result) {
						showLabelAsActive();
						if (oldActive != null
								&& oldActive != NavigationItemImpl.this) {
							oldActive.showLabelAsInactive();
						}
						refresh.visible(false);
						button.visible(true);
						widget.flipPage().flip();
					}
				});
			}
		}, true);
		return this;
	}

	private void showLabelAsActive(boolean viaClick,
			co.fxl.gui.impl.ICallback<Void> cb, boolean notify) {
		widget.active(this, viaClick, cb, notify);
		for (INavigationListener l : listeners)
			l.onActive(true);
	}

	private void showLabelAsActive() {
		button.font().color().black();
		buttonPanel.clickable(false);
		applyColor(buttonPanel.color(), widget.colorActive);
	}

	private void applyColor(IColor color, int[] rgb) {
		color.remove();
		color.rgb(rgb[0], rgb[1], rgb[2]);
	}

	private void applyGradient(IColor color, int[] rgb, int[] rgb2) {
		color.rgb(rgb[0], rgb[1], rgb[2]).gradient().vertical()
				.rgb(rgb2[0], rgb2[1], rgb2[2]);
	}

	@Override
	public INavigationItem back() {
		widget.panel1().clear();
		widget.panel0();
		return this;
	}

	@Override
	public boolean isActive() {
		return widget.activeItem() == this;
	}

	@Override
	public String name() {
		return button.text();
	}

	@Override
	public IDecorator initDecorator() {
		return decorator;
	}

	@Override
	public NavigationItemImpl visible(boolean visible) {
		updateVisible(visible);
		group.updateVisible();
		return this;
	}

	public NavigationItemImpl updateVisible(boolean visible) {
		basicPanel.visible(visible);
		return this;
	}

	@Override
	public INavigationItem addListener(INavigationListener l) {
		listeners.add(l);
		return this;
	}

	@Override
	public boolean visible() {
		return basicPanel.visible();
	}
}

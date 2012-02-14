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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.navigation.group.api.INavigationItem;

public class NavigationItemImpl extends LazyClickListener implements
		INavigationItem {

	public static int SPACING_LOADING = Constants.get(
			"NavigationItemImpl.SPACING_LOADING", 5);
	// TODO when row height computation in scrolltablewidgetimpl is working for
	// invisible panels (unflipped pages), set to true
	private static boolean FLIP_AFTER_RETURN_IS_POSSIBLE = true;
	private static boolean FLIP_AFTER_RETURN = Constants.get(
			"NavigationItemImpl.FLIP_AFTER_RETURN", true);
	static int c = 1;
	ILabel button;
	private IDecorator decorator;
	IHorizontalPanel buttonPanel;
	private NavigationWidgetImpl widget;
	private ILinearPanel<?> itemPanel;
	IHorizontalPanel basicPanel;
	NavigationGroupImpl group;
	private List<INavigationListener> listeners = new LinkedList<INavigationListener>();
	private IImage refresh;
	private IBorder border;

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
			border = buttonPanel.border().width(1).style().noBottom();
			IHorizontalPanel subPanel = buttonPanel.add().panel().horizontal();
			refresh = subPanel.add().image().resource("loading_white.gif")
					.visible(false);
			button = subPanel.add().label();
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
		showLabelAsInactive(true);
	}

	void showLabelAsInactive(boolean notify) {
		if (buttonPanel == null)
			return;
		button.visible(true);
		buttonPanel.spacing(5);
		refresh.visible(false);
		button.font().color().white();
		buttonPanel.clickable(true);
		border.color()
				.mix()
				.rgb(widget.colorInactive[0], widget.colorInactive[1],
						widget.colorInactive[2])
				.rgb(widget.colorInactiveGradient[0],
						widget.colorInactiveGradient[1],
						widget.colorInactiveGradient[2]);
		applyGradient(buttonPanel.color(), widget.colorInactive,
				widget.colorInactiveGradient);
		if (notify)
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

	NavigationItemImpl active(boolean viaClick) {
		showLabelAsActive(viaClick, new CallbackTemplate<Void>() {
			@Override
			public void onSuccess(Void result) {
				IVerticalPanel panel0 = widget.flipPage().next().panel()
						.vertical();
				applyColor(panel0.color(), widget.colorActive);
				int width = buttonPanel.width();
				int height = buttonPanel.height();
				showLoading();
				buttonPanel.size(width, height);
				if (!flipAfterReturn())
					flipPage();
				try {
					decorator.decorate(panel0, new CallbackTemplate<Void>() {
						@Override
						public void onSuccess(Void result) {
							flipRegister(flipAfterReturn());
						}

						@Override
						public void onFail(Throwable t) {
							resetLabel();
							super.onFail(t);
						}
					});
				} catch (Exception e) {
					onFail(e);
				}
			}

			private boolean flipAfterReturn() {
				return FLIP_AFTER_RETURN && FLIP_AFTER_RETURN_IS_POSSIBLE;
			}

			@Override
			public void onFail(Throwable t) {
				resetLabel();
				super.onFail(t);
			}
		}, true);
		return this;
	}

	private void showLabelAsActive(boolean viaClick,
			co.fxl.gui.api.ICallback<Void> cb, boolean notify) {
		widget.active(this, viaClick, cb, notify);
		for (INavigationListener l : listeners)
			l.onActive(true);
	}

	private void showLabelAsActive() {
		button.font().color().black();
		buttonPanel.clickable(false);
		border.color().gray();
		applyColor(buttonPanel.color(), widget.colorActive);
		button.visible(true);
		refresh.visible(false);
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

	private void resetLabel() {
		showLabelAsInactive();
		buttonPanel.spacing(5);
		refresh.visible(false);
		button.visible(true);
	}

	private void showRestAsInactive() {
		for (NavigationGroupImpl g : group.widget.groups) {
			for (NavigationItemImpl i : g.items) {
				if (i != NavigationItemImpl.this) {
					i.showLabelAsInactive(false);
				}
			}
		}
	}

	private void showLoading() {
		buttonPanel.spacing(SPACING_LOADING);
		refresh.visible(true);
		button.visible(false);
		for (NavigationGroupImpl g : group.widget.groups) {
			for (NavigationItemImpl i : g.items) {
				if (i != NavigationItemImpl.this) {
					i.refresh.visible(false);
					i.button.visible(true);
				}
			}
		}
	}

	private void flipRegister(boolean flipNow) {
		showRestAsInactive();
		showLabelAsActive();
		if (flipNow)
			flipPage();
	}

	void flipPage() {
		widget.flipPage().flip();
	}
}

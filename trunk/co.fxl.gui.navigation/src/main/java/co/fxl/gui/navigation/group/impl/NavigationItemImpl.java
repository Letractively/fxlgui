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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.navigation.api.ITabDecorator;
import co.fxl.gui.navigation.group.api.INavigationItem;

public class NavigationItemImpl extends LazyClickListener implements
		INavigationItem {

	static final int POPUP_WIDTH = 280;
	public static int SPACING_LOADING = Constants.get(
			"NavigationItemImpl.SPACING_LOADING", 5);
	// TODO when row height computation in scrolltablewidgetimpl is working for
	// invisible panels (unflipped pages), set to true
	private static boolean FLIP_AFTER_RETURN_IS_POSSIBLE = true;
	private static boolean FLIP_AFTER_RETURN = Constants.get(
			"NavigationItemImpl.FLIP_AFTER_RETURN", true);
	static int c = 1;
	ILabel button;
	private ITabDecorator decorator;
	IHorizontalPanel buttonPanel;
	public NavigationWidgetImpl widget;
	private ILinearPanel<?> itemPanel;
	IHorizontalPanel basicPanel;
	NavigationGroupImpl group;
	// private List<INavigationListener> listeners = new
	// LinkedList<INavigationListener>();
	private IImage refresh;
	private IBorder border;
	private boolean enabled = true;
	private boolean labelAsActive;
	private boolean isMoreTab;

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
		buttonPanel.border().remove();
		showLabelAsInactive(true);
	}

	void showLabelAsInactive(boolean notify) {
		if (buttonPanel == null)
			return;
		labelAsActive = false;
		button.visible(true);
		buttonPanel.spacing(5);
		refresh.visible(false);
		button.font().color().white();
		showBackgroundInactive();
		// if (notify)
		// for (INavigationListener l : listeners)
		// l.onActive(false);
	}

	void showBackgroundInactive() {
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
	}

	void showBackgroundNeutral() {
		border.remove();
		buttonPanel.color().remove();
		refresh.resource("more_black.png");
	}

	@Override
	public IVerticalPanel addExtraPanel() {
		showLabelAsActive(false, null, false);
		return widget.panel1().clear();
	}

	@Override
	public INavigationItem decorator(ITabDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	IPopUp popUp;
	private boolean visible = true;

	@Override
	public void onAllowedClick() {
		if (isMoreTab) {
			if (popUp == null) {
				popUp = Display.instance().showPopUp().autoHide(true);
				popUp.border().remove();
				// popUp.border().color().gray();// .mix().white().lightgray();
				popUp.border().style().shadow();
				popUp.width(280);
				popUp.addVisibleListener(new IUpdateListener<Boolean>() {
					@Override
					public void onUpdate(Boolean value) {
						if (!value) {
							hidePopUp();
						}
					}
				});
				IVerticalPanel panel = popUp.container().panel().vertical();
				panel.width(POPUP_WIDTH);
				panel.color().white();
				refresh.resource("more_black.png");
				decorator.decorate(panel, new CallbackTemplate<Void>() {
					@Override
					public void onSuccess(Void result) {
						border.color().gray();
						// border.style().shadow();
						buttonPanel.color().remove();
						buttonPanel.color().white();
						// refresh.resource("more_black.png");
						int x = basicPanel.offsetX() - getLeftPartPopUpWidth();
						if (x < 10)
							x = 10;
						popUp.offset(x,
								basicPanel.offsetY() + basicPanel.height());
						popUp.visible(true);
					}
				});
				buttonPanel.clickable(false);
			}
		} else
			setActive(true);
	}

	@Override
	public NavigationItemImpl active(boolean active) {
		if (!displayed()) {
			displayed(true);
			widget.active = this;
			widget.update();
		}
		assert active;
		return updateActive();
	}

	public NavigationItemImpl updateActive() {
		return setActive(false);
	}

	NavigationItemImpl setActive(boolean viaClick) {
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
		// for (INavigationListener l : listeners)
		// l.onActive(true);
	}

	private void showLabelAsActive() {
		labelAsActive = true;
		button.font().color().black();
		buttonPanel.clickable(false);
		showBackgroundActive();
		button.visible(true);
		refresh.visible(false);
	}

	void showBackgroundActive() {
		border.color().gray();
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
	public INavigationItem closeExtraPanel() {
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
	public ITabDecorator decorator() {
		return decorator;
	}

	@Override
	public NavigationItemImpl visible(boolean visible) {
		this.visible = visible;
		updateVisible();
		group.updateVisible();
		if (!visible && popUp != null)
			popUp.visible(false);
		return this;
	}

	public NavigationItemImpl updateVisible() {
		basicPanel.visible(visible && enabled);
		return this;
	}

	// @Override
	// public INavigationItem addListener(INavigationListener l) {
	// listeners.add(l);
	// return this;
	// }

	@Override
	public boolean visible() {
		return visible;
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
		toggleLoading(true);
		for (NavigationGroupImpl g : group.widget.groups) {
			for (NavigationItemImpl i : g.items) {
				if (i != NavigationItemImpl.this) {
					i.toggleLoading(false);
				}
			}
		}
	}

	@Override
	public INavigationItem toggleLoading(boolean t) {
		refresh.resource(labelAsActive ? "loading_black.gif"
				: "loading_white.gif");
		refresh.visible(t);
		button.visible(!t);
		return this;
	}

	private void flipRegister(boolean flipNow) {
		// buttonPanel.border().remove();
		showRestAsInactive();
		showLabelAsActive();
		if (flipNow)
			flipPage();
	}

	void flipPage() {
		widget.flipPage().flip();
	}

	@Override
	public INavigationItem enabled(boolean enabled) {
		this.enabled = enabled;
		updateVisible();
		group.updateVisible();
		return this;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	int width() {
		return basicPanel.width();
	}

	@Override
	public INavigationItem moreTab() {
		initButtonPanel();
		isMoreTab = true;
		refresh.resource("more_black.png").visible(true);
		button.visible(false);
		buttonPanel.spacing(SPACING_LOADING);
		refresh.addMouseOverListener(new IMouseOverListener() {

			@Override
			public void onMouseOver() {
				if (popUp == null) {
					refresh.resource("more.png");
					showBackgroundInactive();
				}
			}

			@Override
			public void onMouseOut() {
				if (popUp == null)
					showBackgroundNeutral();
			}
		});
		showBackgroundNeutral();
		return this;
	}

	void hidePopUp() {
		showBackgroundNeutral();
		// refresh.resource("more.png");
		popUp = null;
	}

	void displayed(boolean b) {
		enabled(b);
	}

	boolean displayed() {
		return enabled;
	}

	int getLeftPartPopUpWidth() {
		return 280 - basicPanel.width();
	}
}

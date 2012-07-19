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
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.Events;
import co.fxl.gui.impl.Events.EventListener;
import co.fxl.gui.impl.Events.ListenerRegistration;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.log.impl.Log;
import co.fxl.gui.navigation.api.ITabDecorator;
import co.fxl.gui.navigation.group.api.INavigationItem;

public class NavigationItemImpl extends LazyClickListener implements
		INavigationItem {

	static final int POPUP_WIDTH = 280;
	public static int SPACING_LOADING = Constants.get(
			"NavigationItemImpl.SPACING_LOADING", 5);
	// TODO when row height computation in scrolltablewidgetimpl is working for
	// invisible panels (unflipped pages), set to true
	private static boolean USE_TEMP_FLIP = true;
	private static boolean FLIP_AFTER_RETURN_IS_POSSIBLE = true;
	private static boolean FLIP_AFTER_RETURN = Constants.get(
			"NavigationItemImpl.FLIP_AFTER_RETURN", true);
	static int c = 1;
	ILabel button;
	private ITabDecorator decorator;
	IHorizontalPanel buttonPanel;
	public NavigationWidgetImpl widget;
	private IHorizontalPanel itemPanel;
	IHorizontalPanel basicPanel;
	NavigationGroupImpl group;
	// private List<INavigationListener> listeners = new
	// LinkedList<INavigationListener>();
	private IImage refresh;
	private IBorder border;
	private boolean enabled = true;
	private boolean labelAsActive;
	private boolean isMoreTab;
	IFocusPanel focusPanel;
	int[] colorInactive;
	private int[] colorInactiveGradient;

	NavigationItemImpl(NavigationGroupImpl group) {
		this.group = group;
		widget = group.widget;
		itemPanel = group.itemPanel;
		colorInactive = group.colorInactive;
		colorInactiveGradient = group.colorInactiveGradient;
	}

	void initButtonPanel() {
		if (basicPanel == null) {
			focusPanel = itemPanel.add().panel().focus();
			basicPanel = focusPanel.add().panel().horizontal();
			basicPanel.addSpace(3);
			buttonPanel = basicPanel.add().panel().horizontal();
			buttonPanel.spacing(5).align().center();
			buttonPanel.addSpace(2);
			border = buttonPanel.border().width(1).style().noBottom();
			IHorizontalPanel subPanel = buttonPanel.add().panel().horizontal();
			button = subPanel.add().label();
			button.font().pixel(14).weight().bold().color().white();
			button.addClickListener(this);
			refresh = subPanel.add().image().visible(false).size(16, 16);
			refreshResource("loading_white.gif");
			refresh.addClickListener(this);
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
		clickable(true);
		border.color()
				.mix()
				.rgb(colorInactive[0], colorInactive[1], colorInactive[2])
				.rgb(colorInactiveGradient[0], colorInactiveGradient[1],
						colorInactiveGradient[2]);
		applyGradient(buttonPanel.color(), colorInactive, colorInactiveGradient);
	}

	void clickable(boolean b) {
		buttonPanel.clickable(b);
		// button.clickable(b);
		// refresh.clickable(b);
		// refresh.opacity(1);
	}

	void showBackgroundNeutral() {
		border.remove();
		buttonPanel.color().remove();
		refreshResource("more_black.png");
	}

	@Override
	public IVerticalPanel addExtraPanel() {
		forkLabelAsActive(false, null, false);
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
				decorator.decorate(panel, new CallbackTemplate<Void>() {
					@Override
					public void onSuccess(Void result) {
						border.color().gray();
						// border.style().shadow();
						buttonPanel.color().remove();
						buttonPanel.color().white();
						button.font().color().black();
						refreshResource("more_black.png");
						// refresh.resource("more_black.png");
						int x = basicPanel.offsetX() - getLeftPartPopUpWidth();
						if (x < 10)
							x = 10;
						popUp.offset(x,
								basicPanel.offsetY() + basicPanel.height());
						popUp.visible(true);
						clickable(false);
					}
				});
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
		forkLabelAsActive(viaClick, new CallbackTemplate<Void>() {

			private ListenerRegistration reg1 = null;
			private ListenerRegistration reg2 = null;

			@Override
			public void onSuccess(Void result) {
				Log.instance().start("Showing tab " + button.text());
				IVerticalPanel panel0 = widget.flipPage().next().panel()
						.vertical();
				widget.activeBackground(panel0);
				int width = buttonPanel.width();
				int height = buttonPanel.height();
				showLoading();
				buttonPanel.size(width, height);
				if (!flipAfterReturn())
					flipPage();
				if (USE_TEMP_FLIP && flipAfterReturn()) {
					widget.flipPage().showNext();
					Events.instance().clear();
					reg1 = Events.instance().register(Events.SERVER_CALL_START,
							new EventListener() {
								@Override
								public void notifyEvent() {
									widget.flipPage().showCurrent();
								}
							});
					reg2 = Events.instance().register(
							Events.SERVER_CALL_RETURN, new EventListener() {
								@Override
								public void notifyEvent() {
									widget.flipPage().showNext();
								}
							});
				}
				try {
					decorator.decorate(panel0, new CallbackTemplate<Void>() {

						private void removeRegistrations() {
							if (reg1 != null) {
								reg1.remove();
								reg2.remove();
								widget.flipPage().showCurrent();
							}
						}

						@Override
						public void onSuccess(Void result) {
							removeRegistrations();
							Log.instance().stop("Showing tab " + button.text());
							flipRegister(flipAfterReturn());
							widget.update();
						}

						@Override
						public void onFail(Throwable t) {
							removeRegistrations();
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

	private void forkLabelAsActive(boolean viaClick,
			co.fxl.gui.api.ICallback<Void> cb, boolean notify) {
		widget.active(this, viaClick, cb, notify);
		// for (INavigationListener l : listeners)
		// l.onActive(true);
	}

	private void showLabelAsActive() {
		labelAsActive = true;
		button.font().color().black();
		clickable(false);
		showBackgroundActive();
		button.visible(true);
		refresh.visible(false);
		widget.addSeparatorBorder();
	}

	void showBackgroundActive() {
		border.color().gray();
		widget.activeBackground(buttonPanel);
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
		if (this.visible == visible)
			return this;
		this.visible = visible;
		updateVisible();
		group.updateVisible();
		if (!visible && popUp != null)
			popUp.visible(false);
		widget.update();
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
		String resource = labelAsActive ? "loading_black.gif"
				: "loading_white.gif";
		refreshResource(resource);
		refresh.visible(t).size(16, 16);
		button.visible(!t);
		return this;
	}

	private void refreshResource(String resource) {
		refresh.resource(resource);
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
		refreshResource("more_black.png");
		refresh.visible(true);
		button.text("More").font().color().black();
		buttonPanel.spacing(SPACING_LOADING);
		// IMouseOverListener mol = new IMouseOverListener() {
		//
		// @Override
		// public void onMouseOver() {
		// if (popUp == null) {
		// button.font().color().white();
		// refresh.resource("more.png");
		// showBackgroundInactive();
		// }
		// }
		//
		// @Override
		// public void onMouseOut() {
		// if (popUp == null) {
		// button.font().color().black();
		// showBackgroundNeutral();
		// }
		// }
		// };
		// focusPanel.addMouseOverListener(mol);
		showBackgroundNeutral();
		return this;
	}

	void hidePopUp() {
		popUp = null;
		showBackgroundNeutral();
		clickable(true);
		// refresh.resource("more.png");
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

	@Override
	public void showTitleAsEmpty(boolean empty) {
	}

	@Override
	public INavigationItem colorInactive(int[] inactive, int[] inactiveGradient) {
		colorInactive = inactive;
		colorInactiveGradient = inactiveGradient;
		return this;
	}
}
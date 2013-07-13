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

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
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
import co.fxl.gui.impl.DummyCallback;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.HyperlinkMouseOverListener;
import co.fxl.gui.impl.IContentPage;
import co.fxl.gui.impl.LazyClickListener;
import co.fxl.gui.impl.PopUp;
import co.fxl.gui.impl.ResizableWidgetTemplate;
import co.fxl.gui.impl.ServerCallCache;
import co.fxl.gui.impl.Shell;
import co.fxl.gui.log.api.ILog.IMeasurement;
import co.fxl.gui.log.impl.Log;
import co.fxl.gui.navigation.api.ITabDecorator;
import co.fxl.gui.navigation.group.api.INavigationItem;
import co.fxl.gui.navigation.impl.BufferedPanelImpl;
import co.fxl.gui.style.impl.Style;

public class NavigationItemImpl extends ResizableWidgetTemplate implements
		INavigationItem, IClickListener {

	static final int POPUP_WIDTH = 280;
	protected static final boolean ALLOW_CACHING = true;
	public static int SPACING_LOADING = Constants.get(
			"NavigationItemImpl.SPACING_LOADING", 5);
	// TODO when row height computation in scrolltablewidgetimpl is working for
	// invisible panels (unflipped pages), set to true
	private static boolean USE_TEMP_FLIP = Constants.get(
			"NavigationItemImpl.USE_TEMP_FLIP", true);
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
	private boolean enabled = true;
	private boolean labelAsActive;
	private boolean isMoreTab;
	IFocusPanel focusPanel;
	private IContentPage flipPage;
	// private boolean isFirst = true;
	private IVerticalPanel cached;
	private int lastWidth;
	private int lastHeight;

	NavigationItemImpl(NavigationGroupImpl group) {
		this.group = group;
		widget = group.widget;
		itemPanel = group.itemPanel;
		lastWidth = Shell.instance().width(group.itemPanel);
		lastHeight = Shell.instance().height(group.itemPanel);
	}

	void initButtonPanel() {
		if (basicPanel == null) {
			focusPanel = itemPanel.add().panel().focus();
			basicPanel = focusPanel.add().panel().horizontal();
			basicPanel.addSpace(3);
			buttonPanel = basicPanel.add().panel().horizontal();
			buttonPanel.spacing(5).align().center();
			buttonPanel.addSpace(2);
			IHorizontalPanel subPanel = buttonPanel.add().panel().horizontal();
			button = subPanel.add().label();
			new HyperlinkMouseOverListener(button);
			button.font().pixel(14).weight().bold();
			LazyClickListener clickListener = new LazyClickListener() {
				@Override
				protected void onAllowedClick() {
					if (buttonPanel.clickable())
						NavigationItemImpl.this.onClick();
				}
			};
			button.addClickListener(clickListener);
			refresh = subPanel.add().image().visible(false).size(16, 16);
			refreshResource(refreshInactive());
			refresh.addClickListener(clickListener);
			buttonPanel.addSpace(1);
			buttonPanel.addClickListener(clickListener);
			showLabelAsInactive();
			flipPage = widget.flipPage().newPage();
		}
	}

	private String refreshInactive() {
		return Style.instance().navigation().inactiveRefreshImage();
	}

	private String refreshActive() {
		return Style.instance().navigation().activeRefreshImage();
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
		Style.instance().navigation().inactive(buttonPanel, button);
		clickable(true);
		// if (notify)
		// for (INavigationListener l : listeners)
		// l.onActive(false);
	}

	@Override
	public NavigationItemImpl clickable(boolean b) {
		buttonPanel.clickable(b);
		button.clickable(b);
		refresh.clickable(b);
		refresh.opacity(1);
		return this;
	}

	void showBackgroundNeutral() {
		buttonPanel.border().remove();
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
		setResizableWidget(decorator, "decorator");
		return this;
	}

	IPopUp popUp;
	private boolean visible = true;
	private IMeasurement measurement;

	boolean popUpMovedRight() {
		int x = basicPanel.offsetX() - getLeftPartPopUpWidth();
		return x < 10;
	}

	@Override
	public void onClick() {
		if (widget.isLoading)
			return;
		if (isMoreTab) {
			setUpMoreTab();
		} else
			setActive(true, DummyCallback.voidInstance());
	}

	private void setUpMoreTab() {
		if (popUp == null) {
			clickable(false);
			popUp = PopUp.showPopUp(true).autoHide(true);
			// popUp.border().width(0);
			popUp.width(280);
			popUp.addVisibleListener(new IUpdateListener<Boolean>() {
				@Override
				public void onUpdate(Boolean value) {
					if (!value) {
						hidePopUp();
						button.font().color().black();
					}
				}
			});
			IVerticalPanel panel = popUp.container().panel().vertical();
			panel.width(POPUP_WIDTH);
			panel.color().white();
			decorator.decorate(new BufferedPanelImpl(panel),
					new CallbackTemplate<Void>() {

						@Override
						public void onSuccess(Void result) {
							// border.style().shadow();
							refreshResource("more_black.png");
							Style.instance().navigation()
									.activeMore(buttonPanel, button, refresh);
							// refresh.resource("more_black.png");
							int x = basicPanel.offsetX()
									- getLeftPartPopUpWidth() - 2;
							if (x < 10) {
								x = buttonPanel.offsetX();
							}
							popUp.offset(x,
									basicPanel.offsetY() + basicPanel.height());
							popUp.visible(true);
							clickable(false);
						}
					});
		}
	}

	@Override
	public void active(final boolean active, final ICallback<Void> cb) {
		final boolean b = !displayed();
		NavigationItemImpl previous = widget.active;
		if (b) {
			displayed(true);
			widget.active = NavigationItemImpl.this;
			widget.update();
		}
		widget.active = previous;
		assert active;
		updateActive(new CallbackTemplate<Void>(cb) {
			@Override
			public void onSuccess(Void result) {
				if (b) {
					widget.active = NavigationItemImpl.this;
				}
				cb.onSuccess(null);
			}
		});
	}

	public NavigationItemImpl updateActive(ICallback<Void> cb) {
		return setActive(false, cb);
	}

	NavigationItemImpl setActive(boolean viaClick, final ICallback<Void> cb0) {
		if (widget.activeItem() != null) {
			widget.activeItem().updateDisplaySize();
		}
		widget.setResizableWidget(this, "activeItem");
		final boolean wasActive = isActive();
		ServerCallCache.instance().record(true);
		boolean cachingActiveTemp = cached != null && ALLOW_CACHING;
		final boolean requiresResize = cachingActiveTemp
				&& (lastWidth != Shell.instance().width(itemPanel) || lastHeight != Shell
						.instance().height(itemPanel));
		if (requiresResize && Env.is(Env.IE)) {
			cachingActiveTemp = false;
		}
		final boolean cachingActive = cachingActiveTemp;
		if (!Env.is(Env.SWING))
			startLoading(cachingActive);
		forkLabelAsActive(viaClick, new CallbackTemplate<Void>(cb0) {

			@Override
			public void onSuccess(Void result) {
				if (Env.is(Env.SWING))
					startLoading(cachingActive);
				widget.flipPage().active(flipPage);
				final CallbackTemplate<Void> cb = new CallbackTemplate<Void>(
						cb0) {

					private void removeRegistrations() {
						if (USE_TEMP_FLIP) {
							widget.listeningOnServerCalls(false);
							// widget.flipPage().back();
						}
						// isFirst = false;
					}

					@Override
					public void onSuccess(Void result) {
						removeRegistrations();
						flipRegister(true);
						widget.update();
						decorator.finishDecorate();
						stopLoading(cachingActive);
						checkResize();
						cb0.onSuccess(result);
					}

					private void checkResize() {
						if (requiresResize) {
							String msg = "Resizing tab " + name();
							IMeasurement m = Log.instance().start(msg);
							widget.recursiveResize(
									Shell.instance().width(itemPanel), Shell
											.instance().height(itemPanel));
							m.stop();
						}
						updateDisplaySize();
					}

					@Override
					public void onFail(Throwable t) {
						removeRegistrations();
						resetLabel();
						stopLoading(cachingActive);
						checkResize();
						flipPage.revert();
						cached = null;
						super.onFail(t);
					}
				};
				try {
					// if (isFirst || !widget.flipPage().supportsRefresh()) {
					if (cachingActive) {
						if (!wasActive) {
							flipPage.next().element(cached);
							preview();
						}
						decorator.refresh(cb);
					} else {
						cached = flipPage.next().panel().vertical();
						preview();
						decorator.decorate(new BufferedPanelImpl(cached), cb);
						if (!ALLOW_CACHING)
							cached = null;
					}
					// } else {
					// decorator.refresh(cb);
					// }
				} catch (Exception e) {
					onFail(e);
				}
			}

			private void preview() {
				if (USE_TEMP_FLIP) {
					widget.flipPage().preview();
					widget.listeningOnServerCalls(true);
				}
			}

			@Override
			public void onFail(Throwable t) {
				resetLabel();
				super.onFail(t);
			}
		}, true);
		return this;
	}

	public void startLoading(boolean cached) {
		measurement = Log.instance().start(getMessage(cached));
		int width = buttonPanel.width();
		int height = buttonPanel.height();
		buttonPanel.size(width, height);
		showLoading();
		widget.loading(true);
	}

	void stopLoading(boolean cached) {
		measurement.stop();
		widget.loading(false);
	}

	String getMessage(boolean cached) {
		return (cached ? "Updating" : "Drawing") + " tab " + button.text();
	}

	private void forkLabelAsActive(boolean viaClick,
			co.fxl.gui.api.ICallback<Void> cb, boolean notify) {
		widget.active(this, viaClick, cb, notify);
		// for (INavigationListener l : listeners)
		// l.onActive(true);
	}

	private void showLabelAsActive() {
		labelAsActive = true;
		Style.instance().navigation().active(buttonPanel, button);
		clickable(false);
		button.visible(true);
		refresh.visible(false);
		widget.addSeparatorBorder();
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
		String resource = labelAsActive ? refreshActive() : refreshInactive();
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
		flipPage.flip();
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
		Display.instance().invokeLater(new Runnable() {
			@Override
			public void run() {
				clickable(true);
			}
		}, 300);
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
	public IVerticalPanel newContentPanel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void showNewContentPanel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearCache() {
		cached = null;
	}

	private void updateDisplaySize() {
		lastWidth = Shell.instance().width(itemPanel);
		lastHeight = Shell.instance().height(itemPanel);
	}

	@Override
	public void notifyFailureLoad() {
	}

}
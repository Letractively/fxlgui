/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.gwt;

import co.fxl.data.format.gwt.GWTFormat;
import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IWebsite;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.WidgetProviderNotFoundException;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.DialogImpl;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.DisplayTemplate;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.ImagePathResolver;
import co.fxl.gui.impl.RuntimeTemplate;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.log.impl.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTDisplay extends DisplayTemplate implements IDisplay,
		WidgetParent {

	// TODO Look: Quirk: GWT: IE9: Höhe von ComboBox stimmt nicht (eventuell in
	// Grid einbetten)
	// TODO Look: Quirk: GWT: Safari: Höhe von ComboBox stimmt nicht (eventuell
	// in Grid einbetten)

	private static final String FIREFOX = "Firefox/";
	private static final String CHROME = "Chrome/";
	public static final String BROWSER_WARNING_IE8 = "You are using an outdated browser with a slow javascript engine! If possible: Update to Internet Explorer 9+ or switch to another browser like Firefox or Chrome. This will significantly improve application response time.";
	public static final String BROWSER_WARNING_FIREFOX36 = "You are using an outdated browser! If possible: Update to Firefox 12+ or switch to another browser like Chrome. This will significantly improve application response time.";
	public static final String BROWSER_WARNING_ZOOM_CHROME = "You are using a zoom level <> 100%. This is currently not supported: please switch to zoom level 100%, e.g. by clicking Ctrl+0.";
	static GWTDisplay instance;
	private GWTContainer<Widget> container;
	private GWTUncaughtExceptionHandler uncaughtExceptionHandler;
	public static boolean waiting = false;
	static int lastClickX = 0;
	static int lastClickY = 0;
	private Scheduler scheduler = new SchedulerImpl();
	private IRuntime runtime;
	private boolean scrolling = true;

	public static void notifyEvent(DomEvent<?> event) {
		if (event != null) {
			final NativeEvent nativeEvent = event.getNativeEvent();
			notifyEvent(nativeEvent);
		}
	}

	public static void notifyEvent(NativeEvent nativeEvent) {
		lastClickX = nativeEvent.getClientX();
		lastClickY = nativeEvent.getClientY();
	}

	private GWTDisplay() {
		Display.instance(this);
		container = new GWTContainer<Widget>(this) {
			public void setComponent(Widget component) {
				widget = component;
				widget.setWidth("100%");
				RootPanel.get().add(component, 0, 0);
			}
		};
		// TODO AOPC: Aspect Log.instance(new GWTLog());
		ToolbarImpl.ALLOW_ALIGN_END_FOR_FLOW_PANEL = !(isChrome() && getBrowserVersion() <= 13);
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				if (!scrolling) {
					RootPanel.get().setSize(width() + "px", height() + "px");
				}
				notifyResizeListeners();
			}
		});
		runtime = new RuntimeTemplate(getBrowserName(), getBrowserVersion());
		declareConstants();
		GWTFormat.setUp();
		if (isChromeZoomActive()) {
			Log.instance().debug("Zoom is active in Google Chrome");
		}
	}

	public static boolean isChromeZoomActive() {
		return Env.is(Env.CHROME)
				&& Display.instance().width() != getOuterWidth();
	}

	private void declareConstants() {

		// TODO replace with Env.is(...) declarations in the respective widgets

		Constants
				.put("TableViewTemplate.CORRECT_HEIGHT", !isInternetExplorer());
		// if (isInternetExplorer()) {
		// Constants.put("DashboardPagePage.HEIGHT_DECREMENT", 3);
		// Constants.put("DashboardPagePage.HEIGHT_CONTENT_DECREMENT", 30);
		// }
		if (isFirefox()) {
			Constants.put("ScrollTableWidgetImpl.ADD_TOP_PANEL_TOP_PADDING",
					true);
			if (isFirefox3()) {
				Constants.put("FormWidgetImpl.FIXED_WIDTH", true);
				Constants.put("NavigationItemImpl.USE_TEMP_FLIP", false);
			}
		}
		if (isOpera())
			Constants.put("ScrollTableWidgetImpl.ADD_TOP_PANEL_SPACING", true);
		if (isFirefox() || isOpera()) {
			Constants.put("MiniFilterPanel.MODIFIED_TITLE_ADD", true);
		}
		final boolean isChrome15Plus = isChrome()
				&& GWTDisplay.getBrowserVersion() >= 15;
		final String imagePath = Constants.get("GWTLazyTreeWidget.IMAGE_PATH",
				(isChrome15Plus ? "" : GWT.getModuleBaseURL()) + "images/");
		Constants.put("ImagePathResolver", new ImagePathResolver() {
			@Override
			public String resolve(String resource) {
				if (isChrome15Plus) {
					ImageResource ir = GWTImage.resolve(resource);
					if (ir != null)
						return ir.getSafeUri().asString();
				}
				return imagePath + resource;
			}
		});
	}

	private String getBrowserName() {
		if (GWTDisplay.isChrome())
			return co.fxl.gui.impl.Env.CHROME;
		else if (GWTDisplay.isOpera())
			return co.fxl.gui.impl.Env.OPERA;
		else if (GWTDisplay.isInternetExplorer())
			return co.fxl.gui.impl.Env.IE;
		else if (GWTDisplay.isFirefox())
			return co.fxl.gui.impl.Env.FIREFOX;
		return co.fxl.gui.impl.Env.OTHER_BROWSER;
	}

	public static double getBrowserVersion() {
		if (GWTDisplay.isFirefox()) {
			return getBrowserVersionFirefox();
		}
		if (isChrome()) {
			String userAgent = getUserAgent();
			return getBrowserVersionChrome(userAgent);
		}
		if (isInternetExplorer()) {
			if (isInternetExplorer8OrBelow())
				return 8;
			else
				return 9;
		}
		return -1;
	}

	private static double getBrowserVersionFirefox() {
		String userAgent = getUserAgent();
		if (userAgent.contains(FIREFOX)) {
			int index = userAgent.indexOf(FIREFOX) + FIREFOX.length();
			int index2 = userAgent.indexOf(".", index);
			String substring = userAgent.substring(index, index2);
			return Double.valueOf(substring);
		}
		return 4;
	}

	private static int getBrowserVersionChrome(String userAgent) {
		int index = userAgent.indexOf(CHROME) + CHROME.length();
		int lastIndex = userAgent.indexOf(".", index);
		if (index == -1 || lastIndex == -1 || index >= lastIndex)
			return -1;
		String number = userAgent.substring(index, lastIndex);
		try {
			return Integer.valueOf(number);
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public IDisplay height(int pixel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IDisplay width(int pixel) {
		throw new UnsupportedOperationException();
	}

	static IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		IWidgetProvider<?> iWidgetProvider = instance.widgetProviders
				.get(interfaceClass);
		if (iWidgetProvider == null)
			throw new WidgetProviderNotFoundException(interfaceClass);
		return iWidgetProvider;
	}

	@Override
	public IDisplay title(String title) {
		Window.setTitle(title);
		return this;
	}

	@Override
	public IContainer container() {
		return container;
	}

	@Override
	public IDisplay visible(boolean visible) {
		if (container.widget != null)
			container.widget.setVisible(visible);
		return this;
	}

	@Override
	public IDisplay fullscreen() {
		return this;
	}

	public static IDisplay instance() {
		if (instance == null) {
			instance = new GWTDisplay();
		}
		return instance;
	}

	@Override
	public IDialog showDialog() {
		return new DialogImpl(this) {
			@Override
			protected void decorate(IGridPanel grid) {
				GWTGridPanel gridPanel = (GWTGridPanel) grid;
				Element element = gridPanel.container.widget.getElement();
				DOM.setStyleAttribute(element, "tableLayout", "fixed");
				element.getStyle().setOverflow(Overflow.HIDDEN);
				DOM.setStyleAttribute(element, "wordWrap", "break-word");
			}
		};
	}

	@Override
	public IWebsite showWebsite() {
		return new GWTWebsite();
	}

	@Override
	public IColor color() {
		return new GWTStyleColor(null) {
			@Override
			public void setColor(String color) {
				DOM.setStyleAttribute(RootPanel.getBodyElement(),
						"backgroundColor", color);
			}
		};
	}

	@Override
	public IDisplay addExceptionHandler(IExceptionHandler handler) {
		GWTUncaughtExceptionHandler h = setUpUncaughtExceptionHandler();
		h.add(handler);
		return this;
	}

	private GWTUncaughtExceptionHandler setUpUncaughtExceptionHandler() {
		if (uncaughtExceptionHandler == null) {
			uncaughtExceptionHandler = new GWTUncaughtExceptionHandler();
			GWT.setUncaughtExceptionHandler(uncaughtExceptionHandler);
		}
		return uncaughtExceptionHandler;
	}

	@Override
	public void add(Widget widget) {
		throw new UnsupportedOperationException();
	}

	// @Override
	// public GWTDisplay lookupDisplay() {
	// return this;
	// }

	@Override
	public void remove(Widget widget) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int height() {
		return Window.getClientHeight();
	}

	@Override
	public int width() {
		return Window.getClientWidth();
	}

	@Override
	public ICursor cursor() {
		return new ICursor() {

			@Override
			public ICursor waiting() {
				DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor",
						"wait");
				return this;
			}

			@Override
			public ICursor pointer() {
				DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor",
						"default");
				return this;
			}
		};
	}

	// public static native String getUserAgent()
	/*-{ return navigator.userAgent; }-*/;

	private static native int getOuterWidth()
	/*-{ return window.outerWidth; }-*/;

	public static String getUserAgent() {
		String userAgent = Window.Navigator.getUserAgent();
		return userAgent;
	}

	public static boolean isFirefox() {
		return !isChrome() && !isOpera() && !isInternetExplorer();
	}

	public static boolean isFirefox3() {
		return !isChrome() && !isOpera() && !isInternetExplorer()
				&& getUserAgent().contains("Firefox/3.");
	}

	public static boolean isChrome() {
		return getUserAgent().toLowerCase().contains("webkit");
	}

	public static boolean isInternetExplorer() {
		return getUserAgent().toLowerCase().contains("msie");
	}

	public static boolean isInternetExplorer8OrBelow() {
		return getUserAgent().toLowerCase().contains("msie 8.0")
				|| getUserAgent().toLowerCase().contains("msie 7.0")
				|| getUserAgent().toLowerCase().contains("msie 6.0");
	}

	public static boolean isOpera() {
		return getUserAgent().toLowerCase().contains("opera");
	}

	@Override
	public IDisplay block(boolean waiting) {
		GWTDisplay.waiting = waiting;
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor",
				waiting ? "wait" : "default");
		return this;
	}

	@Override
	public IDisplay invokeLater(final Runnable runnable) {
		scheduler.scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				runnable.run();
			}
		});
		return this;
	}

	@Override
	public IDisplay invokeLater(final Runnable runnable, int ms) {
		scheduler.scheduleFixedDelay(new RepeatingCommand() {
			@Override
			public boolean execute() {
				runnable.run();
				return false;
			}
		}, ms);
		return this;
	}

	@Override
	public IPopUp showPopUp() {
		return new GWTPopUp();
	}

	@Override
	public IDisplay size(int width, int height) {
		return width(width).height(height);
	}

	// public IDisplay runAsync(final Runnable runnable) {
	// GWT.runAsync(new RunAsyncCallback() {
	//
	// @Override
	// public void onSuccess() {
	// runnable.run();
	// }
	//
	// @Override
	// public void onFailure(Throwable reason) {
	// throw new RuntimeException(reason);
	// }
	// });
	// return this;
	// }

	@Override
	public String title() {
		return Window.getTitle();
	}

	@Override
	public IDisplay clear() {
		RootPanel.get().clear();
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void createWidget(final Class interfaceClass, final IContainer c,
			final ICallback widget) {
		ensure(interfaceClass, new CallbackTemplate<Void>(widget) {
			@Override
			public void onSuccess(Void result) {
				Object w = lookupWidgetProvider(interfaceClass).createWidget(
						container);
				widget.onSuccess(w);
			}
		});
	}

	@Override
	public IDisplay scrolling(boolean scrolling) {
		this.scrolling = scrolling;
		// Window.enableScrolling(scrolling);
		// if (isChrome()) {
		// Window.addWindowScrollHandler(new ScrollHandler() {
		//
		// private Long timeToRescroll = null;
		//
		// @Override
		// public void onWindowScroll(ScrollEvent arg0) {
		// boolean schedule = timeToRescroll == null;
		// timeToRescroll = System.currentTimeMillis() + 1000;
		// if (schedule)
		// schedule();
		// }
		//
		// private void schedule() {
		// int l = (int) Math.max(250,
		// timeToRescroll - System.currentTimeMillis());
		// invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// if (System.currentTimeMillis() < timeToRescroll) {
		// schedule();
		// return;
		// }
		// if (Window.getScrollTop() != -1) {
		// Window.scrollTo(Window.getScrollLeft(), -1);
		// }
		// timeToRescroll = null;
		// }
		// }, l);
		// }
		// });
		// }
		return this;
	}

	void resetScrollPanelTop() {
		if (isChrome() && !scrolling && Window.getScrollTop() != 0) {
			Window.scrollTo(Window.getScrollLeft(), 0);
		}
	}

	@Override
	public IRuntime runtime() {
		return runtime;
	}

	void notifyElement(GWTElement<?, ?> e) {

		// TODO ...

	}
}

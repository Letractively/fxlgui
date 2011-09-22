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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IWebsite;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.WidgetProviderNotFoundException;
import co.fxl.gui.impl.ContextMenu;
import co.fxl.gui.impl.DialogImpl;
import co.fxl.gui.impl.DiscardChangesDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTDisplay implements IDisplay, WidgetParent {

	// TODO 2DECIDE: Feature: Usability: GWT/IE: marking of text has been
	// deactivated

	public interface BlockListener {

		void onBlock(boolean block);
	}

	private static GWTDisplay instance;
	private Map<Class<?>, IWidgetProvider<?>> widgetProviders = new HashMap<Class<?>, IWidgetProvider<?>>();
	private List<BlockListener> blockListeners = new LinkedList<BlockListener>();
	private GWTContainer<Widget> container;
	private GWTUncaughtExceptionHandler uncaughtExceptionHandler;
	public static boolean waiting = false;
	private Map<IResizeListener, HandlerRegistration> resizeListeners = new HashMap<IResizeListener, HandlerRegistration>();
	static int lastClickX = 0;
	static int lastClickY = 0;

	public static void notifyEvent(DomEvent<?> event) {
		final NativeEvent nativeEvent = event.getNativeEvent();
		notifyEvent(nativeEvent);
	}

	public static void notifyEvent(NativeEvent nativeEvent) {
		lastClickX = nativeEvent.getClientX();
		lastClickY = nativeEvent.getClientY();
	}

	private GWTDisplay() {
		container = new GWTContainer<Widget>(this) {

			public void setComponent(Widget component) {
				widget = component;
				widget.setWidth("100%");
				RootPanel.get().add(component, 0, 0);
			}
		};
		DiscardChangesDialog.display = this;
		ContextMenu.instance(this);
	}

	@Override
	public IDisplay register(IWidgetProvider<?>... widgetProviders) {
		for (IWidgetProvider<?> widgetProvider : widgetProviders)
			this.widgetProviders.put(widgetProvider.widgetType(),
					widgetProvider);
		return this;
	}

	@Override
	public IDisplay height(int pixel) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay width(int pixel) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
		IWidgetProvider<?> iWidgetProvider = widgetProviders
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
		return new DialogImpl(this);
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
		throw new MethodNotImplementedException();
	}

	@Override
	public GWTDisplay lookupDisplay() {
		return this;
	}

	@Override
	public void remove(Widget widget) {
		throw new MethodNotImplementedException();
	}

	// private boolean resizing = false;
	// private Runnable r = null;

	private void resize(final IResizeListener listener) {
		// if (resizing) {
		// if (r == null) {
		// r = new Runnable() {
		// @Override
		// public void run() {
		// r = null;
		// resize(listener);
		// }
		// };
		// invokeLater(r);
		// }
		// return;
		// }
		// resizing = true;
		boolean active = listener.onResize(width(), height());
		if (!active)
			removeResizeListener(listener);
		// resizing = false;
	}

	@Override
	public IDisplay addResizeListener(final IResizeListener listener) {
		HandlerRegistration reg = Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				resize(listener);
			}
		});
		resizeListeners.put(listener, reg);
		return this;
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

	public static native String getUserAgent() /*-{
												return navigator.userAgent;
												}-*/;

	public static boolean isFirefox() {
		return !isChrome() && !isOpera() && !isInternetExplorer();
	}

	public static boolean isChrome() {
		return getUserAgent().toLowerCase().contains("webkit");
	}

	public static boolean isInternetExplorer() {
		return getUserAgent().toLowerCase().contains("msie");
	}

	public static boolean isOpera() {
		return getUserAgent().toLowerCase().contains("opera");
	}

	@Override
	public IDisplay block(boolean waiting) {
		GWTDisplay.waiting = waiting;
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor",
				waiting ? "wait" : "default");
		for (BlockListener l : blockListeners)
			l.onBlock(waiting);
		return this;
	}

	public GWTDisplay addBlockListener(BlockListener l) {
		blockListeners.add(l);
		return this;
	}

	@Override
	public IDisplay invokeLater(final Runnable runnable) {
		Scheduler s = new SchedulerImpl();
		s.scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				runnable.run();
			}
		});
		return this;
	}

	@Override
	public boolean supports(Class<?> widgetClass) {
		return widgetProviders.containsKey(widgetClass);
	}

	@Override
	public IPopUp showPopUp() {
		return new GWTPopUp(this);
	}

	@Override
	public IDisplay removeResizeListener(IResizeListener listener) {
		HandlerRegistration r = resizeListeners.get(listener);
		resizeListeners.remove(listener);
		r.removeHandler();
		return this;
	}

	@Override
	public IDisplay size(int width, int height) {
		return width(width).height(height);
	}

	@Override
	public IDisplay runAsync(final Runnable runnable) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				runnable.run();
			}

			@Override
			public void onFailure(Throwable reason) {
				throw new RuntimeException(reason);
			}
		});
		return this;
	}
}

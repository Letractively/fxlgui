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
import java.util.Map;

import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IWebsite;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.WidgetProviderNotFoundException;
import co.fxl.gui.api.IColored.IColor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class GWTDisplay implements IDisplay {

	private static GWTDisplay instance;
	private Map<Class<?>, IWidgetProvider<?>> widgetProviders = new HashMap<Class<?>, IWidgetProvider<?>>();
	private GWTContainer<Panel> container;
	private ILayout layout;

	private GWTDisplay() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler());
		container = new GWTContainer<Panel>(null) {

			@Override
			GWTDisplay lookupDisplay() {
				return GWTDisplay.this;
			}

			@Override
			IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
				return GWTDisplay.this.widgetProviders.get(interfaceClass);
			}

			void setComponent(Panel component) {
				widget = component;
				widget.setWidth("100%");
				RootPanel.get().add(component);
			}
		};
		layout = container.panel();
	}

	@Override
	public IDisplay register(IWidgetProvider<?>... widgetProviders) {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler());
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

	IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass) {
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
	public ILayout layout() {
		return layout;
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
		return new GWTDialog();
	}

	@Override
	public IWebsite showWebsite() {
		return new GWTWebsite();
	}

	@Override
	public IColor color() {
		return new GWTStyleColor(null) {
			@Override
			void setColor(String color) {
				DOM.setStyleAttribute(RootPanel.getBodyElement(),
						"backgroundColor", color);
			}
		};
	}

	@Override
	public IDisplay addExceptionHandler(IExceptionHandler handler) {
		throw new MethodNotImplementedException();
	}
}

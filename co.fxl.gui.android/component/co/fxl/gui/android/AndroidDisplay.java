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
package co.fxl.gui.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ICursor;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IWebsite;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.WidgetProviderNotFoundException;

public class AndroidDisplay implements IDisplay, Parent {

	Activity activity;
	private AndroidContainer container = new AndroidContainer(this);
	private Map<Class<?>, IWidgetProvider<?>> widgetProviders = new HashMap<Class<?>, IWidgetProvider<?>>();

	private AndroidDisplay(Activity activity) {
		this.activity = activity;
	}

	@Override
	public IDisplay addExceptionHandler(IExceptionHandler handler) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay addResizeListener(IResizeListener listener) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColor color() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IContainer container() {
		return container;
	}

	@Override
	public IDisplay fullscreen() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay height(int height) {
		throw new MethodNotImplementedException();
	}

	@Override
	public int height() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay register(IWidgetProvider<?>... widgetProviders) {
		for (IWidgetProvider<?> widgetProvider : widgetProviders)
			this.widgetProviders.put(widgetProvider.widgetType(),
					widgetProvider);
		return this;
	}

	@Override
	public IDialog showDialog() {
		return new AndroidDialog(this);
	}

	@Override
	public IWebsite showWebsite() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay title(String title) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay visible(boolean visible) {
		if (!visible)
			throw new MethodNotImplementedException();
		return this;
	}

	@Override
	public IDisplay width(int width) {
		throw new MethodNotImplementedException();
	}

	@Override
	public int width() {
		throw new MethodNotImplementedException();
	}

	@Override
	public AndroidDisplay androidDisplay() {
		return this;
	}

	@Override
	public void add(View view) {
		activity.setContentView(view);
	}

	public static IDisplay instance(Activity activity) {
		return new AndroidDisplay(activity);
	}

	@Override
	public View element() {
		throw new MethodNotImplementedException();
	}

	IWidgetProvider<?> widgetProvider(Class<?> interfaceClass) {
		IWidgetProvider<?> iWidgetProvider = widgetProviders
				.get(interfaceClass);
		if (iWidgetProvider == null)
			throw new WidgetProviderNotFoundException(interfaceClass);
		return iWidgetProvider;
	}

	public Activity activity() {
		return activity;
	}

	@Override
	public void remove(View view) {
		throw new MethodNotImplementedException();
	}

	@Override
	public boolean supports(Class<?> widgetClass) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IPopUp showPopUp() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay removeResizeListener(IResizeListener listener) {
		throw new MethodNotImplementedException();
	}

	@Override
	public ICursor cursor() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay block(boolean waiting) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay invokeLater(Runnable runnable) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay size(int width, int height) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IDisplay runAsync(Runnable runnable) {
		runnable.run();
		return this;
	}

	@Override
	public String title() {
		throw new MethodNotImplementedException();
	}
}

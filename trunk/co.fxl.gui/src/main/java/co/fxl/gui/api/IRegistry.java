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
package co.fxl.gui.api;

public interface IRegistry<R> {

	public interface IAsyncServiceProvider<T> {

		Class<T> serviceType();

		void loadAsync(ICallback<IServiceProvider<T>> callback);
	}

	public interface IServiceProvider<T> {

		Class<T> serviceType();

		T getService();
	}

	R register(IPanelProvider<?>... layoutProvider);

	R register(IWidgetProvider<?>... widgetProvider);

	R register(IWidgetProvider.IAsyncWidgetProvider<?>... widgetProvider);

	R register(IAsyncServiceProvider<?>... serviceProvider);

	<N> R registerService(Class<N> clazz, N service);

	R register(IServiceProvider<?>... service);

	boolean supports(Class<?> widgetClass);

	R ensure(ICallback<Void> callback, Class<?>... widgetClass);

	<T> T service(Class<T> clazz);

}

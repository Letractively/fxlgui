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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IPanelProvider;
import co.fxl.gui.api.IServiceRegistry;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.IWidgetProvider.IAsyncWidgetProvider;
import co.fxl.gui.log.api.ILog.IMeasurement;
import co.fxl.gui.log.impl.Log;

public class ServiceRegistryImpl<T> implements IServiceRegistry<T> {

	private static int counter = 1;

	private abstract class LoadAsyncCallbackTemplate<R> extends
			CallbackTemplate<R> {

		private int index = counter++;
		private IMeasurement measurement;

		private LoadAsyncCallbackTemplate(ICallback<Void> callback) {
			super(callback);
			measurement = Log.instance().start(message());
			ServerListener.notifyCall(-index);
		}

		private String message() {
			return "Loading code asynchronously [" + index + "]";
		}

		@Override
		public void onSuccess(R result) {
			close();
			registerResult(result);
			encapsulatedCallback.onSuccess(null);
		}

		abstract void registerResult(R result);

		@Override
		public void onFail(Throwable throwable) {
			close();
			super.onFail(throwable);
		}

		private void close() {
			ServerListener.notifyReturn(-index);
			measurement.stop();
		}
	}

	public Map<Class<?>, IWidgetProvider<?>> widgetProviders = new HashMap<Class<?>, IWidgetProvider<?>>();
	public Map<Class<?>, IAsyncWidgetProvider<?>> asyncWidgetProviders = new HashMap<Class<?>, IAsyncWidgetProvider<?>>();
	public Map<Class<?>, IAsyncServiceProvider<?>> asyncServices = new HashMap<Class<?>, IAsyncServiceProvider<?>>();
	public Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();
	public Map<Class<?>, IPanelProvider<?>> panelProviders = new HashMap<Class<?>, IPanelProvider<?>>();
	private Map<Class<?>, ICallback<Void>> callbackQueue = new HashMap<Class<?>, ICallback<Void>>();

	@Override
	public final boolean supports(Class<?> widgetClass) {
		return widgetProviders.containsKey(widgetClass)
				|| asyncWidgetProviders.containsKey(widgetClass)
				|| services.containsKey(widgetClass)
				|| asyncServices.containsKey(widgetClass);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final T ensure(final Class<?> interfaceClass,
			final ICallback<Void> callback) {
		if (widgetProviders.containsKey(interfaceClass)
				|| services.containsKey(interfaceClass)) {
			callback.onSuccess(null);
			return (T) this;
		} else if (asyncWidgetProviders.containsKey(interfaceClass)) {
			IAsyncWidgetProvider wp = asyncWidgetProviders
					.remove(interfaceClass);
			wp.loadAsync(new LoadAsyncCallbackTemplate<IWidgetProvider>(
					callback) {
				@Override
				void registerResult(IWidgetProvider result) {
					register(result);
				}
			});
			return (T) this;
		} else if (asyncServices.containsKey(interfaceClass)) {
			IAsyncServiceProvider wp = asyncServices.remove(interfaceClass);
			wp.loadAsync(new LoadAsyncCallbackTemplate<IServiceProvider>(
					callback) {
				@Override
				void registerResult(IServiceProvider result) {
					register(result);
				}
			});
			return (T) this;
		} else {
			assert !callbackQueue.containsKey(interfaceClass);
			callbackQueue.put(interfaceClass, callback);
			return (T) this;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T ensure(ICallback<Void> callback, Class<?>... widgetClass) {
		List<Class<?>> classes = new LinkedList<Class<?>>(
				Arrays.asList(widgetClass));
		ensure(callback, classes);
		return (T) this;
	}

	private void ensure(final ICallback<Void> callback,
			final List<Class<?>> classes) {
		if (classes.isEmpty())
			callback.onSuccess(null);
		else if (classes.size() == 1)
			ensure(classes.get(0), callback);
		else {
			Class<?> clazz = classes.remove(0);
			ensure(clazz, new CallbackTemplate<Void>(callback) {

				@Override
				public void onSuccess(Void result) {
					ensure(callback, classes);
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T register(
			@SuppressWarnings("rawtypes") IAsyncServiceProvider... serviceProviders) {
		for (IAsyncServiceProvider<?> serviceProvider : serviceProviders) {
			asyncServices.put(serviceProvider.serviceType(), serviceProvider);
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <R> R service(Class<R> clazz) {
		R r = (R) services.get(clazz);
		if (r == null)
			throw new RuntimeException("Service not yet loaded: "
					+ clazz.getName());
		return r;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T register(
			@SuppressWarnings("rawtypes") co.fxl.gui.api.IServiceRegistry.IServiceProvider... services) {
		for (IServiceProvider<?> service : services) {
			Class<?> type = service.serviceType();
			this.services.put(type, service.getService());
			handlePendingCallbacks(type);
		}
		return (T) this;
	}

	private void handlePendingCallbacks(Class<?> type) {
		final ICallback<Void> cb = callbackQueue.remove(type);
		if (cb != null) {
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					cb.onSuccess(null);
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T register(IWidgetProvider<?>... widgetProviders) {
		for (IWidgetProvider<?> widgetProvider : widgetProviders) {
			Class<?> type = widgetProvider.widgetType();
			this.widgetProviders.put(type, widgetProvider);
			handlePendingCallbacks(type);
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T register(
			@SuppressWarnings("rawtypes") IAsyncWidgetProvider... widgetProviders) {
		for (IAsyncWidgetProvider<?> widgetProvider : widgetProviders)
			this.asyncWidgetProviders.put(widgetProvider.widgetType(),
					widgetProvider);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T register(IPanelProvider<?>... panelProviders) {
		for (IPanelProvider<?> panelProvider : panelProviders)
			this.panelProviders.put(panelProvider.panelType(), panelProvider);
		return (T) this;
	}

	@Override
	public final <N> T registerService(final Class<N> clazz, final N service) {
		return register(new IServiceProvider<N>() {

			@Override
			public Class<N> serviceType() {
				return clazz;
			}

			@Override
			public N getService() {
				return service;
			}
		});
	}

}

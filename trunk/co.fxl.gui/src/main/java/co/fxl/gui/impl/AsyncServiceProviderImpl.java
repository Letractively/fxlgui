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
package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IRegistry.IAsyncServiceProvider;
import co.fxl.gui.api.IRegistry.IServiceProvider;

public abstract class AsyncServiceProviderImpl<T> implements
		IAsyncServiceProvider<T> {

	protected Class<T> clazz;
	private String id = "services";

	public AsyncServiceProviderImpl(Class<T> clazz) {
		this.clazz = clazz;
		// String simpleName = clazz.getName().substring(
		// clazz.getName().lastIndexOf(".") + 1);
		// StringBuilder b = new StringBuilder(simpleName.charAt(0));
		// for (int i = 1; i < simpleName.length(); i++) {
		// if (Character.isUpperCase(simpleName.charAt(i))) {
		// b.append(" ");
		// }
		// b.append(simpleName.charAt(i));
		// }
		// id = b.toString().toLowerCase();
	}

	@Override
	public Class<T> serviceType() {
		return clazz;
	}

	@Override
	public void loadAsync(final ICallback<IServiceProvider<T>> callback) {
		final StatusPopUp p = new StatusPopUp().start(id);
		loadAsyncImpl(new CallbackTemplate<IServiceProvider<T>>(callback) {
			@Override
			public void onSuccess(IServiceProvider<T> result) {
				p.stop();
				callback.onSuccess(result);
			}

			@Override
			public void onFail(Throwable throwable) {
				p.stop();
				super.onFail(throwable);
			}
		});

	}

	protected abstract void loadAsyncImpl(
			ICallback<IServiceProvider<T>> callback);

}

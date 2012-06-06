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
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.api.IWidgetProvider.IAsyncWidgetProvider;

public abstract class AsyncWidgetProviderImpl<T> implements
		IAsyncWidgetProvider<T> {

	protected Class<T> clazz;

	public AsyncWidgetProviderImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Class<T> widgetType() {
		return clazz;
	}

	@Override
	public void loadAsync(final ICallback<IWidgetProvider<T>> callback) {
		final StatusPanel p = StatusPanel.newInstance().start("widgets");
		loadAsyncImpl(new CallbackTemplate<IWidgetProvider<T>>(callback) {
			@Override
			public void onSuccess(IWidgetProvider<T> result) {
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

	protected abstract void loadAsyncImpl(ICallback<IWidgetProvider<T>> callback);

}

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
import co.fxl.gui.log.api.ILog.IMeasurement;
import co.fxl.gui.log.impl.Log;

public class ProfilingCallback<T> extends CallbackTemplate<T> implements
		RuntimeConstants {

	private static final boolean ACTIVE = true;
	private IMeasurement measurement;

	private ProfilingCallback(ICallback<T> callback, String id) {
		super(callback);
		String signature = id != null ? id : new Exception().getStackTrace()[2]
				.toString();
		measurement = Log.instance().start(signature);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(T result) {
		measurement.stop();
		((ICallback<T>) encapsulatedCallback).onSuccess(result);
	}

	@Override
	public void onFail(Throwable throwable) {
		measurement.stop();
		super.onFail(throwable);
	}

	public static <R> ICallback<R> adapt(ICallback<R> callback) {
		return adapt(callback, null);
	}

	public static <R> ICallback<R> adapt(ICallback<R> callback, String id) {
		if (!ACTIVE)
			return callback;
		return new ProfilingCallback<R>(callback, id);
	}
}

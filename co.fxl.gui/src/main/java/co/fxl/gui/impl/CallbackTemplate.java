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

import co.fxl.gui.api.ICallback;

public abstract class CallbackTemplate<T> implements ICallback<T> {

	protected ICallback<?> encapsulatedCallback;

	public CallbackTemplate() {
	}

	public CallbackTemplate(ICallback<?> cb) {
		this.encapsulatedCallback = cb;
	}

	@Override
	public void onFail(Throwable throwable) {
		if (encapsulatedCallback == null) {
			// throwable.printStackTrace();
			rethrow(throwable);
		} else
			encapsulatedCallback.onFail(throwable);
	}

	public static ICallback<Boolean> adapterVoid(final ICallback<Void> cb2) {
		return new CallbackTemplate<Boolean>(cb2) {
			@Override
			public void onSuccess(Boolean result) {
				if (cb2 != null)
					cb2.onSuccess(null);
			}
		};
	}

	public static ICallback<Void> adapterBoolean(final ICallback<Boolean> cb2) {
		return new CallbackTemplate<Void>(cb2) {
			@Override
			public void onSuccess(Void result) {
				cb2.onSuccess(false);
			}
		};
	}

	public static ICallback<Void> adapterBooleanTrue(
			final ICallback<Boolean> cb2) {
		return new CallbackTemplate<Void>(cb2) {
			@Override
			public void onSuccess(Void result) {
				cb2.onSuccess(true);
			}
		};
	}

	public static void rethrow(Throwable pThrowable) {
		if (pThrowable instanceof RuntimeException) {
			throw (RuntimeException) pThrowable;
		} else if (pThrowable instanceof Error) {
			throw (Error) pThrowable;
		} else {
			throw new RuntimeException(pThrowable);
		}
	}
}

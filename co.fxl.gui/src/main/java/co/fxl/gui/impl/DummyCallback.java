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

public class DummyCallback<T> implements ICallback<T> {

	private static final DummyCallback<Boolean> DUMMY_CALLBACK_BOOLEAN = new DummyCallback<Boolean>();
	private static final DummyCallback<Void> DUMMY_CALLBACK_VOID = new DummyCallback<Void>();

	@Override
	public void onSuccess(T result) {
	}

	@Override
	public void onFail(Throwable throwable) {
		CallbackTemplate.rethrow(throwable);
	}

	public static ICallback<Void> voidInstance() {
		return DUMMY_CALLBACK_VOID;
	}

	public static ICallback<Boolean> booleanInstance() {
		return DUMMY_CALLBACK_BOOLEAN;
	}

}

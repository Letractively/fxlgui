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
import co.fxl.gui.impl.StatusPopUp.Status;

public abstract class LongRunningCallback<T> extends CallbackTemplate<T> {

	private Status dialog;

	public LongRunningCallback(String info) {
		set(info);
	}

	public LongRunningCallback(String info, ICallback<?> cb) {
		super(cb);
		set(info);
	}

	private void set(String info) {
		dialog = StatusPopUp.instance().show("Please wait - " + info + "...",
				null, false);
	}

	@Override
	public final void onSuccess(T pResult) {
		Display.instance().invokeLater(new Runnable() {
			@Override
			public void run() {
				dialog.hide(false);
			}
		});
		onInternalSuccess(pResult);
	}

	protected abstract void onInternalSuccess(T pResult);

	@Override
	public void onFail(Throwable throwable) {
		dialog.hide(false);
		super.onFail(throwable);
	}
}

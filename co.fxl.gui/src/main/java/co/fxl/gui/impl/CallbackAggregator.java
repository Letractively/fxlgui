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

public class CallbackAggregator<T> {

	private ICallback<T> cb;
	private int counter = 0;
	private boolean fixedNumber;

	public CallbackAggregator() {
		this(null, 0);
	}

	public CallbackAggregator(ICallback<T> cb) {
		this(cb, 0);
	}

	public CallbackAggregator(ICallback<T> cb, int counter) {
		this.cb = cb;
		this.counter = counter;
		fixedNumber = counter > 0;
	}

	public CallbackAggregator(int counter) {
		this(null, counter);
	}

	public ICallback<T> derive() {
		if (!fixedNumber)
			counter++;
		return new CallbackTemplate<T>(cb) {
			@Override
			public void onSuccess(T result) {
				counter--;
				if (counter == 0 && cb != null) {
					cb.onSuccess(result);
				}
			}
		};
	}

}

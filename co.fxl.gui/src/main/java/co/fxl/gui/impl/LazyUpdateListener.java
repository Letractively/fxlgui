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

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

public abstract class LazyUpdateListener<T> extends CallbackTemplate<Boolean>
		implements IUpdateListener<T> {

	private T value;
	private IComboBox cb;
	private boolean active = true;
	private T allowedValue;

	public LazyUpdateListener() {
	}

	public LazyUpdateListener(MapComboBox<?> cb) {
		this(cb.comboBox());
	}

	@SuppressWarnings("unchecked")
	public LazyUpdateListener(IComboBox cb) {
		this.cb = cb;
		if (cb != null)
			allowedValue = (T) cb.text();
	}

	@Override
	public void onUpdate(T value) {
		if (!active)
			return;
		this.value = value;
		DiscardChangesDialog.show(this);
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			allowedValue = value;
			onAllowedUpdate(value);
		} else {
			onCancelledUpdate(value);
			if (cb != null) {
				active = false;
				cb.text((String) allowedValue);
				active = true;
			}
		}
	}

	protected abstract void onAllowedUpdate(T value);

	protected void onCancelledUpdate(T value) {
	}
}

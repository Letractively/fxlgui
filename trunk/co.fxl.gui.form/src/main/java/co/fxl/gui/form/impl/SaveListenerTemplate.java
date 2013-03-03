/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.form.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.form.api.IFormWidget.ISaveListener;

public abstract class SaveListenerTemplate implements ISaveListener {

	@Override
	public void save(boolean isAndBack, ICallback<Boolean> cb) {
		save(cb);
	}

	protected void save(ICallback<Boolean> cb) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean allowsSaveAndBack() {
		return false;
	}

	@Override
	public boolean allowsCancel() {
		return false;
	}

	@Override
	public void cancel(ICallback<Boolean> cb) {
		throw new UnsupportedOperationException();
	}
}

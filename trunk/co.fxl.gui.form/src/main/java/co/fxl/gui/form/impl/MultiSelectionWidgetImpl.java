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

import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.form.api.IMultiSelectionWidget;

class MultiSelectionWidgetImpl implements IMultiSelectionWidget<Object> {

	MultiSelectionWidgetImpl(IContainer container) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IUpdateable<List<Object>> addUpdateListener(
			IUpdateListener<List<Object>> listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMultiSelectionWidget<Object> adapter(
			IMultiSelectionAdapter<Object> adapter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String text() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void text(String text) {
		throw new UnsupportedOperationException();
	}

}

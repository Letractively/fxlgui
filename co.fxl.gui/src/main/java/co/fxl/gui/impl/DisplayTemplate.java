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

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.log.impl.LogWidgetProvider;

public abstract class DisplayTemplate extends ServiceRegistryImpl<IDisplay>
		implements IDisplay {

	private ResizeRegistryImpl<IDisplay> resizeRegistry = new ResizeRegistryImpl<IDisplay>(
			this);

	protected DisplayTemplate() {
		register(new LogWidgetProvider());
	}

	@Override
	public final IResizeConfiguration addResizeListener(
			final IResizeListener listener) {
		return resizeRegistry.addResizeListener(listener);
	}

	@Override
	public final IDisplay removeResizeListener(IResizeListener listener) {
		return resizeRegistry.removeResizeListener(listener);
	}

	@Override
	public IDisplay notifyResizeListeners() {
		return resizeRegistry.notifyResizeListeners();
	}

	@Override
	public final IDisplay notifyResizeListener(IResizeListener listener) {
		return resizeRegistry.notifyResizeListener(listener);
	}
}

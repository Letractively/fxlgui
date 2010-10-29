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
package co.fxl.gui.api;

import co.fxl.gui.api.IColored.IColor;

public interface IDisplay {

	public interface IResizeListener {

		void onResize(int width, int height);
	}

	public interface IExceptionHandler {

		void onException(RuntimeException exception);
	}

	IDisplay title(String title);

	IDisplay width(int width);

	IDisplay height(int height);

	IDisplay visible(boolean visible);

	IContainer container();

	IDisplay fullscreen();

	IDisplay register(IWidgetProvider<?>... widgetProvider);

	IDialog showDialog();

	IWebsite showWebsite();

	IColor color();

	IDisplay addExceptionHandler(IExceptionHandler handler);

	IDisplay addResizeListener(IResizeListener listener);

	int width();

	int height();
}

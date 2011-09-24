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

public interface IDisplay extends IColored{

	public interface IResizeListener {

		boolean onResize(int width, int height);
	}

	public interface IExceptionHandler {

		void onException(Throwable exception);
	}

	IDisplay title(String title);

	IDisplay width(int width);

	IDisplay height(int height);

	IDisplay size(int width, int height);

	IDisplay visible(boolean visible);

	IContainer container();

	IDisplay fullscreen();

	boolean supports(Class<?> widgetClass);

	IDisplay register(IWidgetProvider<?>... widgetProvider);

	IDialog showDialog();

	IWebsite showWebsite();

	IPopUp showPopUp();

	IDisplay addExceptionHandler(IExceptionHandler handler);

	IDisplay addResizeListener(IResizeListener listener);

	IDisplay removeResizeListener(IResizeListener listener);

	int width();

	int height();

	ICursor cursor();

	IDisplay block(boolean waiting);

	IDisplay invokeLater(Runnable runnable);

	// TODO eventually extract to ICodeSplit or something similar
	IDisplay runAsync(Runnable runnable);

	String title();
}

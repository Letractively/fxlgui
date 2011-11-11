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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay.IExceptionHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;

class GWTUncaughtExceptionHandler implements GWT.UncaughtExceptionHandler {

	private List<IExceptionHandler> handlers = new LinkedList<IExceptionHandler>();
	public static final String RESIZE_BROWSER_IE8 = "-2147024809";
	public static final String DETAIL_VIEW_IE9 = "-2147467259";

	void add(IExceptionHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void onUncaughtException(Throwable e) {
		if (isIgnore(e)) {
			return;
		}
		for (IExceptionHandler h : handlers) {
			h.onException(e);
		}
	}

	private boolean isIgnore(Throwable e) {
		if (!GWTDisplay.isInternetExplorer())
			return false;
		if (GWTDisplay.isInternetExplorer8() && e instanceof UmbrellaException) {
			UmbrellaException ue = (UmbrellaException) e;
			boolean isIgnore = true;
			for (Throwable cause : ue.getCauses()) {
				isIgnore &= isIgnore(cause);
			}
			return isIgnore;
		}

		// TODO remove temporary IE hack !!!!

		String message = e.getMessage();
		if (message != null && message.startsWith("(Error): ")) {
			// show detail view in IE9 (administration)
			if (message.contains("number: " + DETAIL_VIEW_IE9)) {
				GWTDisplay.instance().title(DETAIL_VIEW_IE9);
				return true;
			}
			// resize browser in IE8
			if (message.contains("number: " + RESIZE_BROWSER_IE8)
					&& GWTDisplay.isInternetExplorer8())
				GWTDisplay.instance().title(RESIZE_BROWSER_IE8);
			return true;
		}
		return false;
	}
}
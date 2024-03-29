/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.gui.api.ICursor;
import co.fxl.gui.impl.RuntimeConstants;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

class GWTCursor<R> implements ICursor<R>, RuntimeConstants {

	private R owner;
	private Widget widget;

	GWTCursor(R owner, Widget widget) {
		this.owner = owner;
		this.widget = widget;
	}

	@Override
	public R waiting() {
		return symbol("wait");
	}

	private R symbol(String symbol) {
		DOM.setStyleAttribute(widget.getElement(), "cursor", symbol);
		return owner;

	}

	@Override
	public R hand() {
		return symbol(FIREFOX ? "pointer" : "hand");
	}

	@Override
	public R pointer() {
		return symbol(FIREFOX ? "default" : "pointer");
	}
}
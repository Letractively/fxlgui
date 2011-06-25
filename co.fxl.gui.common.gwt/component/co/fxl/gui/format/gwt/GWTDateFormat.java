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
package co.fxl.gui.format.gwt;

import java.util.Date;

import co.fxl.data.format.api.IFormat;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

class GWTDateFormat implements IFormat<Date> {

	private DateTimeFormat impl;

	GWTDateFormat(PredefinedFormat predef) {
		try {
			this.impl = DateTimeFormat.getFormat(predef);
		} catch (Throwable lThrowable) {
			lThrowable.printStackTrace();
		}
	}

	@Override
	public String format(Date object) {
		if (object == null)
			return "";
		return impl.format(object);
	}
	
	@Override
	public String format(Date object, String pFormatStyle) {
		if (object == null) {
			return "";
		}
		DateTimeFormat lDateTimeFormat = DateTimeFormat.getFormat(pFormatStyle);
		return lDateTimeFormat.format(object);
	}

	@Override
	public Date parse(String format) {
		if (format == null)
			return null;
		try {
			return impl.parse(format);
		} catch (Exception e) {
			return null;
		}
	}
	
}

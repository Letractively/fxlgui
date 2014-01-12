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
package co.fxl.data.format.swing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.fxl.data.format.api.IFormat;

class DateFormatImpl implements IFormat<Date> {

	private String defaultFormatStyle = null;
	private java.util.TimeZone timeZone = java.util.TimeZone.getDefault();
	private DateFormat impl;

	DateFormatImpl(DateFormat impl) {
		this.impl = impl;
	}

	@Override
	public IFormat<Date> defaultFormatStyle(String pDefaultFormatStyle) {
		defaultFormatStyle = pDefaultFormatStyle;
		return this;
	}

	@Override
	public String format(Date object) {
		if (object == null)
			return "";
		if (defaultFormatStyle == null) {
			return impl.format(object);
		} else {
			return format(object, defaultFormatStyle);
		}
	}

	@Override
	public String format(Date object, String pFormat) {
		SimpleDateFormat lFormat = new SimpleDateFormat(pFormat);
		return lFormat.format(object);
	}

	@Override
	public Date parse(String format) {
		if (defaultFormatStyle == null) {
			try {
				return impl.parse(format);
			} catch (Exception e) {
				return null;
			}
		} else {
			return parse(format, defaultFormatStyle);
		}
	}

	@Override
	public Date parse(String format, String style) {
		try {
			SimpleDateFormat s = new SimpleDateFormat(style);
			return s.parse(format);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public IFormat<Date> timeZone(TimeZone pTimeZone) {
		return this;
	}

	// @Override
	// public IFormat<Date> setLocale(String locale) {
	// return this;
	// }
}
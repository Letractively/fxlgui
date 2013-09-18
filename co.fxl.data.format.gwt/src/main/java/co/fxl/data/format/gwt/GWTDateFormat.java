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
package co.fxl.data.format.gwt;

import java.util.Date;

import co.fxl.data.format.api.IFormat;

import com.google.gwt.i18n.client.DateTimeFormat;

class GWTDateFormat implements IFormat<Date> {

	private com.google.gwt.i18n.client.TimeZone timeZone;
	private DateTimeFormat impl;
	static DateTimeFormat TIME_PATTERN = DateTimeFormat.getFormat("hh:mm:ss aaa");
	static DateTimeFormat DATE_PATTERN = DateTimeFormat.getFormat("yyyy-MM-dd");
	static DateTimeFormat DATE_TIME_PATTERN = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss aaa");

	GWTDateFormat(DateTimeFormat predef) {
		impl = predef;
		timeZone = com.google.gwt.i18n.client.TimeZone.createTimeZone(0);
	}

	@Override
	public Date parse(String object, String pFormatStyle) {
		if (object == null) {
			return null;
		}
		DateTimeFormat lDateTimeFormat = DateTimeFormat.getFormat(pFormatStyle);
		return lDateTimeFormat.parse(object);
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
	
	@Override
	public String format(Date object) {
		if (object == null)
			return "";
		return impl.format(object, timeZone);		
	}

	@Override
	public String format(Date object, String pFormatStyle) {
		if (object == null) {
			return "";
		}
		DateTimeFormat lDateTimeFormat = DateTimeFormat.getFormat(pFormatStyle);
		return lDateTimeFormat.format(object, timeZone);
	}
	
	@Override
	public IFormat<Date> defaultFormatStyle(String pDefaultFormatStyle) {
		impl = DateTimeFormat.getFormat(pDefaultFormatStyle); 
		return this;
	}
	
	@Override
	public IFormat<Date> timeZone(TimeZone pTimeZone) {
		int lOffset = pTimeZone.getOffsetToUtcInMinutes();
		// @see http://stackoverflow.com/questions/5068701/gwt-datetimeformat-reverses-timezone-value
		timeZone = com.google.gwt.i18n.client.TimeZone.createTimeZone(-1*lOffset);		
		return this;
	}

}

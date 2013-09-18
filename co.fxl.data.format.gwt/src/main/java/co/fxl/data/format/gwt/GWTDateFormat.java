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
	private int timeZoneOffset;
	private DateTimeFormat printFormat;
	private DateTimeFormat parseFormat;
	static DateTimeFormat TIME_PATTERN = DateTimeFormat.getFormat("hh:mm:ss aaa");
	static DateTimeFormat DATE_PATTERN = DateTimeFormat.getFormat("yyyy-MM-dd");
	static DateTimeFormat DATE_TIME_PATTERN = DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss aaa");

	GWTDateFormat(DateTimeFormat pDateTimeFormat) {
		printFormat = pDateTimeFormat;
		parseFormat = pDateTimeFormat;
		timeZone = com.google.gwt.i18n.client.TimeZone.createTimeZone(0);
		timeZoneOffset = 0;
	}

	@Override
	public Date parse(String object, String pFormatStyle) {
		if (object == null) {
			return null;
		}
		DateTimeFormat lDateTimeFormat = DateTimeFormat.getFormat(pFormatStyle);
		return internalParse(lDateTimeFormat, pFormatStyle);
	}

	@Override
	public Date parse(String format) {
		if (format == null)
			return null;
		try {
			return internalParse(parseFormat, format);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected Date internalParse(DateTimeFormat dateTimeFormat, String pDateString) {
		// Parse Date for utc timezone
		Date date = dateTimeFormat.parse(pDateString+" UTC");
		// Convert date to timezone
		return addTimezoneOffset(date);
	}
	
	protected Date addTimezoneOffset(Date pDate) {
		if (pDate == null) {
			return null;
		} else {
			return new Date(pDate.getTime()-(timeZoneOffset*1000*60));
		}
	}
	
	@Override
	public String format(Date object) {
		if (object == null)
			return "";
		return printFormat.format(object, timeZone);		
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
		printFormat = DateTimeFormat.getFormat(pDefaultFormatStyle);
		// Important, 'v' adds timezone
		parseFormat = DateTimeFormat.getFormat(pDefaultFormatStyle+" v");
		return this;
	}
	
	@Override
	public IFormat<Date> timeZone(TimeZone pTimeZone) {
		int lOffset = pTimeZone.getOffsetToUtcInMinutes();
		// @see http://stackoverflow.com/questions/5068701/gwt-datetimeformat-reverses-timezone-value		
		timeZone = com.google.gwt.i18n.client.TimeZone.createTimeZone(-1*lOffset);		
		timeZoneOffset = lOffset;
		return this;
	}

}

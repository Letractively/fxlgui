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
import com.google.gwt.i18n.client.TimeZone;

class GWTDateFormat implements IFormat<Date> {

	private TimeZone utcTimeZone;
	private DateTimeFormat impl;
	private String defaultFormatStyle = null;
//	private String locale = Format.LOCALE_EN;
//	private DateTimeFormat predef;
	static DateTimeFormat TIME_PATTERN = DateTimeFormat
			.getFormat("hh:mm:ss aaa");
	static DateTimeFormat DATE_PATTERN = DateTimeFormat.getFormat("yyyy-MM-dd");
	static DateTimeFormat DATE_TIME_PATTERN = DateTimeFormat
			.getFormat("yyyy-MM-dd hh:mm:ss aaa");

	GWTDateFormat(DateTimeFormat predef) {
		try {
			impl = predef;
//			this.predef = predef;
			utcTimeZone = TimeZone.createTimeZone(0);
		} catch (Throwable lThrowable) {
			lThrowable.printStackTrace();
		}
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
//			if (locale.equals(Format.LOCALE_DE)) {
//				if (predef.equals(TIME_PATTERN))
//					return getTime(new Date(), format);
//				if (predef.equals(DATE_TIME_PATTERN))
//					return getTime(getDate(format), format);
//				if (predef.equals(DATE_PATTERN))
//					return getDate(format);
//			}
			if (defaultFormatStyle == null) {
				return impl.parse(format);
			} else {
				return parse(format, defaultFormatStyle);
			}
		} catch (Exception e) {
			return null;
		}
	}

	// public static void main(String[] args) {
	// System.out.println(getDate("10.02.2012"));
	// System.out.println(getTime(new Date(), "14:55:11"));
	// System.out.println(getTime(getDate("10.02.2012 14:55:11"),
	// "10.02.2012 14:55:11"));
	// }

//	static Date getDate(String format) {
//		int[] s = getPart(format, 0, "\\.");
//		assert s.length == 3;
//		return new Date(s[2] - 1900, s[1] - 1, s[0]);
//	}

	static int[] getPart(String format, int i, String split) {
		if (!format.contains(" "))
			return toIntArray(format.split(split));
		return toIntArray(format.split(" ")[i].split(split));
	}

	static int[] toIntArray(String[] split) {
		int[] a = new int[split.length];
		for (int i = 0; i < split.length; i++)
			a[i] = Integer.valueOf(split[i]);
		return a;
	}

//	static Date getTime(Date date, String format) {
//		int[] s = getPart(format, 1, ":");
//		assert s.length == 3;
//		return new Date(date.getYear(), date.getMonth(), date.getDate(), s[0],
//				s[1], s[2]);
//	}

//	private String getTimeString(Date object) {
//		return l(object.getHours(), 2) + ":" + l(object.getMinutes(), 2) + ":"
//				+ l(object.getSeconds(), 2);
//	}
//
//	private String getDateString(Date object) {
//		return l(object.getDate(), 2) + "." + l(object.getMonth() + 1, 2) + "."
//				+ l(object.getYear() + 1900, 4);
//	}

//	private String l(int date, int i) {
//		String s = String.valueOf(date);
//		while (s.length() < i)
//			s = "0" + s;
//		return s;
//	}
	
	@Override
	public String format(Date object) {
		if (object == null)
			return "";
		if (defaultFormatStyle == null) {
			return impl.format(object, utcTimeZone);	
		} else {
			return format(object, defaultFormatStyle);
		}
//		if (locale.equals(Format.LOCALE_DE)) {
//			if (predef.equals(TIME_PATTERN))
//				return getTimeString(object);
//			if (predef.equals(DATE_TIME_PATTERN))
//				return getDateString(object) + " " + getTimeString(object);
//			if (predef.equals(DATE_PATTERN))
//				return getDateString(object);
//		}
		
	}

	@Override
	public String format(Date object, String pFormatStyle) {
		if (object == null) {
			return "";
		}
		DateTimeFormat lDateTimeFormat = DateTimeFormat.getFormat(pFormatStyle);
		return lDateTimeFormat.format(object, utcTimeZone);
	}
	
	@Override
	public IFormat<Date> defaultFormatStyle(String pDefaultFormatStyle) {
		defaultFormatStyle = pDefaultFormatStyle;
		return this;
	}

//	@Override
//	public IFormat<Date> setLocale(String locale) {
//		this.locale = locale;
//		return this;
//	}
}

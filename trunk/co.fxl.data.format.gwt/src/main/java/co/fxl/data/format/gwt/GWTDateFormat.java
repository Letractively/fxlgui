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
import co.fxl.data.format.impl.Format;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

class GWTDateFormat implements IFormat<Date> {

	private DateTimeFormat impl;
	private String locale = Format.LOCALE_EN;
	private PredefinedFormat predef;

	GWTDateFormat(PredefinedFormat predef) {
		try {
			this.predef = predef;
			this.impl = DateTimeFormat.getFormat(predef);
		} catch (Throwable lThrowable) {
			lThrowable.printStackTrace();
		}
	}

	@Override
	public Date parse(String format) {
		if (format == null)
			return null;
		try {
			if (locale.equals(Format.LOCALE_DE)) {
				if (predef.equals(PredefinedFormat.TIME_LONG))
					return getTime(new Date(), format);
				if (predef.equals(PredefinedFormat.DATE_TIME_MEDIUM))
					return getTime(getDate(format), format);
				if (predef.equals(PredefinedFormat.DATE_SHORT))
					return getDate(format);
			}
			return impl.parse(format);
		} catch (Exception e) {
			return null;
		}
	}

	private Date getDate(String format) {
		int[] s = getPart(format, 0, ".");
		return new Date(s[2], s[1] - 1, s[0]);
	}

	private int[] getPart(String format, int i, String split) {
		if (!format.contains(" "))
			return toIntArray(format.split(split));
		return toIntArray(format.split(" ")[i].split(split));
	}

	private int[] toIntArray(String[] split) {
		int[] a = new int[split.length];
		for (int i = 0; i < split.length; i++)
			a[i] = Integer.valueOf(split[i]);
		return a;
	}

	private Date getTime(Date date, String format) {
		int[] s = getPart(format, 0, ".");
		return new Date(date.getYear(), date.getMonth(), date.getDate(), s[0],
				s[1], s[2]);
	}

	@Override
	public String format(Date object) {
		if (object == null)
			return "";
		if (locale.equals(Format.LOCALE_DE)) {
			if (predef.equals(PredefinedFormat.TIME_LONG))
				return getTimeString(object);
			if (predef.equals(PredefinedFormat.DATE_TIME_MEDIUM))
				return getDateString(object) + " " + getTimeString(object);
			if (predef.equals(PredefinedFormat.DATE_SHORT))
				return getDateString(object);
		}
		return impl.format(object);
	}

	private String getTimeString(Date object) {
		return l(object.getHours(), 2) + ":" + l(object.getMinutes(), 2) + ":"
				+ l(object.getSeconds(), 2);
	}

	private String getDateString(Date object) {
		return l(object.getDate(), 2) + "." + l(object.getMonth() + 1, 2) + "."
				+ l(object.getYear(), 4);
	}

	private String l(int date, int i) {
		String s = String.valueOf(date);
		while (s.length() < i)
			s = "0" + s;
		return s;
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
	public IFormat<Date> setLocale(String locale) {
		this.locale = locale;
		return this;
	}
}

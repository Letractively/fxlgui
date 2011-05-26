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
package co.fxl.gui.format.impl;

import java.util.Date;

import co.fxl.gui.format.api.IFormat;

class DateFormatImpl implements IFormat<Date> {

	int yearIncrement = 1900;
	int monthIncrement = 1;
	int dayIncrement = 1;

	public DateFormatImpl() {
	}

	public DateFormatImpl(int yearIncrement, int monthIncrement,
			int dayIncrement) {
		this.yearIncrement = yearIncrement;
		this.monthIncrement = monthIncrement;
		this.dayIncrement = dayIncrement;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Date parse(String string) {
		try {
			if (string == null || string.equals(""))
				return null;
			String[] s = string.split("\\.");
			Integer year = Integer.valueOf(s[2]);
			if (year < 0)
				return null;
			Integer month = Integer.valueOf(s[1]);
			if (month < 1 || month > 12)
				return null;
			Integer day = Integer.valueOf(s[0]);
			if (day < 1 || day > 31)
				return null;
			return new Date(year - yearIncrement, month - monthIncrement, day
					- dayIncrement);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String format(Date date) {
		if (date == null)
			return "";
		int day = date.getDate() + dayIncrement;
		int month = date.getMonth() + monthIncrement;
		int year = date.getYear() + yearIncrement;
		String string = l(day, 2) + "." + l(month, 2) + "." + l(year, 4);
		return string;
	}

	static String l(int date, int i) {
		String s = String.valueOf(date);
		while (s.length() < i)
			s = "0" + s;
		return s;
	}
}

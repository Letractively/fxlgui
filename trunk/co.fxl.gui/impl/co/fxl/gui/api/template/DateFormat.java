/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.api.template;

import java.util.Date;

public abstract class DateFormat {

	public static int yearIncrement = 1900;
	public static int monthIncrement = 1;
	public static int YEAR_INCREMENT_DEC = 0;
	public static int MONTH_INCREMENT_DEC = 0;
	public static DateFormat instance = new DateFormat() {

		@SuppressWarnings("deprecation")
		@Override
		public Date parse(String string) {
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
			return new Date(year - YEAR_INCREMENT_DEC, month
					- MONTH_INCREMENT_DEC, day);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String format(Date date) {
			if (date == null)
				return "";
			int day = date.getDate();
			int month = date.getMonth() + monthIncrement;
			int year = date.getYear() + yearIncrement;
			String string = l(day, 2) + "." + l(month, 2) + "." + l(year, 4);
			return string;
		}

		private String l(int date, int i) {
			String s = String.valueOf(date);
			while (s.length() < i)
				s = "0" + s;
			return s;
		}
	};

	public abstract Date parse(String string);

	public abstract String format(Date date);

}

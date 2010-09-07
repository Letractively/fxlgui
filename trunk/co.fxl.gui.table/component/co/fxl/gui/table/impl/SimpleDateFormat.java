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
package co.fxl.gui.table.impl;

import java.util.Date;

public class SimpleDateFormat {

	@SuppressWarnings("deprecation")
	public Date parse(String string) {
		if (string == null || string.equals(""))
			return null;
		String[] s = string.split("\\.");
		return new Date(Integer.valueOf(s[2]), Integer.valueOf(s[1]), Integer
				.valueOf(s[0]));
	}

	@SuppressWarnings("deprecation")
	public String format(Date date) {
		if (date == null)
			return "";
		return l(date.getDate(), 2) + "." + l(date.getMonth(), 2) + "."
				+ l(date.getYear(), 4);
	}

	private String l(int date, int i) {
		String s = String.valueOf(date);
		while (s.length() < i)
			s = "0" + s;
		return s;
	}
}

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

import co.fxl.gui.format.api.IFormat;

public abstract class DateTimeFormatImpl extends DateFormatImpl implements IFormat<Date> {

	public DateTimeFormatImpl() {
		super();
	}

	public DateTimeFormatImpl(int yearIncrement, int monthIncrement,
			int dayIncrement) {
		super(yearIncrement, monthIncrement, dayIncrement);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Date parse(String string) {
		try {
			if (string == null || string.equals(""))
				return null;
			String[] s = string.split(" ");
			Date date = super.parse(s[0]);
			String[] time = s[1].split(":");
			Integer hours = Integer.valueOf(time[0]);
			if (hours < 0)
				return null;
			date.setHours(hours);
			Integer minutes = Integer.valueOf(time[1]);
			if (minutes < 0 || minutes > 60)
				return null;
			date.setMinutes(minutes);
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String format(Date date) {
		return super.format(date) + " " + TimeFormatImpl.getTime(date);
	}
}

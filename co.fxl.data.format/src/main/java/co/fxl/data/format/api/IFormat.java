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
package co.fxl.data.format.api;

public interface IFormat<T> {

	String format(T object);

	String format(T object, String formatStyle);

	T parse(String text);

	T parse(String text, String formatStyle);

	// IFormat<T> setLocale(String locale);

	IFormat<T> defaultFormatStyle(String defaultFormatStyle);

	IFormat<T> timeZone(TimeZone pTimeZone);

	public class TimeZone {
		private String name;
		private int offsetToUtcInMinutes;

		public TimeZone(String pName, int pOffset) {
			name = pName;
			offsetToUtcInMinutes = pOffset;
		}

		public void setName(String pName) {
			name = pName;
		}

		public String getName() {
			return name;
		}

		public void setOffsetToUtcInMinutes(int pOffset) {
			offsetToUtcInMinutes = pOffset;
		}

		public int getOffsetToUtcInMinutes() {
			return offsetToUtcInMinutes;
		}
	}

}

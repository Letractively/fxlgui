/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.data.format.impl.Format;

public class GWTFormat {

	public static void setUp() {
		Format.registerDateTime(new GWTDateFormat(GWTDateFormat.DATE_TIME_PATTERN));
		Format.register(Date.class, new GWTDateFormat(GWTDateFormat.DATE_PATTERN));
		Format.registerTime(new GWTDateFormat(GWTDateFormat.TIME_PATTERN));
		Format.register(Long.class, new GWTNumberFormat<Long>() {
			@Override
			Long convert(Double d) {
				return d.longValue();
			}
		});
		Format.register(Integer.class, new GWTNumberFormat<Integer>() {
			@Override
			Integer convert(Double d) {
				return d.intValue();
			}
		});
	}
}

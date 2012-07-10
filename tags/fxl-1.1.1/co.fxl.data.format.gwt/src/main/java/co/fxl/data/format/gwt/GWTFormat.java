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

import co.fxl.data.format.impl.Format;

import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

public class GWTFormat {

	public static void setUp() {
		Format.registerDateTime(new GWTDateFormat(
				PredefinedFormat.DATE_TIME_MEDIUM));
		Format.register(Date.class, new GWTDateFormat(
				PredefinedFormat.DATE_SHORT));
		Format.registerTime(new GWTDateFormat(PredefinedFormat.TIME_LONG));
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
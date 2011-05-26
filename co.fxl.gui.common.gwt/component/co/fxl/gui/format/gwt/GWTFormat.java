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

import co.fxl.gui.format.impl.Format;

import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class GWTFormat {

	public static void setUp() {
		Format.registerTime(new GWTDateFormat(PredefinedFormat.DATE_TIME_MEDIUM));
		Format.register(Date.class, new GWTDateFormat(
				PredefinedFormat.DATE_SHORT));
		Format.register(Long.class,
				new GWTNumberFormat<Long>(NumberFormat.getDecimalFormat()) {
					@Override
					Long convert(Double d) {
						return d.longValue();
					}
				});
		Format.register(Integer.class, new GWTNumberFormat<Integer>(
				NumberFormat.getDecimalFormat()) {
			@Override
			Integer convert(Double d) {
				return d.intValue();
			}
		});
	}
}

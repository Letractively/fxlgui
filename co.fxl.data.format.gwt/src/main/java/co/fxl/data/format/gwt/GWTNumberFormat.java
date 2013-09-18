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

import co.fxl.data.format.api.IFormat;

import com.google.gwt.i18n.client.NumberFormat;

abstract class GWTNumberFormat<T> implements IFormat<T> {

	private String defaultFormatStyle = null;
//	private NumberFormat impl;

	GWTNumberFormat() {
//		impl = NumberFormat.get 
				// NumberFormat.getDecimalFormat();
	}
	
	protected NumberFormat getNumberFormat() {
		if (defaultFormatStyle == null) {
			return NumberFormat.getDecimalFormat();
		} else {
			return NumberFormat.getFormat(defaultFormatStyle);
		}
	}

	@Override
	public String format(T object) {
		if (object == null)
			return "";
	
		return getNumberFormat().format(((Number) object));
	}

	@Override
	public String format(T object, String pFormat) {
		return format(object);
	}

	@Override
	public T parse(String format) {
		if (format == null)
			return null;
		try {
			return convert(new Double(getNumberFormat().parse(format)));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public T parse(String format, String style) {
		return parse(format);
	}

	abstract T convert(Double d);

//	@Override
//	public IFormat<T> setLocale(String locale) {
//		return (IFormat<T>) this;
//	}
	
	@Override
	public IFormat<T> defaultFormatStyle(String pDefaultFormatStyle) {
		defaultFormatStyle = pDefaultFormatStyle;
		return this;
	}
	
}

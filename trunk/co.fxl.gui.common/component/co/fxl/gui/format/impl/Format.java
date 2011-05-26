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
import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.format.api.IFormat;

public class Format {

	private static Map<String, IFormat<?>> formats = new HashMap<String, IFormat<?>>();
	private static IFormat<Date> dateTimeFormat;
	private static IFormat<Date> timeFormat;
	static {
		setUp();
	}

	public static void register(Class<?> clazz, IFormat<?> format) {
		formats.put(clazz.getName(), format);
	}

	public static void registerDateTime(IFormat<Date> format) {
		dateTimeFormat = format;
	}

	public static void registerTime(IFormat<Date> format) {
		timeFormat = format;
	}

	private static void setUp() {
		register(Boolean.class, new FormatImpl<Boolean>() {
			@Override
			public Boolean parseWithException(String format) {
				return Boolean.valueOf(format);
			}
		});
		register(Long.class, new FormatImpl<Long>() {
			@Override
			public Long parseWithException(String format) {
				return Long.valueOf(format);
			}
		});
		register(Integer.class, new FormatImpl<Integer>() {
			@Override
			public Integer parseWithException(String format) {
				return Integer.valueOf(format);
			}
		});
		register(Date.class, new DateFormatImpl());
		registerDateTime(new DateTimeFormatImpl());
		registerTime(new TimeFormatImpl());
	}

	public static IFormat<?> get(Class<?> clazz) {
		IFormat<?> format = formats.get(clazz.getName());
		return format;
	}

	public static IFormat<?> get(String className) {
		return formats.get(className);
	}

	public static String format(Object object) {
		Class<? extends Object> clazz = object.getClass();
		return format(clazz, object);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String format(Class<? extends Object> clazz, Object object) {
		IFormat format = get(clazz);
		return format.format(object);
	}

	@SuppressWarnings({ "rawtypes" })
	public static Object parse(Class<?> clazz, String text) {
		IFormat format = get(clazz);
		return format.parse(text);
	}

	@SuppressWarnings("unchecked")
	public static IFormat<Date> date() {
		return (IFormat<Date>) get(Date.class);
	}

	public static IFormat<Date> dateTime() {
		return (IFormat<Date>) dateTimeFormat;
	}

	public static IFormat<Date> time() {
		return (IFormat<Date>) timeFormat;
	}

	@SuppressWarnings("unchecked")
	public static IFormat<Long> longInt() {
		return (IFormat<Long>) get(Long.class);
	}

	@SuppressWarnings("unchecked")
	public static IFormat<Integer> integer() {
		return (IFormat<Integer>) get(Integer.class);
	}

	@SuppressWarnings("unchecked")
	public static IFormat<Boolean> bool() {
		return (IFormat<Boolean>) get(Boolean.class);
	}
}

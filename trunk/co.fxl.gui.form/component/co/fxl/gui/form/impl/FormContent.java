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
package co.fxl.gui.form.impl;

import java.util.List;

public interface FormContent {

	enum Type {
		TEXT, LONG_TEXT, BOOLEAN, CONSTRAINED_TEXT;
	}

	public interface ITextField extends IField<String> {

		boolean isLong();
	}

	public interface ICheckBoxField extends IField<Boolean> {

	}

	public interface IConstrainedTextField extends IField<String> {

		String[] constraints();
	}

	public interface IField<T> {

		String name();

		Type type();

		T preset();

		T[] constraints();

		void value(T value);
	}

	String title();

	List<IField<?>> fields();
}

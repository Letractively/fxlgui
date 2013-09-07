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
package co.fxl.gui.api;

import co.fxl.gui.api.IClickable.IClickListener;

public interface IKeyRecipient<T> {

	public interface IKey<T> {

		IKey<T> ctrl();

		T enter();

		T tab();

		T up();

		T down();

		T left();

		T right();

		T character(char c);

		T backspace();
	}

	IKey<T> addKeyListener(IClickListener listener);
}

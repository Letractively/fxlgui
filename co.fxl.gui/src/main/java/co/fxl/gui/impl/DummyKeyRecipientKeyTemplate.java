/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.IKeyRecipient.IKey;

public class DummyKeyRecipientKeyTemplate<T> implements IKeyRecipient.IKey<T> {

	@Override
	public T enter() {
		// TODO ...
		return (T) this;
	}

	@Override
	public T tab() {
		// TODO ...
		return (T) this;
	}

	@Override
	public T up() {
		// TODO ...
		return (T) this;
	}

	@Override
	public T down() {
		// TODO ...
		return (T) this;
	}

	@Override
	public T left() {
		// TODO ...
		return (T) this;
	}

	@Override
	public T right() {
		// TODO ...
		return (T) this;
	}

	@Override
	public IKey<T> ctrl() {
		// TODO ...
		return this;
	}

	@Override
	public T character(char c) {
		// TODO ...
		return (T) this;
	}

}
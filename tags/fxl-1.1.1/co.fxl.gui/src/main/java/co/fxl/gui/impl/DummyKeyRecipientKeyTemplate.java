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

@SuppressWarnings("unchecked")
public class DummyKeyRecipientKeyTemplate<T> implements IKeyRecipient.IKey<T> {

	@Override
	public T enter() {
		return (T) this;
	}

	@Override
	public T tab() {
		return (T) this;
	}

	@Override
	public T up() {
		return (T) this;
	}

	@Override
	public T down() {
		return (T) this;
	}

	@Override
	public T left() {
		return (T) this;
	}

	@Override
	public T right() {
		return (T) this;
	}

	@Override
	public IKey<T> ctrl() {
		return this;
	}

	@Override
	public T character(char c) {
		return (T) this;
	}

}
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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IUpdateable;

public class ComboBoxAdp extends TextElementAdp<IComboBox> implements IComboBox {

	public ComboBoxAdp(IComboBox c) {
		super(c);
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		element().addUpdateListener(listener);
		return this;
	}

	@Override
	public co.fxl.gui.api.IKeyRecipient.IKey<IComboBox> addKeyListener(
			IClickListener listener) {
		return element().addKeyListener(listener);
	}

	@Override
	public IComboBox clear() {
		element().clear();
		return this;
	}

	@Override
	public IComboBox addNull() {
		element().addNull();
		return this;
	}

	@Override
	public IComboBox addText(String... texts) {
		element().addText(texts);
		return this;
	}

	@Override
	public IComboBox removeText(String... texts) {
		element().removeText(texts);
		return this;
	}

}

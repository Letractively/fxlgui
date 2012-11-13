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

import co.fxl.gui.api.ITextArea;

public class TextAreaAdp extends TextInputAdp<ITextArea> implements ITextArea {

	protected TextAreaAdp() {
		super();
	}

	protected TextAreaAdp(ITextArea element) {
		super(element);
	}

	@Override
	public int cursorPosition() {
		return element().cursorPosition();
	}

	@Override
	public ITextArea cursorPosition(int position) {
		element().cursorPosition(position);
		return this;
	}

	@Override
	public ITextArea clickable(boolean clickable) {
		return element().clickable(clickable);
	}

	@Override
	public boolean clickable() {
		return element().clickable();
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<ITextArea> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
		return element().addClickListener(clickListener);
	}

	@Override
	public ITextArea addResizeListener(
			co.fxl.gui.api.IResizable.IResizeListener listener) {
		element().addResizeListener(listener);
		return this;
	}

}

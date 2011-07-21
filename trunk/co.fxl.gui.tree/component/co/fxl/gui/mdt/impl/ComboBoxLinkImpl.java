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
package co.fxl.gui.mdt.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IUpdateable;
import co.fxl.gui.mdt.api.IComboBoxLink;

class ComboBoxLinkImpl implements IComboBoxLink {

	String name;
	List<IUpdateListener<String>> listeners = new LinkedList<IUpdateListener<String>>();
	List<String> texts = new LinkedList<String>();
	String text;

	ComboBoxLinkImpl(String name) {
		this.name = name;
	}

	@Override
	public IUpdateable<String> addUpdateListener(
			co.fxl.gui.api.IUpdateable.IUpdateListener<String> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IComboBoxLink addText(String... texts) {
		this.texts.addAll(Arrays.asList(texts));
		return this;
	}

	@Override
	public IComboBoxLink text(String text) {
		this.text = text;
		return this;
	}
}

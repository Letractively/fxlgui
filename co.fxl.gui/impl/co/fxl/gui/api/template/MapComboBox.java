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
package co.fxl.gui.api.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import co.fxl.gui.api.IComboBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IUpdateable;

public class MapComboBox<T> implements IUpdateable<T> {

	private static Heights HEIGHTS = new Heights(0);
	private IComboBox comboBox;
	private Map<String, T> text2object = new HashMap<String, T>();
	private Map<T, String> object2text = new HashMap<T, String>();

	public MapComboBox(IContainer container) {
		this.comboBox = container.comboBox();
	}

	public MapComboBox(IComboBox comboBox) {
		this.comboBox = comboBox;
		HEIGHTS.decorate(comboBox);
	}

	@Override
	public IUpdateable<T> addUpdateListener(
			final co.fxl.gui.api.IUpdateable.IUpdateListener<T> listener) {
		comboBox.addUpdateListener(new IUpdateListener<String>() {

			@Override
			public void onUpdate(String value) {
				listener.onUpdate(text2object.get(value));
			}
		});
		return this;
	}

	public MapComboBox<T> clear() {
		comboBox.clear();
		text2object.clear();
		object2text.clear();
		return this;
	}

	public MapComboBox<T> addNull() {
		comboBox.addNull();
		return this;
	}

	public MapComboBox<T> addObject(T object) {
		if (object == null)
			return addNull();
		return addObject(String.valueOf(object), object);
	}

	public MapComboBox<T> addObject(String text, T object) {
		comboBox.addText(text);
		text2object.put(text, object);
		object2text.put(object, text);
		return this;
	}

	public T object() {
		return text2object.get(comboBox.text());
	}

	public MapComboBox<T> object(T object) {
		comboBox.text(object2text.get(object));
		return this;
	}

	public MapComboBox<T> editable(boolean editable) {
		comboBox.editable(editable);
		return this;
	}

	public IComboBox comboBox() {
		return comboBox;
	}

	public Collection<T> objects() {
		return object2text.keySet();
	}
}

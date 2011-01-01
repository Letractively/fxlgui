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
package co.fxl.gui.gwt;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IComboBox;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

class GWTComboBox extends GWTElement<ListBox, IComboBox> implements IComboBox {

	private List<String> constraints = new LinkedList<String>();

	GWTComboBox(GWTContainer<ListBox> container) {
		super(container);
		font(this);
		container.widget.setHeight("24px");
	}

	@Override
	public IComboBox addText(String... texts) {
		for (String choice : texts) {
			constraints.add(choice);
			container.widget.addItem(choice);
		}
		container.widget.setSelectedIndex(0);
		return this;
	}

	@Override
	public IComboBox addUpdateListener(final IUpdateListener<String> listener) {
		container.widget.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				listener.onUpdate(text());
			}
		});
		return this;
	}

	@Override
	public IComboBox text(String choice) {
		if (choice == null)
			choice = "";
		if (!constraints.contains(choice))
			addText(choice);
		int index = constraints.indexOf(choice);
		container.widget.setSelectedIndex(index);
		return this;
	}

	@Override
	public String text() {
		return constraints.get(container.widget.getSelectedIndex());
	}

	@Override
	public IComboBox editable(boolean editable) {
		container.widget.setEnabled(editable);
		return this;
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public IComboBox tooltip(String text) {
		container.widget.setTitle(text);
		return this;
	}

	@Override
	public IColor color() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IComboBox clear() {
		container.widget.clear();
		return this;
	}
}

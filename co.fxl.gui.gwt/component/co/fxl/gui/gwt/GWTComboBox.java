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

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

class GWTComboBox extends GWTElement<ListBox, IComboBox> implements IComboBox {

	private List<String> constraints = new LinkedList<String>();
	private boolean hasNull = false;
	private int defaultHeight;
	private List<IUpdateListener<String>> listeners = new LinkedList<IUpdateListener<String>>();

	GWTComboBox(GWTContainer<ListBox> container) {
		super(container);
		container.widget.addStyleName("gwt-Combobox-FXL");
		defaultFont();
		defaultHeight = height();
		if (defaultHeight < 20)
			defaultHeight = 20;
		height(24);
	}

	@Override
	public IComboBox height(int height) {
		if (GWTDisplay.isFirefox()) {
			container.widget.getElement().getStyle()
					.setPaddingTop((height - defaultHeight) / 2, Unit.PX);
		}
		return super.height(height);
	}

	@Override
	public IComboBox addNull() {
		hasNull = true;
		return addText("");
	}

	@Override
	public IComboBox addText(String... texts) {
		for (String choice : texts) {
			if (choice == null) {
				hasNull = true;
				choice = "";
			}
			constraints.add(choice);
			container.widget.addItem(choice);
		}
		if (container.widget.getSelectedIndex() == -1)
			container.widget.setSelectedIndex(0);
		return this;
	}

	@Override
	public IComboBox addUpdateListener(final IUpdateListener<String> listener) {
		listeners.add(listener);
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
		String token = choice;
		if (choice == null) {
			if (!hasNull) {
				addNull();
			}
			token = "";
		}
		if (!constraints.contains(token))
			addText(choice);
		int index = constraints.indexOf(token);
		container.widget.setSelectedIndex(index);
		for (IUpdateListener<String> l : listeners)
			l.onUpdate(text());
		return this;
	}

	@Override
	public String text() {
		String string = constraints.get(container.widget.getSelectedIndex());
		if (hasNull && string.equals(""))
			return null;
		return string;
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
		return newBackgroundColor();
	}

	@Override
	public IComboBox clear() {
		container.widget.clear();
		return this;
	}
}

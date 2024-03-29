/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import co.fxl.gui.api.ICheckBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;

public class GWTCheckBox extends GWTElement<CheckBox, ICheckBox> implements
		ICheckBox {

	protected List<IUpdateListener<Boolean>> listeners = new LinkedList<IUpdateListener<Boolean>>();

	GWTCheckBox(GWTContainer<CheckBox> container) {
		super(container);
		init();
	}

	public void init() {
		container.widget.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean c = checked();
				fireUpdateListeners(c);
			}
		});
	}

	@Override
	public ICheckBox addUpdateListener(final IUpdateListener<Boolean> listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public ICheckBox focus(boolean focus) {
		if (editable()) {
			return super.focus(focus);
		}
		return this;
	}

	@Override
	public ICheckBox checked(boolean checked) {
		container.widget.setValue(checked);
		return this;
	}

	@Override
	public ICheckBox editable(boolean editable) {
		container.widget.setEnabled(editable);
		if (!editable)
			container.widget.addStyleName("gwt-CheckBox-disabled");
		else
			container.widget.removeStyleName("gwt-CheckBox-disabled");
		return this;
	}

	@Override
	public IFont font() {
		return new GWTFont(container.widget);
	}

	@Override
	public ICheckBox text(String text) {
		container.widget.setText(text);
		return this;
	}

	@Override
	public boolean checked() {
		return container.widget.getValue();
	}

	@Override
	public String text() {
		return container.widget.getText();
	}

	@Override
	public boolean editable() {
		return container.widget.isEnabled();
	}

	void fireUpdateListeners(boolean c) {
		for (IUpdateListener<Boolean> listener : listeners)
			listener.onUpdate(c);
	}
}

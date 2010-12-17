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
package co.fxl.gui.n2m.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;

class SelectableList {

	class ListItem implements IClickListener {

		private Object object;
		private IVerticalPanel p;
		private boolean visible = true;

		ListItem(Object object, boolean visible) {
			this.object = object;
			this.visible = visible;
		}

		boolean draw(boolean isFirst) {
			if (!visible)
				return false;
			p = panel.add().panel().vertical().spacing(4);
			p.add().label().text(String.valueOf(object));
			if (!isFirst) {
				IBorder b = p.border();
				b.color().lightgray();
				b.style().top();
			}
			p.addClickListener(this);
			return true;
		}

		@Override
		public void onClick() {
			if (selection != null) {
				selection.selected(false);
			}
			selection = this;
			selection.selected(true);
			button.clickable(selection != null);
		}

		void selected(boolean selected) {
			if (selected) {
				p.color().rgb(230, 230, 255);
			} else {
				p.color().remove();
			}
		}

		void visible(boolean b) {
			if (selection == this)
				selection = null;
			visible = b;
		}
	}

	private void draw() {
		selection = null;
		panel.clear();
		if (title != null) {
			IVerticalPanel p = panel.add().panel().vertical().spacing(4);
			p.color().gray();
			p.add().label().text(title).font().weight().bold().pixel(14)
					.color().white();
		}
		boolean hasOne = false;
		boolean isFirst = true;
		for (ListItem i : items) {
			hasOne |= i.draw(isFirst);
			if (isFirst) {
				if (hasOne) {
					isFirst = false;
				}
			}
		}
		allButton.clickable(hasOne);
		button.clickable(selection != null);
	}

	private IVerticalPanel panel;
	private List<ListItem> items = new LinkedList<ListItem>();
	private ListItem selection;
	private IButton button;
	private IButton allButton;
	private String title;

	SelectableList(IGridCell cell, String string) {
		IScrollPane scrollPane = cell.scrollPane();
		scrollPane.height(400);
		scrollPane.color().white();
		panel = scrollPane.viewPort().panel().vertical().spacing(3);
		scrollPane.border().color().gray();
		title = string;
	}

	void domain(List<Object> tokens, boolean visible) {
		for (Object o : tokens) {
			items.add(new ListItem(o, visible));
		}
		draw();
	}

	Object removeSelected() {
		Object o = selection.object;
		selection.visible(false);
		draw();
		button.clickable(false);
		return o;
	}

	List<Object> removeAll() {
		List<Object> os = new LinkedList<Object>();
		for (ListItem i : items) {
			i.visible(false);
			os.add(i.object);
		}
		return os;
	}

	void link(IButton button) {
		this.button = button;
		button.clickable(false);
	}

	void visible(List<Object> tokens, boolean b) {
		for (Object o : tokens)
			visible(o, b);
	}

	void visible(Object selection, boolean b) {
		for (ListItem i : items) {
			if (i.object == selection)
				i.visible(b);
		}
		draw();
	}

	List<Object> getItems() {
		List<Object> os = new LinkedList<Object>();
		for (ListItem i : items) {
			if (i.visible)
				os.add(i.object);
		}
		return os;
	}

	void linkAll(IButton button) {
		this.allButton = button;
	}

	void title(String left) {
		this.title = left;
	}
}

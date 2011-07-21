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
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.mdt.impl.Icons;

class SelectableList {

	class ListItem implements IClickListener {

		Object object;
		private IVerticalPanel p;
		private boolean visible = true;
		private ILabel label;
		private IImage reorderLogo;
		private IHorizontalPanel h;

		ListItem(Object object, boolean visible) {
			this.object = object;
			this.visible = visible;
		}

		@Override
		public String toString() {
			return label.text();
		}

		boolean draw(boolean isFirst) {
			if (!visible)
				return false;
			p = panel.add().panel().vertical().spacing(1).add().panel()
					.vertical().spacing(5);
			p.color().rgb(248, 248, 248).gradient().vertical()
					.rgb(216, 216, 216);
			h = p.add().panel().horizontal().align().begin().add().panel()
					.horizontal();
			boolean hasImage = widget.hasItemImage();
			if (hasImage) {
				IImage i = h.add().image();
				if (widget.itemImage != null)
					i.resource(widget.itemImage);
				String providerImage = widget.imageProvider != null ? widget.imageProvider
						.itemImage(object) : null;
				if (providerImage != null) {
					try {
						i.resource(providerImage);
					} catch (Exception e) {
					}
				}
				h.addSpace(4);
			}
			label = h.add().label().text(String.valueOf(object));
			if (widget.allowReorder)
				h.addSpace(4);
			// if (!isFirst) {
			IBorder b = p.border();
			b.color().rgb(172, 197, 213);
			// b.style().top();
			// }
			p.addClickListener(this);
			p.addClickListener(clickListener).doubleClick();
			if (preselected != null) {
				if (object.equals(preselected)) {
					selection = this;
				}
			}
			selected(this == selection);
			return true;
		}

		@Override
		public void onClick() {
			if (selection != null) {
				selection.selected(false);
			}
			selection = this;
			selection.selected(true);
			updateButtons();
		}

		void selected(boolean selected) {
			if (selected) {
				label.font().color().white();
				p.color().remove().rgb(172, 197, 213);
				if (widget.allowReorder)
					reorderLogo = h.add().image().resource(Icons.MOVE);
			} else {
				label.font().color().black();
				p.color().remove().rgb(248, 248, 248).gradient().vertical()
						.rgb(216, 216, 216);
				if (widget.allowReorder && reorderLogo != null)
					reorderLogo.remove();
				reorderLogo = null;
			}
		}

		void visible(boolean b) {
			if (selection != null && selection.equals(this) && !visible)
				selection = null;
			visible = b;
		}
	}

	private void draw() {
		selection = null;
		drawKeepSelection();
	}

	protected void drawKeepSelection() {
		panel.clear();
		if (title != null) {
			IVerticalPanel p = panel.add().panel().vertical().spacing(4);
			p.color().rgb(136, 136, 136).gradient().vertical()
					.rgb(113, 113, 113);
			p.add().label().text(title.toUpperCase()).font().weight().bold()
					.pixel(12).color().white();
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
		allButton.clickable(widget.editable && hasOne);
		updateButtons();
	}

	protected void updateButtons() {
		button.clickable(widget.editable && selection != null);
		if (buttonUp == null)
			return;
		boolean isUp = widget.editable && selection != null
				&& selectionIndex() > 0;
		buttonUp.clickable(isUp);
		buttonTop.clickable(isUp);
		boolean isDown = widget.editable && selection != null
				&& selectionIndex() < items.size() - 1;
		buttonDown.clickable(isDown);
		buttonBottom.clickable(isDown);
	}

	private int selectionIndex() {
		return items.indexOf(selection);
	}

	private N2MWidgetImpl widget;
	private IVerticalPanel panel;
	private List<ListItem> items = new LinkedList<ListItem>();
	ListItem selection;
	private IClickable<?> button;
	private IClickable<?> allButton;
	private String title;
	private boolean isSelected;
	private IScrollPane scrollPane;
	private IClickListener clickListener;
	private IImage buttonUp;
	private IImage buttonDown;
	private IImage buttonTop;
	private IImage buttonBottom;
	Object preselected;

	SelectableList(N2MWidgetImpl widget, IGridCell cell, String string,
			boolean isSelected) {
		this.widget = widget;
		scrollPane = cell.scrollPane();
		scrollPane.height(400);
		scrollPane.color().rgb(249, 249, 249);
		panel = scrollPane.viewPort().panel().vertical();// .spacing(3);
		scrollPane.border().color().rgb(172, 197, 213);
		title = string;
		this.isSelected = isSelected;
	}

	void height(int height) {
		scrollPane.height(height);
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

	void link(IClickable<?> button, IClickListener clickListener) {
		this.button = button;
		this.clickListener = clickListener;
		button.clickable(false);
	}

	void visible(List<Object> tokens, boolean b) {
		for (Object o : tokens) {
			visible(o, b, false);
		}
		draw();
		if (b)
			preselected = null;
	}

	void visible(Object selection, boolean b) {
		visible(selection, b, true);
	}

	void visible(Object selection, boolean b, boolean draw) {
		// TODO effizienter
		int x = 0;
		for (; x < items.size(); x++) {
			ListItem i = items.get(x);
			if (i.object.equals(selection)) {
				i.visible(b);
				break;
			}
		}
		if (isSelected && x < items.size()) {
			ListItem o = items.remove(x);
			items.add(o);
		}
		if (draw)
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

	void linkAll(IClickable<?> button) {
		this.allButton = button;
	}

	void title(String left) {
		this.title = left;
	}

	void up() {
		move(-1);
	}

	private void move(int i) {
		int index = items.indexOf(selection);
		int position = index + i;
		moveTo(position);
	}

	protected void moveTo(int position) {
		int index = items.indexOf(selection);
		items.remove(index);
		if (position >= items.size())
			items.add(selection);
		else
			items.add(position, selection);
		drawKeepSelection();
	}

	void down() {
		move(1);
	}

	void linkUp(IImage button, IClickListener clickListener) {
		this.buttonUp = button;
		button.clickable(false);
	}

	public void linkDown(IImage button, IClickListener clickListener) {
		this.buttonDown = button;
		button.clickable(false);
	}

	void linkTop(IImage button, IClickListener clickListener) {
		this.buttonTop = button;
		button.clickable(false);
	}

	public void linkBottom(IImage button, IClickListener clickListener) {
		this.buttonBottom = button;
		button.clickable(false);
	}

	void top() {
		moveTo(0);
	}

	void bottom() {
		moveTo(items.size());
	}
}

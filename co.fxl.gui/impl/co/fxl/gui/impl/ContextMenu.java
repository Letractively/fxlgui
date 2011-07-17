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
package co.fxl.gui.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;

public class ContextMenu {

	public class Entry implements IClickable<Entry> {

		private String text = null;
		private boolean clickable = true;
		private List<IClickListener> clickListeners = new LinkedList<IClickListener>();
		private String imageResource = null;

		private Entry(String text) {
			this.text = text;
		}

		public Entry imageResource(String imageResource) {
			this.imageResource = imageResource;
			return this;
		}

		@Override
		public Entry clickable(boolean clickable) {
			this.clickable = clickable;
			return this;
		}

		@Override
		public boolean clickable() {
			return clickable;
		}

		@Override
		public co.fxl.gui.api.IClickable.IKey<Entry> addClickListener(
				co.fxl.gui.api.IClickable.IClickListener clickListener) {
			clickListeners.add(clickListener);
			return null;
		}

		public Entry text(String text) {
			this.text = text;
			return this;
		}
	}

	private static final boolean SHOW_INACTIVE = false;
	private static ContextMenu instance;
	private IDisplay display;
	private List<Object> entries = new LinkedList<Object>();

	public ContextMenu(IDisplay display) {
		this.display = display;
	}

	public Entry addEntry(String text) {
		for (Object o : entries) {
			if (o instanceof Entry && text.equals(((Entry) o).text)) {
				Entry entry = (Entry) o;
				entry.clickListeners.clear();
				return entry;
			}
		}
		Entry entry = new Entry(text);
		entries.add(entry);
		return entry;
	}

	// public ContextMenu addSeparator() {
	// entries.add(this);
	// return this;
	// }

	public ContextMenu addHeader(String header) {
		header = header.toUpperCase();
		if (!entries.contains(header))
			entries.add(header);
		return this;
	}

	public ContextMenu reset() {
		entries.clear();
		return this;
	}

	public ContextMenu decorate(IClickable<?> c) {
		c.addClickListener(new IClickListener() {

			@Override
			public void onClick() {
				show();
			}
		}).mouseRight();
		return this;
	}

	public void show() {
		final IPopUp popUp = display.showPopUp().autoHide(true)// .width(320)
				.atLastClick();
		new Heights(0).decorateBorder(popUp).style().shadow();
		IVerticalPanel v = popUp.container().panel().vertical().spacing(8);
		// v.color().rgb(250, 250, 250);
		IVerticalPanel panel = v.add().panel().vertical();
		panel.spacing(4);
		for (Object o : entries) {
			if (!visible(o))
				continue;
			if (o instanceof Entry) {
				final Entry e = (Entry) o;
				IHorizontalPanel h = panel.add().panel().horizontal()
						.addSpace(10);
				IClickListener clickListener = new IClickListener() {
					@Override
					public void onClick() {
						popUp.visible(false);
						for (IClickListener cl : e.clickListeners)
							cl.onClick();
					}
				};
				if (e.imageResource != null) {
					h.add().image().resource(e.imageResource)
							.addClickListener(clickListener).mouseLeft()
							.clickable(e.clickable);
					h.addSpace(4);
				}
				ILabel l = h.add().label().text(e.text).hyperlink();
				l.addClickListener(clickListener);
				l.clickable(e.clickable);
			} else if (o instanceof String) {
				IHorizontalPanel h = panel.add().panel().horizontal();
				h.add().label().text((String) o).font().pixel(9).weight()
						.bold().color().gray();
				h.addSpace(4).add().line();
			}
			// else {
			// panel.add().line();
			// }
		}
		popUp.visible(true);
	}

	private boolean visible(Object o) {
		if (SHOW_INACTIVE)
			return true;
		if (o instanceof Entry) {
			return ((Entry) o).clickable;
		} else if (o instanceof String) {
			int index = entries.indexOf(o);
			for (int i = index + 1; i < entries.size(); i++) {
				Object o2 = entries.get(i);
				if (o2 instanceof String)
					return false;
				else if (((Entry) o2).clickable)
					return true;
			}
			return false;
		}
		return true;
	}

	public static ContextMenu instance(IDisplay display) {
		if (instance == null)
			instance = new ContextMenu(display);
		return instance;
	}

	public static ContextMenu instance() {
		return instance;
	}
}

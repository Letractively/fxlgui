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

		private String text;
		private boolean clickable = true;
		private List<IClickListener> clickListeners = new LinkedList<IClickListener>();
		private String imageResource;

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
	}

	private IDisplay display;
	private List<Object> entries = new LinkedList<Object>();

	public ContextMenu(IDisplay display) {
		this.display = display;
	}

	public Entry addEntry(String text) {
		Entry entry = new Entry(text);
		entries.add(entry);
		return entry;
	}

	public ContextMenu addSeparator() {
		entries.add(this);
		return this;
	}

	public ContextMenu addTo(IClickable<?> c) {
		c.addClickListener(new IClickListener() {

			@Override
			public void onClick() {
				IPopUp popUp = display.showPopUp().autoHide(true)//.width(320)
						.atLastClick();
				new Heights(0).decorateBorder(popUp).style().shadow();
				IVerticalPanel v = popUp.container().panel().vertical();
				v.color().rgb(250, 250, 250);
				IVerticalPanel panel = v.spacing(8).add().panel().vertical()
						.spacing(2);
				for (Object o : entries) {
					if (o instanceof Entry) {
						Entry e = (Entry) o;
						IHorizontalPanel h = panel.add().panel().horizontal();
						if (e.imageResource != null) {
							h.add().image().resource(e.imageResource);
							h.addSpace(4);
						}
						ILabel l = h.add().label().text(e.text).hyperlink();
						for (IClickListener cl : e.clickListeners)
							l.addClickListener(cl);
						l.clickable(e.clickable);
					} else {
						panel.add().line();
					}
				}
				popUp.visible(true);
			}
		}).mouseRight();
		return this;
	}
}

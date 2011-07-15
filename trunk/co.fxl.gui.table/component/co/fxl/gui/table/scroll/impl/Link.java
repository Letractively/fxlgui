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
package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;

class Link implements IClickable<Link> {

	static int SPACE = 4;
	IImage image;
	ILabel label;
	private IHorizontalPanel p;

	Link() {
	}

	Link(IHorizontalPanel p, IImage image, ILabel label) {
		this.p = p;
		this.image = image;
		this.label = label;
	}

	@Override
	public Link clickable(boolean clickable) {
		p.clickable(clickable);
		image.clickable(clickable);
		label.clickable(clickable);
		if (clickable) {
			label.font().color().black();
			// label.font().underline(true);
		} else {
			label.font().color().gray();
			// label.font().underline(false);
		}
		return this;
	}

	@Override
	public boolean clickable() {
		return label.clickable();
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<Link> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
		p.addClickListener(clickListener);
		image.addClickListener(clickListener);
		label.addClickListener(clickListener);
		return null;
	}

	private Link clickable(IContainer c, String string, boolean clickable) {
		IHorizontalPanel p = c.panel().horizontal();
		// p.spacing(4);
		// p.color().gray();
		// p.border().color().gray();
		String imageR = string.toLowerCase();
		if (imageR.equals("remove"))
			imageR = "cancel";
		if (imageR.equals("edit") || imageR.equals("show"))
			imageR = "detail";
		IImage image = p.add().image().resource(imageR + ".png");
		p.addSpace(SPACE);
		ILabel label = p.add().label().text(string);
		Link l = new Link(p, image, label);
		l.clickable(clickable);
		return l;
	}

	public Link clickableLink(IContainer c, String string) {
		Link link = clickable(c, string, true);
		return link;
	}
}
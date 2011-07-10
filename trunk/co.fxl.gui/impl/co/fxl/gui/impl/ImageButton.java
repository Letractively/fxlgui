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
package co.fxl.gui.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;

public class ImageButton implements IClickable<Object> {

	private static final int SPACE = 4;
	IImage image;
	private ILabel label;
	private IHorizontalPanel panel;
	private List<ILabel> additionalLabels = new LinkedList<ILabel>();
	private IVerticalPanel p0;

	public ImageButton(IContainer c) {
		this.panel = c.panel().horizontal();
		this.image = panel.add().image();
		this.label = panel.addSpace(SPACE).add().label();
		clickable(true);
	}

	public ImageButton(IHorizontalPanel panel, IImage image, ILabel textLabel) {
		this(null, panel, image, textLabel);
	}

	public ImageButton(IVerticalPanel p0, IHorizontalPanel panel, IImage image,
			ILabel textLabel) {
		this.p0 = p0;
		this.panel = panel;
		this.image = image;
		this.label = textLabel;
		clickable(true);
	}

	public ImageButton text(String text) {
		label.text(text);
		return this;
	}

	public ImageButton clickable(boolean clickable) {
		if (image != null)
			image.clickable(clickable);
		label.clickable(clickable);
		for (ILabel l : additionalLabels)
			l.clickable(clickable);
		if (p0 != null)
			p0.clickable(clickable);
		else
			panel.clickable(clickable);
		return this;
	}

	@Override
	public boolean clickable() {
		return label.clickable();
	}

	@Override
	public IKey<Object> addClickListener(IClickListener clickListener) {
		label.addClickListener(clickListener);
		if (image != null)
			image.addClickListener(clickListener);
		if (p0 != null)
			p0.addClickListener(clickListener);
		else
			panel.addClickListener(clickListener);
		return null;
	}

	public ILabel addHyperlink(String text) {
		panel.addSpace(4);
		ILabel label = panel.add().label().text("|");
		styleSeparator(label);
		ILabel l = panel.addSpace(4).add().label().text(text).hyperlink();
		additionalLabels.add(l);
		return l;
	}

	public void styleSeparator(ILabel label) {
		// Styles.instance().style(label, Style.Element.SEPARATOR);
		label.font().color().gray();
	}

	public void imageResource(String string) {
		image.resource(string);
	}
}
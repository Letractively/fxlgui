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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.impl;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IMouseOverElement;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.ContextMenu.Entry;

public class ImageButton implements ButtonAdp,
		co.fxl.gui.api.IClickable.IClickListener,
		IMouseOverElement<ImageButton> {

	private static final int SPACE = 4;
	IImage image;
	private ILabel label;
	private IHorizontalPanel panel;
	private List<ILabel> additionalLabels = new LinkedList<ILabel>();
	private IVerticalPanel p0;
	private String addToContextMenu = null;
	private List<IClickListener> clickListeners = new LinkedList<IClickListener>();
	private Entry entry;
	private boolean hasElements = false;
	private boolean hyperlink;
	private boolean showAlways;

	public ImageButton(IContainer c) {
		this(c, SPACE);
	}

	public ImageButton(IContainer c, int space) {
		this.panel = c.panel().horizontal();
		this.image = panel.add().image().size(16, 16);
		this.label = panel.addSpace(space).add().label();
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

	@Override
	public ButtonAdp text(String text) {
		label.text(text);
		if (addToContextMenu != null)
			if (entry == null) {
				entry = co.fxl.gui.impl.Page.instance().contextMenu()
						.group(addToContextMenu).addEntry(text);
				entry.addClickListener(this);
				if (image != null) {
					entry.imageResource(image.resource());
				}
			} else {
				entry.text(text);
			}
		return this;
	}

	public ImageButton clickable(boolean clickable) {
		if (CommandLink.HIDE_NON_CLICKABLE && !showAlways) {
			if (p0 != null)
				p0.visible(clickable);
			else
				panel.visible(clickable);
		}
		label.font().weight().plain();
		if (image != null)
			image.clickable(clickable);
		label.clickable(clickable);
		for (ILabel l : additionalLabels)
			l.clickable(clickable);
		if (!hasElements) {
			if (p0 != null)
				p0.clickable(clickable);
			else
				panel.clickable(clickable);
		}
		if (entry != null)
			entry.clickable(clickable);
		return this;
	}

	@Override
	public boolean clickable() {
		return label.clickable();
	}

	public ImageButton hyperlink() {
		label.hyperlink();
		return this;
	}

	public ImageButton lazyHyperlink() {
		hyperlink = true;
		return this;
	}

	@Override
	public IKey<Object> addClickListener(IClickListener clickListener) {
		if (hyperlink)
			label.hyperlink();
		label.addClickListener(clickListener);
		if (image != null)
			image.addClickListener(clickListener);
		if (p0 != null)
			p0.addClickListener(clickListener);
		else
			panel.addClickListener(clickListener);
		clickListeners.add(clickListener);
		return null;
	}

	public ILabel addHyperlink(String text) {
		panel.addSpace(SPACE);
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

	public ImageButton imageResource(String string) {
		if (string != null)
			image.resource(string);
		return this;
	}

	public ImageButton addToContextMenu(String group) {
		addToContextMenu = group;
		return this;
	}

	@Override
	public void onClick() {
		for (IClickListener l : clickListeners)
			l.onClick();
	}

	public IContainer addElement() {
		return addElement(SPACE);
	}

	public IContainer addElement(int i) {
		hasElements = true;
		p0.clickable(false);
		panel.clickable(false);
		return panel.addSpace(i).add();
	}

	public ImageButton addSpace(int i) {
		panel.addSpace(i);
		return this;
	}

	public ButtonAdp highlight(boolean b) {
		if (label.clickable())
			return this;
		if (b)
			label.font().weight().bold().color().black();
		else
			label.font().weight().plain().color().gray();
		return this;
	}

	public String text() {
		return label.text();
	}

	public ILabel label() {
		return label;
	}

	public IPanel<?> panel() {
		return panel;
	}

	public ButtonAdp showAlways(boolean b) {
		showAlways = b;
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ImageButton addMouseOverListener(
			co.fxl.gui.api.IMouseOverElement.IMouseOverListener l) {
		label.addMouseOverListener(l);
		if (image != null)
			image.addMouseOverListener(l);
		if (p0 != null && p0 instanceof IMouseOverElement)
			((IMouseOverElement) p0).addMouseOverListener(l);
		else if (panel instanceof IMouseOverElement)
			((IMouseOverElement) panel).addMouseOverListener(l);
		return this;
	}

	public void tooltip(String string) {
		label.tooltip(string);
		if (image != null)
			image.tooltip(string);
		if (p0 != null)
			p0.tooltip(string);
		else
			panel.tooltip(string);
	}
}
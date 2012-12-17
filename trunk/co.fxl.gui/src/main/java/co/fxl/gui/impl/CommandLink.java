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
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.impl.ContextMenu.Entry;
import co.fxl.gui.impl.ContextMenu.Group;

public class CommandLink implements IClickable<IClickable<?>> {

	static final boolean HIDE_NON_CLICKABLE = false;// !Env.is(Env.SWING);
	final WidgetTitle widgetTitle;
	private IHorizontalPanel iPanel;
	private ILabel label;
	private IImage image;
	private String toolTipClickable = null;
	private String toolTipNotClickable = null;
	private List<IClickListener> clickListeners = new LinkedList<IClickListener>();
	private Entry contextMenuEntry;
	private String text;
	private char ctrlKey;
	private IFocusPanel fp;

	public CommandLink(WidgetTitle widgetTitle, IFocusPanel fp,
			IHorizontalPanel iPanel, IImage image, ILabel headerLabel) {
		this.fp = fp;
		this.widgetTitle = widgetTitle;
		this.iPanel = iPanel;
		noDoubleClicks(iPanel);
		this.image = image;
		if (image != null)
			noDoubleClicks(image);
		this.label = headerLabel;
		if (headerLabel != null)
			text = headerLabel.text();
		noDoubleClicks(label);
	}

	private void noDoubleClicks(IClickable<?> c) {
		if (c != null)
			c.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
				}
			}).doubleClick();
	}

	public CommandLink label(String l) {
		this.text = l;
		if (label != null)
			label.text(l);
		return this;
	}

	@Override
	public IClickable<?> clickable(boolean clickable) {
		if (HIDE_NON_CLICKABLE) {
			visible(clickable);
		}
		if (label != null)
			label.clickable(clickable);
		if (image != null)
			image.clickable(clickable);
		if (fp != null)
			fp.clickable(clickable);
		iPanel.clickable(clickable);
		String tooltip = clickable ? toolTipClickable : toolTipNotClickable;
		if (tooltip != null) {
			iPanel.tooltip(tooltip);
			if (image != null)
				image.tooltip(tooltip);
			label.tooltip(tooltip);
		}
		styleDialogButton(label);
		if (contextMenuEntry != null)
			contextMenuEntry.clickable(clickable);
		return this;
	}

	public void styleDialogButton(ILabel label) {
		// Styles.instance().style(label, Style.Window.DIALOG,
		// Style.Element.BUTTON);
		if (label == null)
			return;
		if (label.clickable())
			label.font().color().black();
		else
			label.font().color().gray();
	}

	@Override
	public boolean clickable() {
		if (label != null)
			return label.clickable();
		else
			return image.clickable();
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<IClickable<?>> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
		label.addClickListener(clickListener);
		if (image != null)
			image.addClickListener(clickListener);
		if (fp != null)
			fp.addClickListener(clickListener);
		iPanel.addClickListener(clickListener);
		clickListeners.add(clickListener);
		return null;
	}

	public IClickable<?> tooltips(String toolTipClickable,
			String toolTipNotClickable) {
		this.toolTipClickable = toolTipClickable;
		this.toolTipNotClickable = toolTipNotClickable;
		return this;
	}

	public CommandLink addToContextMenu(String group) {
		Group g = co.fxl.gui.impl.Page.instance().contextMenu().group(group);
		contextMenuEntry = g.addEntry(text);
		if (ctrlKey != 0)
			contextMenuEntry.ctrlKey(ctrlKey);
		if (image != null) {
			String resource = image.resource();
			contextMenuEntry.imageResource(resource);
		}
		contextMenuEntry.addClickListener(getFire());
		return this;
	}

	co.fxl.gui.api.IClickable.IClickListener getFire() {
		return new IClickListener() {
			@Override
			public void onClick() {
				for (IClickListener l : clickListeners)
					l.onClick();
			}
		};
	}

	public void text(String string) {
		label.text(string);
		if (contextMenuEntry != null)
			contextMenuEntry.text(string);
	}

	public void image(String string) {
		image.resource(string);
		if (contextMenuEntry != null)
			contextMenuEntry.imageResource(string);
	}

	public CommandLink ctrlKey(char c) {
		ctrlKey = c;
		if (contextMenuEntry != null)
			contextMenuEntry.ctrlKey(c);
		return this;
	}

	public void visible(boolean b) {
		if (fp != null)
			fp.visible(b);
		iPanel.visible(b);
	}

	public void acceptEnter() {
		fp.addKeyListener(getFire()).enter().focus(true);
	}

	public CommandLink background(boolean b) {
		if (!b) {
			iPanel.color().remove();
			if (fp != null)
				fp.color().remove();
		}
		return this;
	}

	public String text() {
		return label.text();
	}

	public void flip(boolean flip) {
		if (flip) {
			image.remove();
			iPanel.add(image);
		} else {
			label.remove();
			iPanel.add(label);
		}
	}

	public int width() {
		return iPanel.width();
	}

	public void width(int width) {
		iPanel.width(width);
	}
}
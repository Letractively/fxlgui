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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.impl.ContextMenu.Entry;
import co.fxl.gui.impl.ContextMenu.Group;

public class CommandLink implements IClickable<IClickable<?>>,
		co.fxl.gui.api.IClickable.IClickListener {

	static final boolean HIDE_NON_CLICKABLE = !Env.is(Env.SWING);
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
	private IHorizontalPanel backgroundPanel;
	private boolean hide;
	private boolean setup;

	public CommandLink(WidgetTitle widgetTitle, IFocusPanel fp,
			IHorizontalPanel backgroundPanel, IHorizontalPanel iPanel,
			IImage image, ILabel headerLabel) {
		this.fp = fp;
		this.widgetTitle = widgetTitle;
		this.backgroundPanel = backgroundPanel;
		this.iPanel = iPanel;
		noDoubleClicks(iPanel);
		this.image = image;
		if (image != null)
			noDoubleClicks(image);
		this.label = headerLabel;
		if (headerLabel != null) {
			text = headerLabel.text();
			new HyperlinkMouseOverListener(headerLabel);
			headerLabel.margin().right(2);
		}
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
		setUp();
		clickListeners.add(clickListener);
		return null;
	}

	void setUp() {
		if (setup)
			return;
		setup = true;
		label.addClickListener(this);
		if (image != null)
			image.addClickListener(this);
		if (fp != null)
			fp.addClickListener(this);
		else
			iPanel.addClickListener(this);
	}

	public CommandLink tooltips(String toolTipClickable,
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

	public void removeFromContextMenu() {
		if (contextMenuEntry != null)
			contextMenuEntry.remove();
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

	public IColor background() {
		return backgroundPanel.color().remove();
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

	public void fireClick(String buttonLabel) {
		if (label.text().equals(buttonLabel))
			for (IClickListener l : clickListeners)
				l.onClick();
	}

	public IPanel<?> panel() {
		return fp;
	}

	private boolean beforeHide = true;

	public void hide(boolean b) {
		if (hide == b)
			return;
		if (b) {
			beforeHide = iPanel.visible();
			visible(false);
		} else {
			visible(beforeHide);
		}
		hide = b;
	}

	public void tooltip(String string) {
		iPanel.tooltip(string);
		label.tooltip(string);
		if (fp != null)
			fp.tooltip(string);
	}

	public IColor color() {
		return backgroundPanel.color();
	}

	public ILabel label() {
		return label;
	}

	public IBorder border() {
		return backgroundPanel.border();
	}

	@Override
	public void onClick() {
		for (IClickListener c : clickListeners)
			c.onClick();
	}
}
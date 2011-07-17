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
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.impl.ContextMenu.Entry;

public class CommandLink implements IClickable<IClickable<?>> {

	final WidgetTitle widgetTitle;
	private IHorizontalPanel iPanel;
	private ILabel label;
	private IImage image;
	private String toolTipClickable = null;
	private String toolTipNotClickable = null;
	private List<IClickListener> clickListeners = new LinkedList<IClickListener>();
	private Entry contextMenuEntry;

	public CommandLink(WidgetTitle widgetTitle, IHorizontalPanel iPanel,
			IImage image, ILabel headerLabel) {
		this.widgetTitle = widgetTitle;
		this.iPanel = iPanel;
		noDoubleClicks(iPanel);
		this.image = image;
		if (image != null)
			noDoubleClicks(image);
		this.label = headerLabel;
		noDoubleClicks(label);
	}

	private void noDoubleClicks(IClickable<?> c) {
		c.addClickListener(new IClickListener() {

			@Override
			public void onClick() {
			}
		}).doubleClick();
	}

	public CommandLink label(String l) {
		label.text(l);
		return this;
	}

	@Override
	public IClickable<?> clickable(boolean clickable) {
		label.clickable(clickable);
		if (image != null)
			image.clickable(clickable);
		iPanel.clickable(clickable);
		String tooltip = clickable ? toolTipClickable : toolTipNotClickable;
		if (tooltip != null) {
			iPanel.tooltip(tooltip);
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
		if (label.clickable())
			label.font().color().black();
		else
			label.font().color().gray();
	}

	@Override
	public boolean clickable() {
		return label.clickable();
	}

	@Override
	public co.fxl.gui.api.IClickable.IKey<IClickable<?>> addClickListener(
			co.fxl.gui.api.IClickable.IClickListener clickListener) {
		label.addClickListener(clickListener);
		if (image != null)
			image.addClickListener(clickListener);
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

	public CommandLink addToContextMenu(boolean addToContextMenu) {
		if (addToContextMenu) {
			contextMenuEntry = ContextMenu.instance().addEntry(label.text());
			contextMenuEntry.imageResource(image.resource());
			contextMenuEntry.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					for (IClickListener l : clickListeners)
						l.onClick();
				}
			});
		}
		return this;
	}
}
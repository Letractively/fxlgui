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
package co.fxl.gui.api.template;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;

public class WidgetTitle implements IClickListener {

	class CommandLink implements IClickable<IClickable<?>> {

		// private IHorizontalPanel iPanel;
		private ILabel label;
		private IImage image;

		public CommandLink(IHorizontalPanel iPanel, IImage image,
				ILabel headerLabel) {
			// this.iPanel = iPanel;
			this.image = image;
			this.label = headerLabel;
		}

		@Override
		public IClickable<?> clickable(boolean clickable) {
			// iPanel.visible(clickable);
			label.clickable(clickable);
			if (image != null)
				image.clickable(clickable);
			if (grayBackground) {
				if (clickable) {
					label.font().color().white();
					label.font().underline(true);
				} else {
					label.font().color().lightgray();
					label.font().underline(false);
				}
			}
			return this;
		}

		@Override
		public boolean clickable() {
			return label.clickable();
			// return iPanel.visible();
		}

		@Override
		public co.fxl.gui.api.IClickable.IKey<IClickable<?>> addClickListener(
				co.fxl.gui.api.IClickable.IClickListener clickListener) {
			label.addClickListener(clickListener);
			if (image != null)
				image.addClickListener(clickListener);
			return null;
		}

	}

	public static final int LARGE_FONT = 18;
	private String openPNG = "open.png";
	private String closedPNG = "closed.png";
	public IGridPanel panel;
	private IHorizontalPanel titlePanel;
	private IHorizontalPanel commandPanel;
	private boolean hasCommands = false;
	private boolean hasHeaderPanel = false;
	private IDockPanel headerPanel;
	private IContainer contentContainer;
	private boolean open = true;
	private IImage image;
	private boolean foldable = true;
	public ILabel headerLabel;
	private List<ILabel> labels = new LinkedList<ILabel>();
	private List<IImage> images = new LinkedList<IImage>();
	private int space = 10;
	private Map<ILabel, Boolean> clickableState = new HashMap<ILabel, Boolean>();
	private boolean holdOnClicks = false;
	private boolean grayBackground = false;

	public WidgetTitle(ILayout layout) {
		panel = layout.grid();
		headerPanel = panel.cell(0, 0).panel().dock();
		headerPanel.visible(false);
	}

	public WidgetTitle holdOnClick() {
		holdOnClicks = true;
		return this;
	}

	public WidgetTitle reset() {
		for (ILabel l : clickableState.keySet()) {
			l.clickable(clickableState.get(l));
		}
		return this;
	}

	public WidgetTitle space(int space) {
		this.space = space;
		return this;
	}

	public WidgetTitle triangleIcons() {
		openPNG = "open_folder.png";
		closedPNG = "closed_folder.png";
		return this;
	}

	public WidgetTitle foldable(boolean foldable) {
		this.foldable = foldable;
		return this;
	}

	public WidgetTitle grayBackground() {
		if (headerPanel != null)
			headerPanel.color().gray();
		if (titlePanel != null)
			titlePanel.color().gray();
		if (headerLabel != null)
			headerLabel.font().color().white();
		grayBackground = true;
		return this;
	}

	private void initHeader() {
		if (hasHeaderPanel)
			return;
		headerPanel.visible(true);
		// headerPanel.color().rgb(220, 220, 220);
		IHorizontalPanel horizontal = headerPanel.center().panel().horizontal()
				.align().begin();
		if (grayBackground)
			horizontal.color().gray();
		titlePanel = horizontal.add().panel().horizontal().align().begin()
				.spacing(4);
		if (foldable) {
			image = titlePanel.add().image().resource(openPNG);
			image.addClickListener(this);
		}
		IContainer cell = headerPanel.right();
		commandPanel = cell.panel().horizontal().spacing(6);
		// panel.addSpace(space);
		hasHeaderPanel = true;
	}

	@Override
	public void onClick() {
		open = !open;
		if (open) {
			image.resource(openPNG);
		} else {
			image.resource(closedPNG);
		}
		contentContainer.element().visible(open);
		for (ILabel l : labels)
			l.visible(open);
		for (IImage l : images)
			l.visible(open);
	}

	public ILabel addTitle(String title) {
		initHeader();
		if (!grayBackground)
			headerPanel.border().style().bottom();
		headerLabel = titlePanel.add().label().text(title);
		if (foldable) {
			headerLabel.addClickListener(this);
		}
		headerLabel.font().weight().bold().pixel(14);
		if (grayBackground)
			headerLabel.font().color().white();
		return headerLabel;
	}

	public IClickable<?> addHyperlink(String text) {
		return addHyperlink(null, text);
	}

	public IClickable<?> addHyperlink(String imageResource, String text) {
		initHeader();
		if (hasCommands && imageResource == null) {
			ILabel label = commandPanel.add().label().text("|");
			if (grayBackground)
				label.font().color().lightgray();
			else
				label.font().color().gray();
			labels.add(label);
		}
		hasCommands = true;
		IHorizontalPanel iPanel = commandPanel.add().panel().horizontal();
		IImage image = null;
		if (imageResource != null) {
			image = iPanel.add().image().resource(imageResource);
			images.add(image);
		}
		final ILabel label = iPanel.addSpace(2).add().label().text(text);
		if (!grayBackground)
			label.hyperlink();
		label.font().pixel(12);
		labels.add(label);
		if (holdOnClicks) {
			label.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					LinkedList<ILabel> ls = new LinkedList<ILabel>(
							clickableState.keySet());
					for (ILabel hyperlink : ls) {
						clickableState.put(hyperlink, label.clickable());
						hyperlink.clickable(false);
					}
				}
			});
			clickableState.put(label, true);
		}
		CommandLink cl = new CommandLink(iPanel, image, label);
		cl.clickable(true);
		return cl;
	}

	public IImage addImage(String image) {
		initHeader();
		if (hasCommands) {
			commandPanel.add().label().text("|").font().color().gray();
		}
		hasCommands = true;
		IImage label = commandPanel.add().image().resource(image);
		return label;
	}

	public IContainer content() {
		if (contentContainer != null)
			return contentContainer;
		return contentContainer = space == 0 ? panel.cell(0, 1) : panel
				.cell(0, 1).panel().vertical().addSpace(space).add();
	}

	public WidgetTitle clearHyperlinks() {
		commandPanel.clear();
		hasCommands = false;
		return this;
	}

	public WidgetTitle visible(boolean b) {
		panel.visible(b);
		return this;
	}
}

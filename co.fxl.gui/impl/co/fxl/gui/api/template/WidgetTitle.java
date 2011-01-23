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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;

public class WidgetTitle implements IClickListener {

	private String openPNG = "minus.png";
	private String closedPNG = "plus.png";
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
	private int space = 10;
	private Map<ILabel, Boolean> clickableState = new HashMap<ILabel, Boolean>();
	private boolean holdOnClicks = false;

	public WidgetTitle(ILayout layout) {
		panel = layout.grid();
		headerPanel = panel.cell(0, 0).panel().dock();
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

	private void initHeader() {
		if (hasHeaderPanel)
			return;
		// headerPanel.color().rgb(220, 220, 220);
		titlePanel = headerPanel.center().panel().horizontal().add().panel()
				.horizontal().spacing(4);
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
	}

	public ILabel addTitle(String title) {
		initHeader();
		headerPanel.border().style().bottom();
		headerLabel = titlePanel.add().label().text(title);
		if (foldable) {
			headerLabel.addClickListener(this);
		}
		headerLabel.font().weight().bold().pixel(16);
		return headerLabel;
	}

	public ILabel addHyperlink(String text) {
		initHeader();
		if (hasCommands) {
			ILabel label = commandPanel.add().label().text("|");
			label.font().color().gray();
			labels.add(label);
		}
		hasCommands = true;
		final ILabel label = commandPanel.add().label().text(text).hyperlink();
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
		return label;
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
		return contentContainer = space == 0 ? panel.cell(0, 0) : panel
				.cell(0, 1).panel().vertical().addSpace(space).add();
	}

	public WidgetTitle clearHyperlinks() {
		commandPanel.clear();
		hasCommands = false;
		return this;
	}
}

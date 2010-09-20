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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;

public class WidgetTitle implements IClickListener {

	private String openPNG = "minus.png";
	private String closedPNG = "plus.png";
	private IVerticalPanel panel;
	private IHorizontalPanel titlePanel;
	private IHorizontalPanel commandPanel;
	private boolean hasCommands = false;
	private boolean hasHeaderPanel = false;
	private IDockPanel headerPanel;
	private IContainer contentContainer;
	private boolean open = true;
	private IImage image;
	private boolean foldable = true;
	private ILabel headerLabel;
	private List<ILabel> labels = new LinkedList<ILabel>();

	public WidgetTitle(ILayout layout) {
		panel = layout.vertical();
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
		headerPanel = panel.add().panel().dock();
		titlePanel = headerPanel.center().panel().horizontal().add().panel()
				.horizontal().spacing(4);
		if (foldable) {
			image = titlePanel.add().image().resource(openPNG);
			image.addClickListener(this);
		}
		IContainer cell = headerPanel.right();
		commandPanel = cell.panel().horizontal().spacing(6);
		panel.addSpace(10);
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
		ILabel label = commandPanel.add().label().text(text).hyperlink();
		labels.add(label);
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
		return contentContainer = panel.add();
	}

	public WidgetTitle clearHyperlinks() {
		commandPanel.clear();
		hasCommands = false;
		return this;
	}
}

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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;

public class WidgetTitle {

	private IVerticalPanel panel;
	private IHorizontalPanel titlePanel;
	private IHorizontalPanel commandPanel;
	private boolean hasCommands = false;
	private boolean hasHeaderPanel = false;
	private IDockPanel headerPanel;

	public WidgetTitle(ILayout layout) {
		panel = layout.vertical();
	}

	private void initHeader() {
		if (hasHeaderPanel)
			return;
		headerPanel = panel.add().panel().dock();
		titlePanel = headerPanel.center().panel().horizontal().add().panel()
				.horizontal().spacing(4);
		IContainer cell = headerPanel.right();
		commandPanel = cell.panel().horizontal().spacing(6);
		panel.addSpace(10);
		hasHeaderPanel = true;
	}

	public ILabel addTitle(String title) {
		initHeader();
		headerPanel.border().style().bottom();
		ILabel label = titlePanel.add().label().text(title);
		label.font().weight().bold().pixel(16);
		return label;
	}

	public ILabel addHyperlink(String text) {
		initHeader();
		if (hasCommands) {
			commandPanel.add().label().text("|").font().color().gray();
		}
		hasCommands = true;
		ILabel label = commandPanel.add().label().text(text).hyperlink();
		return label;
	}

	public IContainer content() {
		return panel.add();
	}

	public WidgetTitle clearHyperlinks() {
		commandPanel.clear();
		hasCommands = false;
		return this;
	}
}

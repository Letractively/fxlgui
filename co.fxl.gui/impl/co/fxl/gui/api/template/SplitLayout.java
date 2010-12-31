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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;

public class SplitLayout implements IResizeListener, IClickListener {

	// TODO nice-2-have: SplitLayout consists of 3 cells, coupled to height of
	// display

	private static final int WIDTH_SIDE_PANEL = 300;
	private ILayout layout;
	public IGridPanel panel;
	public IVerticalPanel mainPanel;
	public IVerticalPanel sidePanel;
	private IGridCell cell;
	private IImage button;

	public SplitLayout(ILayout layout) {
		this.layout = layout;
		init();
	}

	private void init() {
		panel = layout.grid();
		IVerticalPanel vpanel = panel.cell(0, 0).valign().begin().panel()
				.vertical().spacing(10);
		mainPanel = vpanel.add().panel().vertical().spacing(10);
		mainPanel.border().color().lightgray();
		mainPanel.color().white();
		IHorizontalPanel horizontal = panel.cell(1, 0).width(20).align()
				.center().valign().begin().panel().horizontal();
		horizontal.addSpace(10);
		button = horizontal.add().image().resource("minimize.png");
		cell = panel.cell(2, 0).width(WIDTH_SIDE_PANEL).valign().begin();
		IVerticalPanel vertical = cell.panel().vertical();
		// horizontal.addClickListener(clickListener);
		button.addClickListener(this);
		sidePanel = vertical.add().panel().vertical().spacing(10);
		// onResize(-1, panel.display().height());
		// ResizeListener.setup(panel.display(), this);
	}

	@Override
	public void onClick() {
		boolean visible = !sidePanel.visible();
		onClick(visible);
	}

	private void onClick(boolean visible) {
		sidePanel.visible(visible);
		cell.width(sidePanel.visible() ? 300 : 0);
		button.resource(sidePanel.visible() ? "minimize.png" : "maximize.png");
	}

	@Override
	public void onResize(int width, int height) {
		resize(height, panel);
	}

	private void resize(int height, IElement<?> p) {
		int offsetY = p.offsetY();
		int maxFromDisplay = height - offsetY - 30;
		p.height(maxFromDisplay);
	}

	public void reset() {
		panel.remove();
		init();
	}

	public void showSplit(boolean showSplit) {
		if (!showSplit) {
			onClick(false);
			button.resource(null);
		} else {
			onClick(true);
		}
	}
}

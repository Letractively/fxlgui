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

import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;

public class SplitLayout implements IResizeListener// , IClickListener
{

	// TODO nice-2-have: SplitLayout consists of 3 cells, coupled to height of
	// display

	// TODO Swing: minimize doesn't work

	private static final int SCROLLBAR_WIDTH = 18;
	private static final int WIDTH_SIDE_PANEL = 300 + SCROLLBAR_WIDTH;
	private ILayout layout;
	public IGridPanel panel;
	public IVerticalPanel mainPanel;
	public IVerticalPanel sidePanel;
	private IGridCell cell;
	// private IImage button;
	private IScrollPane sideScrollPanel;
	private IGridCell cell0;

	public SplitLayout(ILayout layout) {
		this.layout = layout;
		init();
	}

	private void init() {
		panel = layout.grid();
		cell0 = panel.cell(0, 0);
		IVerticalPanel vpanel = cell0.valign().begin().panel().vertical()
				.spacing(10);
		mainPanel = addMainPanel(vpanel).spacing(10);
		// IHorizontalPanel horizontal = panel.cell(1, 0).width(16).align()
		// .center().valign().center().panel().horizontal();
		// // horizontal.addSpace(10);
		// button = horizontal.add().image().resource("minimize.png");
		cell = panel.cell(1, 0).width(WIDTH_SIDE_PANEL).valign().begin()
				.align().end();
		// IVerticalPanel vertical = cell.panel().vertical();
		// horizontal.addClickListener(clickListener);
		// button.addClickListener(this);
		sideScrollPanel = cell.scrollPane();
		sidePanel = sideScrollPanel.viewPort().panel().vertical().spacing(10);
		sidePanel.width(WIDTH_SIDE_PANEL - SCROLLBAR_WIDTH);
		SidePanelResizeListener.setup(panel.display(), this);
		onResize(panel.display().width(), panel.display().height());
	}

	@Override
	public void onResize(int width, int height) {
		resizeSidePanel(height);
		cell0.width(width - WIDTH_SIDE_PANEL);
	}

	private void resizeSidePanel(int height) {
		int offsetY = sideScrollPanel.offsetY();
		int maxFromDisplay = height - offsetY - 30;
		if (maxFromDisplay > 0)
			sideScrollPanel.height(maxFromDisplay);
	}

	protected IVerticalPanel addMainPanel(IVerticalPanel vpanel) {
		mainPanel = vpanel.add().panel().vertical();
		mainPanel.border().color().lightgray();
		mainPanel.color().white();
		return mainPanel;
	}

	// @Override
	// public void onClick() {
	// boolean visible = !sidePanel.visible();
	// onClick(visible);
	// }

	// private void onClick(boolean visible) {
	// sidePanel.visible(visible);
	// cell.width(sidePanel.visible() ? 300 : 1);
	// button.resource(sidePanel.visible() ? "minimize.png" : "maximize.png");
	// }

	public void reset() {
		panel.remove();
		init();
	}

	public void showSplit(boolean showSplit) {
		// if (!showSplit) {
		// onClick(false);
		// button.resource(null);
		// } else {
		// onClick(true);
		// }
	}
}

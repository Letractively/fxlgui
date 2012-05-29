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

import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;

public class SplitLayout implements IResizeListener {

	// TODO Code Quality Fine-Tuning: nice-2-have: SplitLayout consists of 3 cells, coupled to height of
	// display

	// TODO SWING-FXL: minimize doesn't work

	public static int SCROLLBAR_WIDTH = 20;
	public static int WIDTH_SIDE_PANEL = 320;
	private ILayout layout;
	IGridPanel panel;
	public IVerticalPanel mainPanel;
	public IVerticalPanel sidePanel;
	IGridCell cell1;
	// private IImage button;
	private IScrollPane sideScrollPanel;
	IGridCell cell0;
	private boolean resizeMainPanel;
	private IVerticalPanel sideBasePanel;
	private boolean isDetached;

	public SplitLayout(ILayout layout) {
		this(layout, false);
	}

	public SplitLayout(ILayout layout, boolean resizeMainPanel) {
		this.layout = layout;
		this.resizeMainPanel = resizeMainPanel;
		init();
	}

	private void init() {
		panel = layout.grid();
		cell0 = panel.cell(0, 0).width(mainPanelWidth());
		IVerticalPanel vpanel = cell0.valign().begin().panel().vertical()
				.spacing(10);
		mainPanel = addMainPanel(vpanel);
		cell1 = panel.cell(1, 0).width(WIDTH_SIDE_PANEL).valign().begin()
				.align().end();
		sideBasePanel = cell1.panel().vertical();
		sideScrollPanel = sideBasePanel.addSpace(10).add().scrollPane();
		sidePanel = sideScrollPanel.viewPort().panel().vertical();
		sidePanel.spacing().right(10).inner(10);
		DisplayResizeAdapter.addResizeListener(this, true);
	}

	public static int mainPanelWidth() {
		return Display.instance().width() - WIDTH_SIDE_PANEL - 3 * 10;
	}

	@Override
	public boolean onResize(int width, int height) {
		if (!panel.visible())
			return false;
		resizeSidePanel(height);
		cell0.width(width - WIDTH_SIDE_PANEL);
		return true;
	}

	public void detachSidePanel() {
		if (isDetached)
			return;
		sideBasePanel.remove();
		isDetached = true;
	}

	public void attachSidePanel() {
		if (!isDetached)
			return;
		cell1.element(sideBasePanel);
		isDetached = false;
	}

	private void resizeSidePanel(int height) {
		int offsetY = sideScrollPanel.offsetY();
		if (offsetY < 68)
			offsetY = 68;
		int maxFromDisplay = height - offsetY - 10;
		if (maxFromDisplay > 0) {
			if (resizeMainPanel)
				mainPanel.height(maxFromDisplay);
			sideScrollPanel.height(maxFromDisplay);
		}
	}

	protected IVerticalPanel addMainPanel(IVerticalPanel vpanel) {
		mainPanel = vpanel.add().panel().vertical();
		mainPanel.color().white();
		return mainPanel;
	}

	public void reset() {
		panel.remove();
		init();
	}

	public void showSplit(boolean showSplit) {
	}
}

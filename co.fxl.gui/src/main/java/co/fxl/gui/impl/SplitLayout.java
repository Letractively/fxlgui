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

import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.style.impl.Style;

public class SplitLayout extends ResizableWidgetTemplate implements
		IResizeListener {

	// TODO SWING-FXL: minimize doesn't work

	private static final int H_SPACE = 14;
	private static final int V_SPACE = 10;
	public static final int DECREMENT = 23;
	public static int SCROLLBAR_WIDTH = 20;
	public static int WIDTH_SIDE_PANEL = 320 - DECREMENT;
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
	private boolean adHocWidth = false;
	private boolean hasSidePanel = true;

	public SplitLayout(boolean adHocWidth, boolean hasSidePanel) {
		this.adHocWidth = adHocWidth;
		this.hasSidePanel = hasSidePanel;
	}

	public SplitLayout(ILayout layout, IResizableWidget widget) {
		this(layout, false, widget);
	}

	public SplitLayout(ILayout layout, boolean resizeMainPanel,
			IResizableWidget widget) {
		this.layout = layout;
		this.resizeMainPanel = resizeMainPanel;
		widget.setResizableWidget(this, "splitLayout");
		init();
	}

	private void init() {
		panel = layout.grid().spacing(0);
		int width = width();
		cell0 = panel.cell(0, 0).width(width);
		IVerticalPanel vertical = cell0.valign().begin().panel().vertical();
		vertical.padding().top(V_SPACE).bottom(V_SPACE).left(H_SPACE)
				.right(H_SPACE);
		mainPanel = addMainPanel(vertical);
		cell1 = panel.cell(1, 0).width(WIDTH_SIDE_PANEL).valign().begin()
				.align().end();
		sideBasePanel = cell1.panel().vertical();
		sideBasePanel.padding().top(V_SPACE);
		sideScrollPanel = sideBasePanel.add().scrollPane();
		sidePanel = sideScrollPanel.viewPort().panel().vertical();
		sidePanel.spacing().right(H_SPACE);
		Shell.instance().fire(this);
	}

	public int mainPanelWidth() {
		if (adHocWidth) {
			int width = mainPanel.width() - 4;
			if (width <= 0 || !hasSidePanel)
				width = rwidth() - 2 * H_SPACE
						- (hasSidePanel ? H_SPACE + WIDTH_SIDE_PANEL : 0);
			return width;
		}
		return width();
	}

	@Override
	public void onResize(int width, int height) {
		resizeSidePanel();
		if (cell0 != null)
			cell0.width(width());
	}

	private int width() {
		return rwidth() - WIDTH_SIDE_PANEL - 3 * H_SPACE;
	}

	public void detachSidePanel() {
		if (isDetached || adHocWidth)
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

	private void resizeSidePanel() {
		int maxFromDisplay;
		if (!size.defined()) {
			int offsetY = DisplayResizeAdapter.withDecrement(
					sideScrollPanel != null ? sideScrollPanel.offsetY()
							: mainPanel.offsetY(),
					Style.instance().embedded() ? 0 : 68);
			maxFromDisplay = Shell.instance().dheight() - offsetY - V_SPACE - 4;
		} else {
			maxFromDisplay = rheight() - V_SPACE + 9;
		}
		if (maxFromDisplay > 0) {
			if (resizeMainPanel)
				mainPanel.height(maxFromDisplay);
			if (sideScrollPanel != null)
				sideScrollPanel.height(maxFromDisplay + 3);
		}
	}

	protected IVerticalPanel addMainPanel(IVerticalPanel mainPanel) {
		return mainPanel;
	}

	public void reset() {
		panel.remove();
		init();
	}

	public void showSplit(boolean showSplit) {
	}
}

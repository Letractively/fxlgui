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
package co.fxl.gui.table.util.impl;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.ScrollPaneAdp;
import co.fxl.gui.table.util.api.ICompositeScrollPane;

public class CompositeScrollPaneImpl extends ScrollPaneAdp implements
		ICompositeScrollPane, IScrollListener {

	private IGridPanel grid;
	private IScrollPane center;
	private IScrollPane right;
	private IContainer rightContainer;
	private IContainer centerContainer;
	private IVerticalPanel dummy;
	private IAbsolutePanel panel;
	private int rightWidth;

	public CompositeScrollPaneImpl(IContainer c) {
		panel = c.panel().absolute().width(1.0).height(1.0);
		grid = panel.add().panel().grid().width(1.0).height(1.0).resize(3, 1);
		element = panel.add().scrollPane().width(1.0).height(1.0);
		addDummyImage(dummy = element.viewPort().panel().vertical());
		center = grid.cell(0, 0).scrollPane().scrollBars().never();
		right = grid.cell(1, 0).scrollPane().scrollBars().never();
		addDummyImage(grid.cell(2, 0).width(Env.HEIGHT_SCROLLBAR).panel()
				.vertical().width(Env.HEIGHT_SCROLLBAR));
		grid.column(2).width(Env.HEIGHT_SCROLLBAR);
		grid.column(0).expand();
		addScrollListener(this);
	}

	private void addDummyImage(IVerticalPanel dummy) {
		dummy.opacity(0).add().image().resource("empty_1x1.png");
	}

	@Override
	public ICompositeScrollPane size(int width, int height) {
		panel.size(width, height);
		grid.size(width, height);
		center.height(height);
		right.height(height);
		return (ICompositeScrollPane) super.size(width, height);
	}

	@Override
	public void onScroll(int maxOffset) {
		adjust(center, centerContainer, maxOffset);
		adjust(right, rightContainer, maxOffset);
	}

	private void adjust(IScrollPane s, IContainer c, int o) {
		int h = c.element().height();
		if (h == dummy.height()) {
			s.scrollTo(o);
		} else if (h > grid.height()) {
			o = Math.min(o, h - grid.height());
			s.scrollTo(o);
		}
	}

	@Override
	public IContainer right(int width) {
		rightWidth = width;
		return rightContainer = right.width(rightWidth).viewPort();
	}

	@Override
	public ICompositeScrollPane visible(boolean visible) {
		grid.column(1).width(rightWidth);
		grid.cell(1, 0).width(rightWidth);
		dummy.height(Math.max(centerContainer.element().height(),
				rightContainer.element().height()));
		return (ICompositeScrollPane) super.visible(visible);
	}

	@Override
	public IContainer viewPort() {
		return centerContainer = center.viewPort();
	}

}

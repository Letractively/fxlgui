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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.Display;
import co.fxl.gui.impl.Env;
import co.fxl.gui.impl.ScrollPaneAdp;
import co.fxl.gui.table.util.api.ICompositeScrollPane;

public class CompositeScrollPaneImpl extends ScrollPaneAdp implements
		ICompositeScrollPane, IScrollListener, Runnable {

	private IGridPanel panel;
	private IGridPanel grid;
	private IScrollPane center;
	private IScrollPane right;
	private IContainer rightContainer;
	private IContainer centerContainer;
	private IVerticalPanel dummy;

	public CompositeScrollPaneImpl(IContainer c) {
		panel = decorate(c).panel().grid().width(1.0).height(1.0);
		grid = panel.cell(0, 0).valign().begin().panel().grid().width(1.0)
				.height(1.0).resize(3, 1);
		panel.column(0).expand();
		element = panel.cell(1, 0).valign().begin().scrollPane()
				.width(Env.HEIGHT_SCROLLBAR).height(1.0);
		grid.margin().right(Env.HEIGHT_SCROLLBAR);
		addDummyImage(dummy = element.viewPort().panel().vertical());
		center = grid.cell(0, 0).valign().begin().scrollPane().scrollBars()
				.never();
		right = grid.cell(1, 0).valign().begin().scrollPane().scrollBars()
				.never();
		// addDummyImage(grid.cell(2, 0).width(Env.HEIGHT_SCROLLBAR).panel()
		// .vertical().width(Env.HEIGHT_SCROLLBAR));
		// grid.column(2).width(Env.HEIGHT_SCROLLBAR);
		grid.column(0).expand();
		addScrollListener(this);
	}

	protected IContainer decorate(IContainer c) {
		return c;
	}

	private void addDummyImage(IVerticalPanel dummy) {
		dummy.opacity(0).add().image().resource("empty_1x1.png");
	}

	@Override
	public ICompositeScrollPane size(int width, int height) {
		width(width);
		height(height);
		return this;
	}

	@Override
	public ICompositeScrollPane width(int width) {
		if (width != -1) {
			panel.width(width);
			grid.width(width - Env.HEIGHT_SCROLLBAR);
		}
		return this;
	}

	@Override
	public ICompositeScrollPane height(int height) {
		if (height != 0) {
			panel.height(height);
			grid.height(height);
			center.height(height);
			right.height(height);
			super.height(height);
		}
		return this;
	}

	@Override
	public void onScroll(int maxOffset) {
		adjust(center, centerContainer, maxOffset);
		adjust(right, rightContainer, maxOffset);
	}

	private void adjust(IScrollPane s, IContainer c, int o) {
		if (c == null || s == null)
			return;
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
		grid.column(1).width(width);
		grid.cell(1, 0).valign().begin().width(width);
		return rightContainer = right.width(width).viewPort();
	}

	@Override
	public ICompositeScrollPane visible(boolean visible) {
		Display.instance().invokeLater(this);
		return (ICompositeScrollPane) super.visible(visible);
	}

	@Override
	public void run() {
		int rh = rightContainer != null ? rightContainer.element().height() : 1;
		int lh = centerContainer.element().height();
		dummy.height(Math.max(lh, rh));
	}

	@Override
	public IContainer viewPort() {
		return centerContainer = center.viewPort();
	}

	@Override
	public ICompositeScrollPane update() {
		run();
		return this;
	}

}

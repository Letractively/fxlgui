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
package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IScrollListener;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.bulk.api.IBulkTableWidget;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.ITableListener;

public class Bulktable {

	private static final int HEIGHT = 600;
	private int numRows = HEIGHT / 28;
	private int numColumns = 8;

	private String random(int c, int i) {
		StringBuilder b = new StringBuilder("-");
		long l = c + i % 5;
		for (int j = 0; j < l; j++) {
			b.append("b" + j);
		}
		return b.toString();
	}

	public void onModuleLoad(IDisplay display) {
		display.visible(true);
		IVerticalPanel main = display.container().panel().vertical()
				.spacing(20);
		final ILabel trace = main.add().label();
		trace.font().weight().bold().pixel(20);
		IDockPanel dock = main.add().panel().dock();
		dock.height(HEIGHT);
		final IVerticalPanel p = dock.center().panel().vertical();
		IScrollPane sp = dock.right().scrollPane();
		sp.size(35, HEIGHT);
		IVerticalPanel v = sp.viewPort().panel().vertical();
		v.add().panel().horizontal().size(1, 1000000);
		paint(trace, p, 0);
		sp.addScrollListener(new IScrollListener() {
			@Override
			public void onScroll(int maxOffset) {
				paint(trace, p, maxOffset);
			}
		});
	}

	private void paint(final ILabel trace, final IVerticalPanel p, int maxOffset) {
		long s = System.currentTimeMillis();
		addContent(p.clear(), maxOffset);
		String title = numColumns + "x" + numRows + " rendered in "
				+ (System.currentTimeMillis() - s) + "ms";
		trace.text(title);
	}

	private void addContent(IVerticalPanel main, int maxOffset) {
		final IBulkTableWidget grid = (IBulkTableWidget) main.add().widget(
				IBulkTableWidget.class);
		numRows = grid.height(HEIGHT);
		for (int c = 0; c < numColumns; c++) {
			grid.column(c).title("Column " + c);
		}
		for (int r = 0; r < numRows; r++)
			for (int c = 0; c < numColumns; c++) {
				grid.cell(c, r).text((maxOffset + r) + ": " + random(c, r));
			}
		grid.visible(true);
		grid.addTableListener(new ITableListener() {

			@Override
			public void onClick(int column, int row) {
				if (row != 0)
					grid.row(row).highlight(true);
			}
		});
		grid.addTableListener(new ITableListener() {

			@Override
			public void onClick(int column, int row) {
				if (row != 0)
					grid.row(row).highlight(false);
			}
		}).ctrlPressed();
	}
}

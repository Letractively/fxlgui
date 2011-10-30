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
package co.fxl.gui.table.util.impl;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.api.ILazyScrollPane;

public class LazyScrollPaneImpl implements ILazyScrollPane, IScrollListener {

	// TODO BUG/Usability: sometimes / under certain circumstances: an expand
	// shifts the displayed range upwards

	public static int WIDTH_SCROLL_PANEL = 35;
	public static final int HEIGHT_SCROLL_BAR = 17;
	private int widthScrollPanel = WIDTH_SCROLL_PANEL;
	private IDecorator decorator;
	private int minRowHeight = 22;
	private int height = 400;
	private IContainer container;
	private ICardPanel treeScrollPanelContainer;
	private int rowIndex = 0;
	private IContainer lastCard = null;
	private IAbsolutePanel scrollContentPanel;
	private int size;
	private int rows2Paint = height / minRowHeight;
	private IScrollPane scrollPane;
	int maxRowIndex;
	int maxOffset;
	private int lastIndex;
	private boolean horizontalScrollPane = false;
	private boolean adjustHeights = true;
	private int width = -1;
	private IVerticalPanel v;
	private IScrollPane treeScrollPanel;
	private IDockPanel treeDockPanel;
	private boolean allowRepaint = false;
	private int[] rowHeights;
	private int heightEstimate = 0;
	private boolean holdScroll;

	LazyScrollPaneImpl(IContainer container) {
		this.container = container;
	}

	@Override
	public ILazyScrollPane decorator(IDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	@Override
	public ILazyScrollPane minRowHeight(int minRowHeight) {
		this.minRowHeight = minRowHeight;
		if (size > 0)
			heightEstimate = size * minRowHeight;
		return updateRows2Paint();
	}

	private ILazyScrollPane updateRows2Paint() {
		rows2Paint = height / minRowHeight;
		if (rows2Paint > size)
			rows2Paint = size;
		return this;
	}

	@Override
	public ILazyScrollPane height(int height) {
		this.height = height;
		return updateRows2Paint();
	}

	@Override
	public ILazyScrollPane rowIndex(int rowIndex) {
		assert rowIndex >= 0;
		this.rowIndex = rowIndex;
		return this;
	}

	@Override
	public int rowIndex() {
		return rowIndex;
	}

	@Override
	public ILazyScrollPane size(int size) {
		this.size = size;
		if (minRowHeight > 0)
			heightEstimate = size * minRowHeight;
		rowHeights = new int[size];
		return updateRows2Paint();
	}

	@Override
	public ILazyScrollPane adjustHeights(boolean adjustHeights) {
		this.adjustHeights = adjustHeights;
		return this;
	}

	@Override
	public ILazyScrollPane visible(boolean visible) {
		if (visible) {
			allowRepaint = true;
			draw();
		} else {
			v.visible(false);
		}
		return this;
	}

	int rowHeight(int i) {
		int rowHeight = decorator.rowHeight(i);
		if (adjustHeights) {
			if (rowHeights[i] == 0)
				rowHeights[i] = minRowHeight;
			if (rowHeight > rowHeights[i]) {
				int inc = rowHeight - rowHeights[i];
				rowHeights[i] = rowHeight;
				heightEstimate += inc;
				updateScrollPanelHeight();
			}
		}
		return rowHeight;
	}

	private void draw() {
		v = container.panel().vertical();
		v.color().white();
		treeDockPanel = v.add().panel().dock();
		if (!adjustHeights) {
			treeDockPanel.height(height);
		}
		if (size == 0) {
			treeDockPanel.left().panel().vertical().spacing(16).add().label()
					.text("NO ENTITIES FOUND").font().pixel(10).color().gray();

			// TODO FEATURE: Usability: nice 2 have: show filter details like in
			// scrolltable

			return;
		}
		treeDockPanel.visible(false);
		treeDockPanel.height(height);
		treeScrollPanelContainer = treeDockPanel.center().panel().card();
		IContainer ctr = treeDockPanel.right();
		boolean inc = !horizontalScrollPane;
		if (inc)
			ctr = ctr.panel().vertical().addSpace(7).add();
		scrollPane = ctr.scrollPane();
		scrollPane.size(widthScrollPanel, height);
		scrollContentPanel = scrollPane.viewPort().panel().absolute();
		scrollContentPanel.add().label().text("&#160;");
		updateScrollPanelHeight();
		final int firstIndex = size - rows2Paint;
		assert firstIndex >= 0;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (!v.visible())
					return;
				assert firstIndex >= 0;
				if (adjustHeights && !decorator.checkIndex(lastIndex)) {
					if (allowRepaint) {
						allowRepaint = false;
						draw();
					}
					return;
				}
				int h = adjustHeights ? rowHeight(lastIndex) : minRowHeight;
				assert lastIndex >= 0;
				maxRowIndex = lastIndex;
				for (int i = lastIndex - 1; i >= firstIndex && h < height; i--) {
					if (adjustHeights && !decorator.checkIndex(lastIndex)) {
						if (allowRepaint) {
							allowRepaint = false;
							draw();
						}
						return;
					}
					int rowHeight = Math.max(minRowHeight,
							adjustHeights ? rowHeight(i) : minRowHeight);
					h += rowHeight;
					if (h < height) {
						maxRowIndex = i;
					}
				}
				assert maxRowIndex >= 0;
				if (maxRowIndex == 0) {
					scrollPane.remove();
					widthScrollPanel = 0;
				} else {
					// if (maxRowIndex < size - 1) {
					// maxRowIndex++;
					// }
				}
				if (rowIndex > maxRowIndex)
					rowIndex = maxRowIndex;
				if (adjustHeights) {
					scrollPane.addScrollListener(LazyScrollPaneImpl.this);
					treeDockPanel.display().invokeLater(new Runnable() {

						@Override
						public void run() {
							if (rowIndex > 0) {
								scrollToRowIndex();
							} else
								update();
							treeDockPanel.visible(true);
						}
					});
				} else {
					if (rowIndex > 0) {
						scrollToRowIndex();
					}
					scrollPane.addScrollListener(LazyScrollPaneImpl.this);
					update();
					treeDockPanel.visible(true);
				}
				allowRepaint = false;
			}

			private void scrollToRowIndex() {
				int y = convertRowIndex2ScrollOffset(rowIndex);
				if (lastCard != null)
					lastCard.clear();
				treeDockPanel.visible(true);
				scrollPane.scrollTo(y);
				treeDockPanel.visible(false);
				// TODO style="overflow:auto" on body element
				// FocusPanel around Widget to scroll into view
			}
		};
		if (adjustHeights) {
			update(firstIndex);
			treeDockPanel.display().invokeLater(runnable);
		} else {
			lastIndex = size - 1;
			if (lastIndex < 0)
				lastIndex = 0;
			runnable.run();
		}
	}

	private void updateScrollPanelHeight() {
		if (heightEstimate == scrollContentPanel.height())
			return;
		holdScroll = true;
		scrollContentPanel.size(1, heightEstimate);
		maxOffset = heightEstimate - height;
		if (maxOffset <= 0)
			maxOffset = 1;
		holdScroll = false;
	}

	private void update() {
		assert rowIndex >= 0;
		update(rowIndex);
	}

	private int update(int rowIndex) {
		setLastIndex(rowIndex);
		IContainer invisibleCard = treeScrollPanelContainer.add();
		IContainer c = invisibleCard;

		// TODO BUG: after switch to Detail-View: horizontal
		// scrollpane is not displayed for tree.
		// required is a click in tree or scrolling to surface

		if (horizontalScrollPane) {
			treeScrollPanel = c.scrollPane();
			if (width != -1)
				treeScrollPanel.width(width - widthScrollPanel);
			c = treeScrollPanel.horizontal().viewPort();
		}
		decorator.decorate(c, rowIndex, lastIndex);
		treeScrollPanelContainer.show(invisibleCard.element());
		if (lastCard != null) {
			lastCard.clear();
			lastCard.element().remove();
		}
		lastCard = invisibleCard;
		return lastIndex;
	}

	@Override
	public ILazyScrollPane width(int width) {
		this.width = width;
		if (width != -1 && treeScrollPanel != null) {
			treeDockPanel.width(width);
			treeScrollPanelContainer.width(width - widthScrollPanel);
			treeScrollPanel.width(width - widthScrollPanel);
		}
		return this;
	}

	private void setLastIndex(int rowIndex) {
		lastIndex = rowIndex + rows2Paint - 1;
		if (lastIndex >= size)
			lastIndex = size - 1;
		if (lastIndex < 0)
			lastIndex = 0;
	}

	int convertScrollOffset2RowIndex(int offset) {
		double r = offset;
		r /= maxOffset;
		r *= maxRowIndex;
		return (int) Math.round(r);
	}

	int convertRowIndex2ScrollOffset(int rowIndex) {
		double r = rowIndex;
		r *= maxOffset;
		r /= maxRowIndex;
		int round = (int) Math.round(r);
		return round;
	}

	@Override
	public void onScroll(int maxOffset) {
		if (holdScroll)
			return;
		int newRowIndex = convertScrollOffset2RowIndex(maxOffset);
		if (newRowIndex == rowIndex)
			return;
		rowIndex = convertScrollOffset2RowIndex(maxOffset);
		assert rowIndex >= 0 : maxOffset + " offset not valid: " + rowIndex;
		update();
	}

	@Override
	public void onUp(int turns) {
		scrollPane.onUp(turns);
	}

	@Override
	public void onDown(int turns) {
		scrollPane.onDown(turns);
	}

	@Override
	public ILazyScrollPane horizontalScrollPane(boolean horizontalScrollPane) {
		this.horizontalScrollPane = horizontalScrollPane;
		return this;
	}

	@Override
	public ILazyScrollPane refresh() {
		update(rowIndex);
		return this;
	}

	@Override
	public void remove() {
		container.clear();
	}
}

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
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.filter.api.IFilterConstraints;
import co.fxl.gui.impl.Display;
import co.fxl.gui.table.scroll.impl.ScrollTableWidgetImpl;
import co.fxl.gui.table.util.api.IDragDropListener;
import co.fxl.gui.table.util.api.ILazyScrollPane;
import co.fxl.gui.table.util.api.IUpDownIndex;
import co.fxl.gui.table.util.impl.DragAndDrop.DragAndDropAdapter;

public class LazyScrollPaneImpl implements ILazyScrollPane, IScrollListener,
		DragAndDropAdapter {

	// TODO Usability: Relation-Register-Easy-Add: Klick auf Add: untere Hälfte
	// von Relation-Register via Split-Pane unterteilt, Filter oben gilt für
	// beide Sichten, untere Sicht enthält alle nicht-assoziierten Objekte
	// (Adapter.nonValueOf). Gibt Buttons dazwischen mit hoch/runter
	// verschieben, geht aber auch mit drag und drop. eventuell lassen sich hier
	// auch working-sets verwenden

	// TODO BUG/Usability: sometimes / under certain circumstances: an expand
	// shifts the displayed range upwards

	// TODO Look: IScrollPane.getScrollbar:
	// IScrollPane.IScrollbar: als widget: html5-implementierung mit d&d und
	// g-like-design, fallback (IE, etc.): alte implementierung

	public static int WIDTH_SCROLL_PANEL = 35;
	public static final int HEIGHT_SCROLL_BAR = 17;
	private static final int BLOCK_INCREMENT = 22;
	private static final int HEIGHT_CORRECTION = 7;
	private int widthScrollPanel = WIDTH_SCROLL_PANEL;
	IDecorator decorator;
	private int minRowHeight = 22;
	private int height = 400;
	private IContainer container;
	private IVerticalPanel treeScrollPanelContainer;
	int rowIndex = 0;
	private IAbsolutePanel scrollContentPanel;
	private int size;
	private int rows2Paint = height / minRowHeight;
	private IScrollPane scrollPane;
	int maxRowIndex;
	int maxOffset;
	int lastIndex;
	private boolean horizontalScrollPane = false;
	private boolean adjustHeights = true;
	private int width = -1;
	private IFocusPanel v;
	private IScrollPane treeScrollPanel;
	private IPanel<?> treeDockPanel;
	private boolean allowRepaint = false;
	private int[] rowHeights;
	private int heightEstimate = 0;
	private boolean holdScroll;
	boolean allowInsertUnder;
	IDragDropListener dragDropListener;
	private IUpDownIndex upDownIndex;

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

	@Override
	public int rowHeight(int i) {
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
		hasScrollbar = true;
		v = container.panel().focus();
		v.color().white();
		if (dragDropListener != null)
			new DragAndDrop(this, v);
		treeDockPanel = (IPanel<?>) v.add().panel().grid();
		if (!adjustHeights) {
			treeDockPanel.height(height);
		}
		if (size == 0) {
			IVerticalPanel l = ((IGridPanel) treeDockPanel).cell(0, 0).valign().begin().panel().vertical()
					.spacing(16);
			ScrollTableWidgetImpl.addNoEntitiesFound(l, false, constraints,
					filterQueryLabel);

			// TODO Feature: Usability: nice 2 have: show filter details like in
			// scrolltable

			return;
		}
		
//		if (size <= 50) {
//			// TODO ...
//			return;
//		}
		
		treeDockPanel.visible(false);
		treeDockPanel.height(height);
		treeScrollPanelContainer = ((IGridPanel) treeDockPanel).cell(0, 0).valign().begin().panel()
				.vertical();
		treeScrollPanelContainer.height(height);
		IContainer ctr = ((IGridPanel) treeDockPanel).cell(1, 0).width(widthScrollPanel);
		scrollPane = ctr.scrollPane();
		scrollPane.size(widthScrollPanel, height);
		scrollContentPanel = scrollPane.viewPort().panel().absolute();
		scrollContentPanel.add().label().text("&#160;");
		((IGridPanel) treeDockPanel).column(0).expand();
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
				treeDockPanel.visible(true);
				int h = hasHeader ? decorator.headerHeight() : 0;
				h += adjustHeights ? rowHeight(lastIndex) : minRowHeight;
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
				treeDockPanel.visible(false);
				assert maxRowIndex >= 0;
				if (maxRowIndex == 0) {
					scrollPane.remove();
					hasScrollbar = false;
					widthScrollPanel = 0;
				}
				if (rowIndex > maxRowIndex)
					rowIndex = maxRowIndex;
				if (adjustHeights) {
					scrollPane.addScrollListener(LazyScrollPaneImpl.this);
					v.height(height);
					updateScrollPanelHeight();
					forkScrollToRowIndex();
					update(false);
					treeDockPanel.visible(true);
				} else {
					forkScrollToRowIndex();
					scrollPane.addScrollListener(LazyScrollPaneImpl.this);
					update(false);
					treeDockPanel.visible(true);
				}
				allowRepaint = false;
			}

			private void forkScrollToRowIndex() {
				if (rowIndex > 0) {
					Display.instance().invokeLater(new Runnable() {
						@Override
						public void run() {
							holdScroll = true;
							int y = convertRowIndex2ScrollOffset(rowIndex);
							scrollPane.scrollTo(y);
							// TODO style="overflow:auto" on body element
							// FocusPanel around Widget to scroll into view
							holdScroll = false;
						}
					});
				}
			}
		};
		if (adjustHeights) {
			update(firstIndex, true);
			v.height(height + HEIGHT_CORRECTION);
			runnable.run();
		} else {
			lastIndex = size - 1;
			if (lastIndex < 0)
				lastIndex = 0;
			runnable.run();
		}
	}

	private void scrollToIndex(int index) {
		rowIndex = index;
		int y = convertRowIndex2ScrollOffset(rowIndex);
		scrollPane.scrollTo(y);
		refresh();
	}

	boolean hasHeader = true;
	private boolean hasScrollbar = true;
	protected IFilterConstraints constraints;
	protected String filterQueryLabel;

	public void addKeyListeners(final IKeyRecipient<?> v) {
		if (v == null)
			return;
		v.addKeyListener(new IClickListener() {
			@Override
			public void onClick() {
				onKeyUp();
			}
		}).up();
		v.addKeyListener(new IClickListener() {
			@Override
			public void onClick() {
				onKeyDown();
			}
		}).down();
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
		update(false);
	}

	private void update(boolean isCalibration) {
		assert rowIndex >= 0;
		update(rowIndex, isCalibration);
	}

	private int update(int rowIndex, boolean isCalibration) {
		// hack for swing
		holdScroll = true;
		setLastIndex(rowIndex);
		int storeLastIndex = lastIndex;
		treeScrollPanelContainer.clear();
		// hack for swing
		treeScrollPanelContainer.add().label().remove();
		IContainer invisibleCard = treeScrollPanelContainer.add();
		IContainer c = invisibleCard;

		// TODO BUG: after switch to Detail-View: horizontal
		// scrollpane is not displayed for tree.
		// required is a click in tree or scrolling to surface

		if (horizontalScrollPane) {
			treeScrollPanel = c.scrollPane();
			treeScrollPanel.height(height);
			if (width != -1)
				treeScrollPanel.width(width - widthScrollPanel);
			c = treeScrollPanel.horizontal().viewPort();
		}
		assert storeLastIndex == lastIndex : "last index changed concurrently: "
				+ lastIndex + "!=" + storeLastIndex;
		assert lastIndex >= rowIndex : "illegal range " + rowIndex + "-"
				+ lastIndex;
		IKeyRecipient<Object> e = decorator.decorate(c, rowIndex, lastIndex,
				isCalibration);
		if (!isCalibration)
			addKeyListeners(e);
		// treeScrollPanelContainer.flip();
		holdScroll = false;
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
		onMouseWheel(-turns);
	}

	private void onMouseWheel(int turns) {
		int newOffset = scrollPane.scrollOffset() + turns * BLOCK_INCREMENT;
		newOffset = Math.max(0, newOffset);
		newOffset = Math.min(newOffset,
				convertRowIndex2ScrollOffset(maxRowIndex));
		scrollPane.scrollTo(newOffset);
	}

	@Override
	public void onDown(int turns) {
		onMouseWheel(+turns);
	}

	@Override
	public ILazyScrollPane horizontalScrollPane(boolean horizontalScrollPane) {
		this.horizontalScrollPane = horizontalScrollPane;
		return this;
	}

	@Override
	public ILazyScrollPane refresh() {
		update(rowIndex, false);
		return this;
	}

	@Override
	public void refreshNow() {
		refresh();
	}

	@Override
	public void remove() {
		container.clear();
	}

	@Override
	public int scrollPosition() {
		return scrollPane.scrollOffset();
	}

	@Override
	public ILazyScrollPane scrollPosition(int scrollPosition) {
		scrollPane.scrollTo(scrollPosition);
		return this;
	}

	@Override
	public ILazyScrollPane dragDropListener(boolean allowInsertUnder,
			IDragDropListener l) {
		this.allowInsertUnder = allowInsertUnder;
		this.dragDropListener = l;
		return this;
	}

	@Override
	public ILazyScrollPane hasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
		return this;
	}

	@Override
	public ILazyScrollPane upDownIndex(IUpDownIndex index) {
		this.upDownIndex = index;
		return this;
	}

	void onKeyUp() {
		if (upDownIndex != null) {
			if (upDownIndex.selectionIndex() == 0)
				return;
			upDownIndex.selectionIndex(upDownIndex.selectionIndex() - 1);
			if (upDownIndex.selectionIndex() == rowIndex - 1) {
				rowIndex--;
				scrollToIndex(rowIndex);
			} else {
				update();
			}
		} else {
			if (rowIndex > 0) {
				scrollToIndex(rowIndex - 1);
			} else {
				update();
			}
		}
	}

	void onKeyDown() {
		if (upDownIndex != null) {
			if (upDownIndex.selectionIndex() == size - 1)
				return;
			upDownIndex.selectionIndex(upDownIndex.selectionIndex() + 1);
			if (upDownIndex.selectionIndex() == rowIndex + 1) {
				rowIndex++;
			}
			scrollToIndex(rowIndex);
		} else {
			if (rowIndex < maxRowIndex) {
				scrollToIndex(rowIndex + 1);
			}
		}
	}

	@Override
	public boolean hasScrollbar() {
		return hasScrollbar;
	}

	@Override
	public IDragDropListener dragDropListener() {
		return dragDropListener;
	}

	@Override
	public IDecorator decorator() {
		return decorator;
	}

	@Override
	public boolean hasHeader() {
		return hasHeader;
	}

	@Override
	public int lastIndex() {
		return lastIndex;
	}

	@Override
	public boolean allowInsertUnder() {
		return allowInsertUnder;
	}

	@Override
	public ILazyScrollPane constraints(IFilterConstraints constraints,
			String filterQueryLabel) {
		this.constraints = constraints;
		this.filterQueryLabel = filterQueryLabel;
		return this;
	}
}

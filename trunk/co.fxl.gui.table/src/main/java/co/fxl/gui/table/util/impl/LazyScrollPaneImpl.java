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
import co.fxl.gui.api.IDraggable.IDragStartListener;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IKeyRecipient;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPoint;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.table.util.api.IDragDropListener;
import co.fxl.gui.table.util.api.IDragDropListener.Where;
import co.fxl.gui.table.util.api.ILazyScrollPane;
import co.fxl.gui.table.util.api.IUpDownIndex;

public class LazyScrollPaneImpl implements ILazyScrollPane, IScrollListener {

	// TODO FEATURE-COMPLETE: NICE2HAVE: KEYS: Usability: Up/Down-Tasten in LazyScrollPane
	// intercepten und hoch und
	// runter navigieren (simulierter klick auf das element darüber/darunter)
	// [muss entschieden werden wo fokus liegen soll, zurzeit immer in der form:
	// möglichkeit: nach new, liegt in form, klick in tree: liegt in tree, nach
	// initialen wechseln in detail-sicht: liegt in form]

	// TODO BUG/Usability: sometimes / under certain circumstances: an expand
	// shifts the displayed range upwards

	// TODO Look: IScrollPane.getScrollbar:
	// IScrollPane.IScrollbar: als widget: html5-implementierung mit d&d und
	// g-like-design, fallback (IE, etc.): alte implementierung

	public static int WIDTH_SCROLL_PANEL = 35;
	public static final int HEIGHT_SCROLL_BAR = 17;
	private static final int BLOCK_INCREMENT = 22;
	private static final int HEIGHT_CORRECTION = 7;
	// public static boolean USE_DOCK_PANEL = false;
	private int widthScrollPanel = WIDTH_SCROLL_PANEL;
	private IDecorator decorator;
	private int minRowHeight = 22;
	private int height = 400;
	private IContainer container;

	// previously: FlipPage
	private IVerticalPanel treeScrollPanelContainer;
	private int rowIndex = 0;
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
	private IFocusPanel v;
	private IScrollPane treeScrollPanel;
	private IPanel<?> treeDockPanel;
	private boolean allowRepaint = false;
	private int[] rowHeights;
	private int heightEstimate = 0;
	private boolean holdScroll;
	private boolean allowInsertUnder;
	private IDragDropListener dragDropListener;
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
		hasScrollbar = true;
		v = container.panel().focus();
		v.color().white();
		// addDragListener(v);
		ILayout layout = v.add().panel();
		treeDockPanel = getPanel(layout);
		if (!adjustHeights) {
			treeDockPanel.height(height);
		}
		if (size == 0) {
			leftPanel(treeDockPanel).panel().vertical().spacing(16).add()
					.label().text("NO ENTITIES FOUND").font().pixel(10).color()
					.gray();

			// TODO FEATURE: Usability: nice 2 have: show filter details like in
			// scrolltable

			return;
		}
		treeDockPanel.visible(false);
		treeDockPanel.height(height);
		treeScrollPanelContainer = centerPanel(treeDockPanel).panel()
				.vertical();
		treeScrollPanelContainer.height(height);
		IContainer ctr = rightPanel(treeDockPanel);
		// boolean inc = !horizontalScrollPane;
		// if (inc)
		// ctr = ctr.panel().vertical().addSpace(7).add();
		scrollPane = ctr.scrollPane();
		scrollPane.size(widthScrollPanel, height);
		scrollContentPanel = scrollPane.viewPort().panel().absolute();
		scrollContentPanel.add().label().text("&#160;");
		// if (!USE_DOCK_PANEL)
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
				} else {
					// if (maxRowIndex < size - 1) {
					// maxRowIndex++;
					// }
				}
				if (rowIndex > maxRowIndex)
					rowIndex = maxRowIndex;
				if (adjustHeights) {
					scrollPane.addScrollListener(LazyScrollPaneImpl.this);
					Runnable r = new Runnable() {

						@Override
						public void run() {
							v.height(height);
							updateScrollPanelHeight();
							if (rowIndex > 0) {
								holdScroll = true;
								scrollToRowIndex();
								holdScroll = false;
							}
							// else
							update(false);
							treeDockPanel.visible(true);
						}
					};
					r.run();
					// treeDockPanel.display().invokeLater(r);
				} else {
					if (rowIndex > 0) {
						scrollToRowIndex();
					}
					scrollPane.addScrollListener(LazyScrollPaneImpl.this);
					update(false);
					treeDockPanel.visible(true);
				}
				allowRepaint = false;
			}

			private void scrollToRowIndex() {
				int y = convertRowIndex2ScrollOffset(rowIndex);
				treeDockPanel.visible(true);
				scrollPane.scrollTo(y);
				treeDockPanel.visible(false);
				// TODO style="overflow:auto" on body element
				// FocusPanel around Widget to scroll into view
			}
		};
		if (adjustHeights) {
			update(firstIndex, true);
			v.height(height + HEIGHT_CORRECTION);
			runnable.run();
			// treeDockPanel.display().invokeLater(runnable);
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

	private int dragIndex = -1;
	private boolean hasHeader = true;
	private boolean hasScrollbar = true;

	private void addDragListener(final IFocusPanel v) {
		if (dragDropListener == null)
			return;
		// v.addDragOverListener(new IDragMoveListener() {
		//
		// private Integer inc = null;
		//
		// private void start(int offsetY) {
		// if (inc == null) {
		// inc = getInc(offsetY);
		// runAndSchedule();
		// }
		// }
		//
		// private void runAndSchedule() {
		// if (inc == null)
		// return;
		// if (inc < 0)
		// onKeyUp();
		// else
		// onKeyDown();
		// if (inc != null)
		// Display.instance().invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		// runAndSchedule();
		// }
		// }, 200);
		// }
		//
		// private int getInc(int offsetY) {
		// if (offsetY < 0) {
		// return -1;
		// } else {
		// return 1;
		// }
		// }
		//
		// private void end(int offsetY) {
		// inc = null;
		// }
		//
		// @Override
		// public void onDragOver(IPoint point) {
		// end(point.offsetY());
		// }
		//
		// @Override
		// public void onDragOut(IPoint point) {
		// start(point.offsetY());
		// }
		//
		// });
		v.addDragStartListener(new IDragStartListener() {

			@Override
			public void onDragStart(IDragStartEvent event) {
				int y = event.offsetY();
				dragIndex = getIndex(y);
				if (dragIndex != -1) {
					IElement<?> element = decorator.elementAt(dragIndex);
					event.dragImage(element);
				}
			}

			@Override
			public void onDragEnd() {
				dragIndex = -1;
			}
		});
		v.addDropListener(new IDropListener() {

			@Override
			public void onDropOn(IPoint point) {
				if (dragIndex == -1)
					return;
				int offsetY = point.offsetY();
				int index = getIndex(offsetY);
				if (index != -1 && dragDropListener.allowsDrop(index)) {
					boolean insertUnder = allowInsertUnder
							&& point.offsetX() > v.width() / 2;
					Where where = Where.UNDER;
					if (!insertUnder) {
						int[] rangeAndRowHeight = getRangeAndRowHeight(offsetY);
						if (offsetY <= rangeAndRowHeight[0]
								+ rangeAndRowHeight[1] / 2)
							where = Where.BEFORE;
						else
							where = Where.AFTER;
					}
					dragDropListener.drop(dragIndex, index, where,
							new CallbackTemplate<Void>() {
								@Override
								public void onSuccess(Void result) {
									refresh();
								}
							});
				}
			}
		});
	}

	public void addKeyListeners(final IKeyRecipient<?> v) {
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

	private IContainer rightPanel(IPanel<?> tdp) {
		// if (USE_DOCK_PANEL) {
		// return ((IDockPanel) tdp).right();
		// } else {
		return ((IGridPanel) tdp).cell(1, 0).width(widthScrollPanel);
		// }
	}

	private IContainer centerPanel(IPanel<?> tdp) {
		// if (USE_DOCK_PANEL) {
		// return ((IDockPanel) tdp).center();
		// } else {
		return ((IGridPanel) tdp).cell(0, 0).valign().begin();
		// }
	}

	private IContainer leftPanel(IPanel<?> tdp) {
		// if (USE_DOCK_PANEL) {
		// return ((IDockPanel) tdp).left();
		// } else {
		return ((IGridPanel) tdp).cell(0, 0).valign().begin();
		// }
	}

	public IPanel<?> getPanel(ILayout layout) {
		// if (USE_DOCK_PANEL)
		// return layout.dock();
		// else {
		IGridPanel grid = layout.grid();
		return grid;
		// }
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
		setLastIndex(rowIndex);
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
		IKeyRecipient<Object> e = decorator.decorate(c, rowIndex, lastIndex,
				isCalibration);
		if (!isCalibration)
			addKeyListeners(e);
		// treeScrollPanelContainer.flip();
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

	public int getIndex(int y) {
		int index = rowIndex;
		int range = hasHeader ? decorator.headerHeight() : 0;
		if (y <= range)
			return 0;
		for (; index <= lastIndex; index++) {
			int rowHeight = rowHeight(index);
			if (y >= range && y <= range + rowHeight) {
				return index;
			} else {
				range += rowHeight;
			}
		}
		return -1;
	}

	public int[] getRangeAndRowHeight(int y) {
		int index = rowIndex;
		int range = hasHeader ? decorator.headerHeight() : 0;
		for (; index <= lastIndex; index++) {
			int rowHeight = rowHeight(index);
			if (y >= range && y <= range + rowHeight) {
				return new int[] { range, rowHeight };
			} else {
				range += rowHeight;
			}
		}
		throw new MethodNotImplementedException();
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
}

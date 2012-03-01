package co.fxl.gui.table.util.impl;

import co.fxl.gui.api.IDraggable.IDragStartListener;
import co.fxl.gui.api.IDropTarget.IDragEvent;
import co.fxl.gui.api.IDropTarget.IDragMoveListener;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IPoint;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.table.util.api.IDragDropListener.Where;

class DragAndDrop implements IDragStartListener, IDropListener,
		IDragMoveListener {

	private LazyScrollPaneImpl pane;
	private IFocusPanel focusPanel;
	private int dragIndex;
	private int overIndex = -1;
	private Where where = null;

	DragAndDrop(LazyScrollPaneImpl pane, IFocusPanel focusPanel) {
		this.pane = pane;
		this.focusPanel = focusPanel;
		focusPanel.addDragStartListener(this);
		focusPanel.addDragOverListener(this);
		focusPanel.addDropListener(this);
	}

	private int getIndex(int y) {
		int index = pane.rowIndex;
		int range = pane.hasHeader ? pane.decorator.headerHeight() : 0;
		if (y <= range)
			return 0;
		for (; index <= pane.lastIndex; index++) {
			int rowHeight = pane.rowHeight(index);
			if (y >= range && y <= range + rowHeight) {
				return index;
			} else {
				range += rowHeight;
			}
		}
		return -1;
	}

	@Override
	public void onDragStart(IDragStartEvent event) {
		int y = event.offsetY();
		dragIndex = getIndex(y);
		if (dragIndex != -1) {
			IElement<?>[] element = pane.decorator.elementsAt(dragIndex
					- pane.rowIndex);
			event.dragImage(element[0]);
		}
	}

	@Override
	public void onDragEnd() {
		dragIndex = -1;
		overIndex = -1;
	}

	@Override
	public void onDropOn(IDragEvent point) {
		int index = getIndex(point);
		if (allowsDrop(index)) {
			Where where = getWhere(point);
			pane.dragDropListener.drop(dragIndex, index, where,
					new CallbackTemplate<Void>() {
						@Override
						public void onSuccess(Void result) {
							pane.refresh();
						}
					});
		}
	}

	boolean allowsDrop(int index) {
		if (dragIndex == -1)
			return false;
		return index != -1 && pane.dragDropListener.allowsDrop(index);
	}

	int getIndex(IPoint point) {
		int offsetY = point.offsetY();
		int index = getIndex(offsetY);
		return index;
	}

	Where getWhere(IPoint point) {
		int offsetY = point.offsetY();
		boolean insertUnder = pane.allowInsertUnder
				&& point.offsetX() > focusPanel.width() / 2;
		Where where = Where.UNDER;
		if (!insertUnder) {
			int[] rangeAndRowHeight = getRangeAndRowHeight(offsetY);
			if (offsetY <= rangeAndRowHeight[0] + rangeAndRowHeight[1] / 2)
				where = Where.BEFORE;
			else
				where = Where.AFTER;
		}
		return where;
	}

	private int[] getRangeAndRowHeight(int y) {
		int index = pane.rowIndex;
		int range = pane.hasHeader ? pane.decorator.headerHeight() : 0;
		for (; index <= pane.lastIndex; index++) {
			int rowHeight = pane.rowHeight(index);
			if (y >= range && y <= range + rowHeight) {
				return new int[] { range, rowHeight };
			} else {
				range += rowHeight;
			}
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void onDragOver(IDragEvent point) {
		int index = getIndex(point);
		if (index == dragIndex)
			return;
		if (!allowsDrop(index))
			return;
		if (overIndex != -1 && overIndex == index) {
			return;
		}
		if (overIndex != -1)
			pane.dragDropListener.out(
					pane.decorator.elementsAt(overIndex - pane.rowIndex),
					dragIndex, overIndex, where);
		overIndex = index;
		where = getWhere(point);
		if (dragIndex == overIndex)
			return;
		if (where.equals(Where.AFTER) && dragIndex - 1 == overIndex)
			return;
		if (where.equals(Where.BEFORE) && dragIndex + 1 == overIndex)
			return;
		pane.dragDropListener.over(
				pane.decorator.elementsAt(overIndex - pane.rowIndex),
				dragIndex, overIndex, where);
	}

	@Override
	public void onDragOut(IDragEvent point) {
	}
}

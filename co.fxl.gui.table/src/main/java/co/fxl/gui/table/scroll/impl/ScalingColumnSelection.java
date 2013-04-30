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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDraggable.IDragStartListener;
import co.fxl.gui.api.IDropTarget.IDragEvent;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.HorizontalScalingPanel;

public class ScalingColumnSelection {

	private static final String COLUMN = "Column";
	private static final String COLUMN_SELECTION = "Column Selection";
	TableWidgetAdp widget;
	private ScrollTableColumnImpl dragged;
	private boolean allowsEmptySelection;

	// private ScrollTableColumnImpl dummy;
	// private IFocusPanel dummyFocusPanel;
	// private ILabel dummyLabel;

	// TODO FEATURE: MINOR: : Option: Look: Column-Selection: if > n
	// Columns or
	// sum(characters of column-headers) > m
	// then dynamically resize font size of column selection labels

	public ScalingColumnSelection(final TableWidgetAdp widget,
			boolean allowsEmptySelection) {
		this.widget = widget;
		// dummy = new ScrollTableColumnImpl(widget, -1);
		// dummy.name("");
		draw();
		this.allowsEmptySelection = allowsEmptySelection;
	}

	public void draw() {
		IVerticalPanel vertical = getContainer().panel().vertical();
		IContainer clear = vertical.add();
		int width = vertical.width();
		final HorizontalScalingPanel p = new HorizontalScalingPanel(clear);
		p.hspace(4);
		IClickListener clickListener = new IClickListener() {
			@Override
			public void onClick() {
				widget.updateTable();
			}
		};
		addToPanel(p, clickListener);
		p.width(width);
		// if (widget.nextTimeShowPopUp()) {
		// widget.nextTimeShowPopUp(false);
		// Display.instance().invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// p.onClick();
		// }
		// });
		// }
	}

	protected IContainer getContainer() {
		if (widget.statusPanel() != null) {
			IGridCell center = widget.statusPanel().cell(1, 0).clear().align()
					.begin().valign().center();
			widget.statusPanel().column(1).expand();
			return center;
		}
		return widget.getContainer();
	}

	void addToPanel(final HorizontalScalingPanel p,
			final IClickListener clickListener) {
		ColumnSelection.addTitle(widget, p.add());
		p.addSpace(4);
		for (final ScrollTableColumnImpl c : widget.columnList()) {
			IFocusPanel fp = p.add().panel().focus();
			IHorizontalPanel h0 = fp.add().panel().horizontal().width(1.0)
					.spacing(3);
			decoratePanel(p, clickListener, c, fp, h0);
		}
		// p.addSpace(4);
		// dummyFocusPanel = p.add().panel().focus();
		// dummyFocusPanel.visible(false);
		// IHorizontalPanel b = dummyFocusPanel.add().panel().horizontal()
		// .spacing(4);
		// dummyLabel = decoratePanel(p, null, dummy, dummyFocusPanel, b);
	}

	ILabel decoratePanel(final HorizontalScalingPanel p,
			final IClickListener clickListener, final ScrollTableColumnImpl c,
			final IFocusPanel fp, final IHorizontalPanel b) {
		fp.addDragStartListener(new IDragStartListener() {

			@Override
			public void onDragStart(IDragStartEvent event) {
				p.popUpVisible(true);
				dragged = c;
				// dummyFocusPanel.visible(true);
				// dummyLabel.text("Drop here").autoWrap(false);
				event.dragImage(fp);
				event.iD(COLUMN_SELECTION);
			}

			@Override
			public void onDragEnd() {
				p.popUpVisible(false);
				// dummyFocusPanel.visible(false);
				dragged = null;
			}
		});
		fp.addDropListener(new IDropListener() {

			@Override
			public void onDropOn(IDragEvent point) {
				p.popUpVisible(false);
				if (point.iD() == null || !point.iD().equals(COLUMN_SELECTION))
					return;
				dropOnTo(c);
				dragged = null;
			}
		});
		if (c.index == -1) {
			b.border().style().dotted();
		} else
			b.border().color().rgb(172, 197, 213);
		if (c.index == -1)
			b.color().white();
		else if (c.visible)
			b.color().gray(120);
		else
			b.color().gray(200);
		String label = c.name.equals("") ? COLUMN + " "
				+ String.valueOf(widget.columnList().indexOf(c) + 1) : c.name();
		ILabel l = b.add().label().text(label).autoWrap(false).breakWord(false);
		l.font().pixel(11);
		// new HyperlinkMouseOverListener(l);
		fp.tooltip("Click to "
				+ (c.visible ? "hide" : "show")
				+ " column \""
				+ label
				+ "\". Drag and drop panel onto another column panel to reorder table columns.");
		if (c.index == -1)
			l.font().color().white();
		else {
			decorateLabel(c, l);
			b.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					// widget.nextTimeShowPopUp(true);
					p.popUpVisible(false);
					c.visible = !c.visible;
					boolean allInvisible = !allowsEmptySelection;
					for (ScrollTableColumnImpl c1 : widget.columnList())
						allInvisible &= !c1.visible;
					if (allInvisible)
						c.visible = true;
					else {
						widget.notifyVisible(c);
						clickListener.onClick();
					}
				}
			});
		}
		return l;
	}

	void decorateLabel(final ScrollTableColumnImpl c, ILabel l) {
		// if (c.visible)
		l.font().color().white();
		// else
		// l.font().color().rgb(192, 192, 192);
	}

	protected void dropOnTo(ScrollTableColumnImpl c) {
		if (c == dragged)
			return;
		// int moved = widget.columnList().indexOf(dragged);
		int target = widget.columnList().indexOf(c);
		widget.columnList().remove(dragged);
		// if (target > moved)
		// target--;
		if (target >= widget.columnList().size())
			widget.columnList().add(dragged);
		else
			widget.columnList().add(target, dragged);
		widget.notifySwap(c, dragged);
		draw();
		widget.updateTable();
	}

}

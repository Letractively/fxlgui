/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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

import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDraggable.IDragStartListener;
import co.fxl.gui.api.IDropTarget.IDragEvent;
import co.fxl.gui.api.IDropTarget.IDropListener;
import co.fxl.gui.api.IFocusPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.impl.HyperlinkMouseOverListener;
import co.fxl.gui.impl.RuntimeConstants;
import co.fxl.gui.style.impl.Style;

public class ColumnSelection implements RuntimeConstants {

	private static final String SHOW_COLUMNS = Style.instance().mobile() ? "Columns"
			: "Column Selection";
	private static final String COLUMN = "Column";
	private static final String COLUMN_SELECTION_ID = "Column Selection";
	static final String TOOLTIP = "Click on column to toggle visibility. Use drag & drop to reorder.";
	TableWidgetAdp widget;
	private ScrollTableColumnImpl dragged;
	private ScrollTableColumnImpl dummy;
	private IFocusPanel dummyFocusPanel;
	private ILabel dummyLabel;
	private boolean allowsEmptySelection;

	// TODO FEATURE: MINOR: : Option: Look: Column-Selection: if > n
	// Columns or
	// sum(characters of column-headers) > m
	// then dynamically resize font size of column selection labels

	private ColumnSelection(final TableWidgetAdp widget,
			boolean allowsEmptySelection) {
		this.widget = widget;
		dummy = new ScrollTableColumnImpl(widget, -1);
		dummy.name("");
		draw();
		this.allowsEmptySelection = allowsEmptySelection;
	}

	public void draw() {
		IContainer clear = getContainer();
		IHorizontalPanel p = clear.panel().horizontal();
		IClickListener clickListener = new IClickListener() {
			@Override
			public void onClick() {
				widget.updateTable();
			}
		};
		addToPanel(p, clickListener);
	}

	protected IContainer getContainer() {
		if (widget.statusPanel() != null) {
			IGridCell center = widget.statusPanel().cell(1, 0).clear().valign()
					.center();
			widget.statusPanel().column(1).expand();
			return center;
		}
		return widget.getContainer();
	}

	void addToPanel(@SuppressWarnings("rawtypes") ILinearPanel p,
			final IClickListener clickListener) {
		addTitle(p);
		List<ScrollTableColumnImpl> columnList = widget.columnList();
		for (final ScrollTableColumnImpl c : columnList) {
			p.addSpace(4);
			IFocusPanel fp = p.add().panel().focus();
			IHorizontalPanel b = fp.add().panel().horizontal().spacing(4);
			decoratePanel(clickListener, c, fp, b);
		}
		p.addSpace(4);
		dummyFocusPanel = p.add().panel().focus();
		dummyFocusPanel.visible(false);
		IHorizontalPanel b = dummyFocusPanel.add().panel().horizontal()
				.spacing(4);
		dummyLabel = decoratePanel(null, dummy, dummyFocusPanel, b);
	}

	ILabel decoratePanel(final IClickListener clickListener,
			final ScrollTableColumnImpl c, final IFocusPanel fp,
			final IHorizontalPanel b) {
		fp.tooltip(TOOLTIP);
		fp.addDragStartListener(new IDragStartListener() {

			@Override
			public void onDragStart(IDragStartEvent event) {
				dragged = c;
				dummyFocusPanel.visible(true);
				dummyLabel.text("Drop here");
				event.dragImage(fp);
				event.iD(COLUMN_SELECTION_ID);
			}

			@Override
			public void onDragEnd() {
				dummyFocusPanel.visible(false);
				dragged = null;
			}
		});
		fp.addDropListener(new IDropListener() {

			@Override
			public void onDropOn(IDragEvent point) {
				if (point.iD() == null
						|| !point.iD().equals(COLUMN_SELECTION_ID))
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
		else
			Style.instance().table().selectedColumn(b, c.visible, false, false);
		ILabel l = b
				.add()
				.label()
				.text(c.name.equals("") ? COLUMN + " "
						+ String.valueOf(widget.columnList().indexOf(c) + 1)
						: c.name()).autoWrap(false).breakWord(false);
		new HyperlinkMouseOverListener(l);
		l.font().pixel(11);
		if (c.index == -1)
			l.font().color().white();
		else {
			decorateLabel(c, l);
			IClickListener cl = new IClickListener() {
				@Override
				public void onClick() {
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
			};
			b.addClickListener(cl);
			l.addClickListener(cl);
		}
		return l;
	}

	protected void dropOnTo(ScrollTableColumnImpl c) {
		if (c == dragged)
			return;
		widget.columnList().remove(dragged);
		if (c == dummy) {
			widget.columnList().add(dragged);
		} else {
			int i = widget.columnList().indexOf(c);
			widget.columnList().add(i, dragged);
		}
		draw();
		widget.updateTable();
	}

	void decorateLabel(final ScrollTableColumnImpl c, ILabel l) {
		Style.instance().table().selectedColumn(l, c.visible);
	}

	void addTitle(ILinearPanel<?> p) {
		addTitle(widget, p.add());
	}

	static void addTitle(TableWidgetAdp widget, IContainer c) {
		IHorizontalPanel horizontal = c.panel().horizontal();
		if (widget.configureListener() != null) {
			final ILabel l = addText(horizontal, SHOW_COLUMNS)
					.addClickListener(widget.configureListener()).mouseLeft();
			l.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					Style.instance().table().statusLink(l, true);
				}

				@Override
				public void onMouseOut() {
					Style.instance().table().statusLink(l, false);
				}
			});
			horizontal.addSpace(4).add().image().resource("edit_gray.png")
					.addClickListener(widget.configureListener());
			addText(horizontal, ":");
		} else
			addText(horizontal, SHOW_COLUMNS + ":");
		horizontal.addSpace(4);
	}

	static ILabel addText(IHorizontalPanel horizontal, String text) {
		return Style.instance().table().statusHeader(horizontal, text);
	}

	public static void newInstance(TableWidgetAdp tableWidgetAdp,
			boolean allowsEmptySelection) {
		if (SWING)
			new ColumnSelection(tableWidgetAdp, allowsEmptySelection);
		else
			new ScalingColumnSelection(tableWidgetAdp, allowsEmptySelection);
	}
}

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
package gwt.draft.client;

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.impl.ICallback;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableColumn;
import co.fxl.gui.table.scroll.api.IScrollTableWidget;
import co.fxl.gui.table.scroll.impl.ScrollTableWidgetImplProvider;
import co.fxl.gui.table.util.api.IDragDropListener;
import co.fxl.gui.table.util.impl.LazyScrollPanelImplWidgetProvider;

public class ScrollTableWidgetDemo implements IDragDropListener {

	private class Row {

		public String identifier;

		public Row(int i) {
			identifier = String.valueOf(i);
		}

	}

	private class Rows implements IRows<String> {

		Rows() {
			for (int i = 0; i < 1000; i++)
				content.add(new Row(i));
		}

		@Override
		public String identifier(int i) {
			return content.get(i).identifier;
		}

		@Override
		public Object[] row(int i) {
			return new Object[] { "C0/" + content.get(i).identifier,
					"C1/" + content.get(i).identifier,
					"C2/" + content.get(i).identifier };
		}

		@Override
		public int size() {
			return content.size();
		}

		@Override
		public boolean deletable(int i) {
			return false;
		}
	}

	private List<Row> content = new LinkedList<Row>();

	@Override
	public boolean allowsDrop(int rowIndex) {
		return true;
	}

	@Override
	public void drop(int dragIndex, int dropIndex, Where where,
			ICallback<Void> cb) {
		Row r = content.remove(dragIndex);
		if (dragIndex < dropIndex)
			dropIndex--;
		if (dropIndex < content.size()) {
			int index = dropIndex + (where.equals(Where.BEFORE) ? 0 : 1);
			content.add(index, r);
		} else if (where.equals(Where.BEFORE)) {
			content.add(dropIndex, r);
		} else
			content.add(r);
		cb.onSuccess(null);
	}

	public void run(IDisplay display) {
		display.register(new ScrollTableWidgetImplProvider());
		display.register(new LazyScrollPanelImplWidgetProvider());
		IVerticalPanel panel = display.container().panel().vertical()
				.spacing(10);
		display.fullscreen().visible(true);
		@SuppressWarnings("unchecked")
		IScrollTableWidget<String> widget = (IScrollTableWidget<String>) panel
				.add().widget(IScrollTableWidget.class);
		widget.selection().multi();
		widget.height(display.height() - 100);
		widget.addTitle("Table");
		widget.addButton("New");
		for (int i = 0; i < 3; i++) {
			IScrollTableColumn<String> c = widget.addColumn();
			c.name("Column " + i).sortable().type().type(String.class);
		}
		widget.rows(new Rows());
		widget.dragDropListener(false, this);
		widget.visible(true);
	}

}

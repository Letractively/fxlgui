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
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;

class ColumnSelection {

	// TODO FEATURE: Option: Look: Column-Selection: if > n Columns or
	// sum(characters of column-headers) > m
	// then dynamically resize font size of column selection labels

	ColumnSelection(final ScrollTableWidgetImpl widget) {
		IGridCell clear = widget.statusPanel().cell(1, 0).clear().align()
				.begin().valign().center();
		IHorizontalPanel p = clear.panel().horizontal().add().panel()
				.horizontal();
		IHorizontalPanel b0 = p.add().panel().horizontal();
		b0.add().label().text("SHOW COLUMNS:").font().pixel(10).weight().bold();
		for (final ScrollTableColumnImpl c : widget.columns) {
			p.addSpace(4);
			IHorizontalPanel b = p.add().panel().horizontal().spacing(4);
			b.border().color().rgb(172, 197, 213);
			if (c.visible)
				b.color().gray();
			else
				b.color().white();
			ILabel l = b.add().label().text(c.name).autoWrap(true);
			l.font().pixel(11);
			if (c.visible)
				l.font().color().white();
			else
				l.font().color().rgb(102, 102, 102);
			b.addClickListener(new IClickListener() {
				@Override
				public void onClick() {
					c.visible = !c.visible;
					boolean allInvisible = true;
					for (ScrollTableColumnImpl c1 : widget.columns)
						allInvisible &= !c1.visible;
					if (allInvisible)
						c.visible = true;
					else
						widget.update();
				}
			});
		}
	}
}

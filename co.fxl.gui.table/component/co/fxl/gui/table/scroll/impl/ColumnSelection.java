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
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;

class ColumnSelection {

	ScrollTableWidgetImpl widget;

	// TODO FEATURE: Option: Look: Column-Selection: if > n Columns or
	// sum(characters of column-headers) > m
	// then dynamically resize font size of column selection labels

	ColumnSelection(final ScrollTableWidgetImpl widget) {
		this.widget = widget;
		IContainer clear = widget.statusPanel().cell(1, 0).clear().align()
				.begin().valign().center();
		IHorizontalPanel p = clear.panel().horizontal().add().panel()
				.horizontal();
		IClickListener clickListener = new IClickListener() {
			@Override
			public void onClick() {
				widget.update();
			}
		};
		addToPanel(p, clickListener);
	}

	void addToPanel(ILinearPanel p, final IClickListener clickListener) {
		addTitle(p);
		for (final ScrollTableColumnImpl c : widget.columns) {
			p.addSpace(4);
			IHorizontalPanel b = p.add().panel().horizontal().spacing(4);
			decoratePanel(clickListener, c, b);
		}
	}

	void decoratePanel(final IClickListener clickListener,
			final ScrollTableColumnImpl c, IHorizontalPanel b) {
		b.border().color().rgb(172, 197, 213);
		if (c.visible)
			b.color().gray();
		else
			b.color().white();
		ILabel l = b.add().label().text(c.name).autoWrap(true);
		l.font().pixel(11);
		decorateLabel(c, l);
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
					clickListener.onClick();
			}
		});
	}

	void decorateLabel(final ScrollTableColumnImpl c, ILabel l) {
		if (c.visible)
			l.font().color().white();
		else
			l.font().color().rgb(102, 102, 102);
	}

	void addTitle(ILinearPanel p) {
		p.add().label().text("SHOW COLUMNS:").font().pixel(10).weight().bold();
	}
}

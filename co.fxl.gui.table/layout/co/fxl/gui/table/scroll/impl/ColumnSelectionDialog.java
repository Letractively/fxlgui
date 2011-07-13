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
package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.ILinearPanel;

public class ColumnSelectionDialog implements IClickListener {

	public static void addButton(final ColumnSelection cs,
			final ILinearPanel<?> p) {
		p.addSpace(4).add().label().text("COLUMNS >").hyperlink()
				.addClickListener(new ColumnSelectionDialog(cs, p)).mouseLeft()
				.font().pixel(10);// .weight().bold();
	}

	private ColumnSelection cs;
	private ILinearPanel<?> p0;

	private ColumnSelectionDialog(final ColumnSelection cs,
			final ILinearPanel<?> p) {
		this.cs = cs;
		this.p0 = p;
	}

	@Override
	public void onClick() {
		final IDialog dialog = p0.display().showDialog().title("SHOW COLUMNS");
		dialog.addButton().ok().addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				dialog.visible(false);
				cs.widget.update();
			}
		});
		ILinearPanel<?> p = dialog.container().panel().vertical().spacing(6)
				.add().panel().vertical().spacing(2);
		addToDialogPanel(p);
		p.addSpace(6);
		dialog.visible(true);
	}

	protected void addToDialogPanel(final ILinearPanel<?> p) {
		cs.addToPanel(p, new IClickListener() {
			@Override
			public void onClick() {
				addToDialogPanel(p.clear());
			}
		});
	}
}

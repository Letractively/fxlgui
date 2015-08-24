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
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.table.bulk.api;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.table.bulk.api.IBulkTableWidget.IUpdateAdapter;

public interface IBulkTableCell {

	IBulkTableCell text(String text);

	IBulkTableCell html(String text);

	IBulkTableCell color(String color);

	IBulkTableCell image(String image);

	IBulkTableCell checkBox(Boolean value);

	IBulkTableCell updateAdapter(IUpdateAdapter<Boolean> updateAdapter);

	IBulkTableCell updateListener(IUpdateListener<?> updateListener);

	IContainer container();

	Object nativeElement();

	IBulkTableCell styledText(String text, String color);

	IBulkTableCell html(String html, boolean checkLineBreak);

}
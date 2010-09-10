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
package co.fxl.gui.table.impl;

import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IGridPanel.IGridCell;

class ImageCellFactory extends CellFactory<IImage, String> {

	static final String PNG = ".png";

	@Override
	public Cell<IImage> create(TableWidgetImpl table, final RowImpl row,
			final int columnIndex, IGridCell cell, String value) {
		IImage image = cell.image();
		if (!value.equals(RowImpl.EMPTY)) {
			image.resource(value + PNG);
		} else
			image.visible(false);
		return new ImageCell(row.rowIndex, cell, image);
	}
}

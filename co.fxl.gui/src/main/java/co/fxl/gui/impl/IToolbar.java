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
package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IMargin;
import co.fxl.gui.api.IPadding;

public interface IToolbar {

	public interface IToolbarElement {

		IElement<?> panel();

		IContainer container();
	}

	IContainer add();

	IToolbar addGroup();

	void clear();

	IToolbar visible(boolean visible);

	IToolbar adjustHeights();

	IAlignment<IToolbar> align();

	IColor color();

	IBorder border();

	IToolbar height(int height);

	IToolbar spacing(int i);

	IToolbarElement addElement();

	IMargin margin();

	IPadding padding();

	// ILabel addLabel();

}

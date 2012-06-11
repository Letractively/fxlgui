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
package co.fxl.gui.navigation.group.impl;

import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.impl.FlipPage;

class ContentBuffer implements IColored {

	private FlipPage flipPage;

	public ContentBuffer(IContainer add) {
		flipPage = new FlipPage(add);
	}

	@Override
	public IColor color() {
		return flipPage.color();
	}

	void back() {
		flipPage.reset();
	}

	void preview() {
		flipPage.preview();
	}

	IContainer next() {
		return flipPage.next();
	}

	void flip() {
		flipPage.flip();
	}

}
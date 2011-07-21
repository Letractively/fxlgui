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
package co.fxl.gui.tree.impl;

import co.fxl.gui.tree.api.ITreeWidget.IViewID;

public class ViewID implements IViewID {

	private String title;
	private Class<?>[] constrainType;

	public ViewID(String title, Class<?>[] constrainType) {
		this.title = title;
		this.constrainType = constrainType;
	}

	@Override
	public int hashCode() {
		return title.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		ViewID iD = (ViewID) o;
		if (!title.equals(iD.title))
			return false;
		return equals(constrainType, iD.constrainType);
	}

	private boolean equals(Class<?>[] c1, Class<?>[] c2) {
		if (c1 == null || c2 == null)
			return c1 == c2;
		if (c1.length != c2.length)
			return false;
		for (int i = 0; i < c1.length; i++)
			if (!c1[i].equals(c2[i]))
				return false;
		return true;
	}
}
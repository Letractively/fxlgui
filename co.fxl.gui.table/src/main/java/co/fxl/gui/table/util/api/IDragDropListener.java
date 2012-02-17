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
package co.fxl.gui.table.util.api;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IElement;

public interface IDragDropListener {

	public enum Where {

		BEFORE, AFTER, UNDER;
	}

	boolean allowsDrop(int rowIndex);

	void over(IElement<?> element, int dragIndex, int dropIndex, Where where);

	void out(IElement<?> element, int dragIndex, int dropIndex, Where where);

	void drop(int dragIndex, int dropIndex, Where where, ICallback<Void> cb);

}
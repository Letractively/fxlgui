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
package co.fxl.gui.impl;

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IResizable.IResizeListener;

public interface IResizableWidget extends IResizeListener {

	public class Size {

		public int widthDecrement;
		public int minWidth;
		public int heightDecrement;
		public int minHeight;

		public Size() {
			this(320, 240, 720, 360);
		}

		public Size(int widthDecrement, int heightDecrement, int minWidth,
				int minHeight) {
			this.widthDecrement = widthDecrement;
			this.heightDecrement = heightDecrement;
			this.minWidth = minWidth;
			this.minHeight = minHeight;
		}

		public boolean defined() {
			return heightDecrement != 0 || widthDecrement != 0;
		}

		// public boolean defined() {
		// return widthDecrement != 0 || heightDecrement != 0;
		// }
	}

	IResizableWidget addResizableWidgetToDisplay();

	IResizableWidget setResizableWidget(IResizableWidget widget, String id);

	IResizableWidget addResizableWidgetToDisplay(IElement<?> link);

	IResizableWidget size(Size size);

}

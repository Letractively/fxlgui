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

import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.style.impl.Style;

public interface IResizableWidget extends IResizeListener {

	public class Size {

		private static final int MIN_WIDTH = StatusDisplay.instance()
				.minWidth() - 54;
		private int widthDecrement;
		private int minWidth;
		private int heightDecrement;
		private int minHeight;

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

		public int widthDecrement() {
			if (Style.instance().mobile())
				return 30;
			return widthDecrement;
		}

		public int heightDecrement() {
			if (Style.instance().mobile())
				return 30;
			return heightDecrement;
		}

		public int minWidth() {
			if (Style.instance().mobile())
				return StatusDisplay.instance().width() - 60;
			return minWidth > MIN_WIDTH ? MIN_WIDTH : minWidth;
		}

		public int minHeight() {
			if (Style.instance().mobile())
				return StatusDisplay.instance().height() - 80;
			return minHeight;
		}

		// public boolean defined() {
		// return widthDecrement != 0 || heightDecrement != 0;
		// }
	}

	IResizableWidget addResizableWidgetToDisplay();

	IResizableWidget setResizableWidget(IResizableWidget widget, String id);

	IResizableWidget addResizableWidgetToDisplay(IElement<?> link);

	IResizableWidget addResizableWidgetToDisplay(IPopUp link);

	IResizableWidget size(Size size);

}

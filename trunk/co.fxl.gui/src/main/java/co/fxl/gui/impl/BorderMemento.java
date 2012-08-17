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

import co.fxl.gui.api.IBordered.IBorder;

public class BorderMemento implements IBorder {

	public class BorderStyleMemento implements IBorderStyle {

		public boolean isPartial = false;
		public boolean top = false;
		public boolean bottom = false;
		public boolean left = false;
		public boolean right = false;

		@Override
		public IBorder shadow() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder shadow(int pixel) {
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder dotted() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder rounded() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder solid() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder etched() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder top() {
			top = true;
			return setPartial();
		}

		private IBorder setPartial() {
			isPartial = true;
			return BorderMemento.this;
		}

		@Override
		public IBorder bottom() {
			bottom = true;
			return setPartial();
		}

		@Override
		public IBorder noBottom() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder right() {
			right = true;
			return setPartial();
		}

		@Override
		public IBorder left() {
			left = true;
			return setPartial();
		}

	}

	public BorderStyleMemento style = new BorderStyleMemento();
	public boolean active = false;

	@Override
	public IColor color() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBorderStyle style() {
		return style;
	}

	@Override
	public IBorder width(int width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBorder title(String title) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBorder remove() {
		throw new UnsupportedOperationException();
	}

}

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
package co.fxl.gui.gwt;

import co.fxl.gui.api.IBordered.IBorder;

public abstract class GWTBorder implements IBorder {

	static final String BORDER_BOTTOM_LESS = "borderBottomLess";
	static final String BORDER_ROUNDED= "borderRounded";

	private class BorderStyle implements IBorderStyle {

		@Override
		public IBorder dotted() {
			style = "dotted";
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder solid() {
			style = "solid";
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder etched() {
			throw new MethodNotImplementedException();
		}

		@Override
		public IBorder top() {
			borderType = "borderTop";
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder bottom() {
			borderType = "borderBottom";
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder noBottom() {
			borderType = BORDER_BOTTOM_LESS;
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder rounded() {
			borderType = BORDER_ROUNDED;
			update();
			return GWTBorder.this;
		}
	}

	protected int width = 1;
	protected String color = "black";
	protected String style = "solid";
	protected String borderType = "border";

	@Override
	public abstract void remove();

	protected abstract void update();

	public IColor color() {
		Style styleS = new Style() {

			@Override
			public void addStyleName(String style) {
				color = style;
				update();
			}
		};
		return new GWTStyleColor(styleS) {

			@Override
			public void setColor(String color) {
				GWTBorder.this.color = color;
				update();
			}

			@Override
			public IColor rgb(int r, int g, int b) {
				color = toString(r, g, b);
				update();
				return this;
			}
		};
	}

	public IBorder width(int width) {
		this.width = width;
		update();
		return this;
	}

	@Override
	public IBorderStyle style() {
		return new BorderStyle();
	}

	@Override
	public IBorder title(String title) {
		throw new MethodNotImplementedException();
	}

}

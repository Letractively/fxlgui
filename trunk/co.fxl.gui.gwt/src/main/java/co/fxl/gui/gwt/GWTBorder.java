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
	static final String BORDER_ROUNDED = "borderRounded";

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
			throw new UnsupportedOperationException();
		}

		@Override
		public IBorder top() {
			top = true;
			borderType = "borderTop";
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder left() {
			left = true;
			borderType = "borderLeft";
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder bottom() {
			bottom = true;
			borderType = "borderBottom";
			update();
			return GWTBorder.this;
		}

		@Override
		public IBorder noBottom() {
			left = true;
			right = true;
			top = true;
			borderType = BORDER_BOTTOM_LESS;
			update();
			return GWTBorder.this;
		}

		@Override
		public IRoundBorder rounded() {
			GWTBorder.this.width(0);
			borderType = BORDER_ROUNDED;
			update();
			return new IRoundBorder() {

				@Override
				public IRoundBorder bottom(boolean bottom) {
					roundBottom = false;
					update();
					return this;
				}

				@Override
				public IRoundBorder width(int width) {
					roundWidth = width;
					update();
					return this;
				}

				@Override
				public IRoundBorder right(boolean right) {
					roundRight = false;
					update();
					return this;
				}

				@Override
				public IRoundBorder left(boolean left) {
					roundLeft = false;
					update();
					return this;
				}

				@Override
				public IRoundBorder remove() {
					roundLeft = false;
					roundRight = false;
					roundBottom = false;
					roundTop = false;
					return this;
				}
			};
		}

		@Override
		public IBorder shadow() {
			return shadow(4);
		}

		// @Override
		// public IBorder roundShadow() {
		// roundShadow = true;
		// update();
		// return GWTBorder.this;
		// }

		@Override
		public IBorder shadow(int pixel) {
			if (pixel != 4 && pixel != 2)
				throw new UnsupportedOperationException();
			GWTBorder.this.shadow(pixel);
			return GWTBorder.this;
		}

		@Override
		public IBorder right() {
			right = true;
			borderType = "borderRight";
			update();
			return GWTBorder.this;
		}
	}

	int width = 1;
	String color = "black";
	String style = "solid";
	String borderType = "border";
	boolean roundBottom = true;
	boolean roundLeft = true;
	boolean roundRight = true;
	boolean roundTop = true;
	int roundWidth = 3;
	boolean top = false;
	boolean bottom = false;
	boolean left = false;
	boolean right = false;

	// boolean roundShadow;

	@Override
	public abstract IBorder remove();

	protected void shadow(int pixel) {
	}

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
		throw new UnsupportedOperationException();
	}

}

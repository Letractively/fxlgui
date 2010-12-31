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
package co.fxl.gui.api.template;

import co.fxl.gui.api.IColored.IColor;

public abstract class ColorTemplate implements IColor {

	private class MixColor extends ColorTemplate implements IColor {

		private int r = 0;
		private int g = 0;
		private int b = 0;
		private int nCalls = 0;

		@Override
		public IColor rgb(int r, int g, int b) {
			this.r += r;
			this.g += g;
			this.b += b;
			nCalls++;
			int newR = this.r / nCalls;
			int newG = this.g / nCalls;
			int newB = this.b / nCalls;
			ColorTemplate.this.rgb(newR, newG, newB);
			return this;
		}

		@Override
		public IColor remove() {
			return ColorTemplate.this.remove();
		}
	}

	@Override
	public IColor black() {
		return rgb(0, 0, 0);
	}

	@Override
	public IColor green() {
		return rgb(0, 255, 0);
	}

	@Override
	public IColor blue() {
		return rgb(0, 0, 255);
	}

	@Override
	public IColor gray() {
		return rgb(140, 140, 140);
	}

	@Override
	public IColor lightgray() {
		return rgb(211, 211, 211);
	}

	@Override
	public IColor red() {
		return rgb(255, 0, 0);
	}

	@Override
	public IColor white() {
		return rgb(255, 255, 255);
	}

	@Override
	public IColor yellow() {
		return rgb(255, 255, 0);
	}

	@Override
	public IColor mix() {
		return new MixColor();
	}
}

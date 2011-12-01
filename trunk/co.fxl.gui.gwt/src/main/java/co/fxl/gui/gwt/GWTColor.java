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

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IColored.IGradient;

abstract class GWTColor implements IColor {

	public class Gradient implements IGradient {

		private GWTColor gradient;
		int[] fallback = null;

		Gradient(final GWTColor original) {
			gradient = new GWTColor() {

				@Override
				public IGradient gradient() {
					throw new MethodNotImplementedException();
				}

				@Override
				public IColor remove() {
					throw new MethodNotImplementedException();
				}

				@Override
				IColor setColorInternal(String string) {
					original.setColor("-webkit-gradient(linear, left top, left bottom, from("
							+ original.color + "), to(" + color + "))");
					return this;
				}
			};
		}

		@Override
		public IGradient fallback(int r, int g, int b) {
			fallback = new int[] { r, g, b };
			return this;
		}

		@Override
		public IColor vertical() {
			return gradient;
		}
	}

	private class MixColor extends GWTColor {

		private String mix = "";

		@Override
		IColor setColorInternal(String string) {
			if (!mix.equals(""))
				mix += "-";
			mix += string;
			GWTColor.this.setColor(mix);
			return this;
		}

		@Override
		public IColor remove() {
			GWTColor.this.remove();
			return this;
		}

		@Override
		public IGradient gradient() {
			return new Gradient(this);
		}

	}

	private String color;

	@Override
	public IColor black() {
		return setColor("black");
	}

	public final IColor setColor(String string) {
		color = string;
		setColorInternal(string);
		return this;
	}

	abstract IColor setColorInternal(String string);

	@Override
	public IColor gray() {
		return setColor("gray");
	}

	@Override
	public IColor lightgray() {
		return setColor("lightgray");
	}

	@Override
	public IColor white() {
		return setColor("white");
	}

	@Override
	public IColor red() {
		return setColor("red");
	}

	@Override
	public IColor green() {
		return setColor("green");
	}

	@Override
	public IColor blue() {
		return setColor("blue");
	}

	@Override
	public IColor yellow() {
		return setColor("yellow");
	}

	@Override
	public IColor rgb(int r, int g, int b) {
		throw new MethodNotImplementedException();
	}

	@Override
	public IColor mix() {
		return new MixColor();
	}
}

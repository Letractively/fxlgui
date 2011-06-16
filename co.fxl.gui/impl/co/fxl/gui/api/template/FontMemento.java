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

import co.fxl.gui.api.IFontElement.IFont;

public class FontMemento implements IFont {

	private enum Type {
		BOLD, ITALIC, PLAIN;
	}

	private class WeightMemento implements IWeight {

		private Type type;

		@Override
		public IFont bold() {
			type = Type.BOLD;
			return FontMemento.this;
		}

		@Override
		public IFont italic() {
			type = Type.ITALIC;
			return FontMemento.this;
		}

		@Override
		public IFont plain() {
			type = Type.PLAIN;
			return FontMemento.this;
		}

		void apply(IWeight weight) {
			if (type == Type.BOLD) {
				weight.bold();
			} else if (type == Type.ITALIC)
				weight.italic();
			else
				weight.plain();
		}
	}

	private int pixel;
	private WeightMemento weight;

	@Override
	public IColor color() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IFamily family() {
		throw new MethodNotImplementedException();
	}

	@Override
	public IWeight weight() {
		return weight = new WeightMemento();
	}

	@Override
	public IFont pixel(int pixel) {
		this.pixel = pixel;
		return this;
	}

	@Override
	public IFont underline(boolean underline) {
		throw new MethodNotImplementedException();
	}

	public void apply(IFont font) {
		if (pixel != -1)
			font.pixel(pixel);
		if (weight != null)
			weight.apply(font.weight());
	}
}

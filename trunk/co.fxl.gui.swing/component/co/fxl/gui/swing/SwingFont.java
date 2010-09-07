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
package co.fxl.gui.swing;

import java.awt.Color;
import java.awt.Font;

import co.fxl.gui.api.IFontElement.IFont;

class SwingFont implements IFont {

	private final class Weight implements IWeight {

		@Override
		public IFont bold() {
			weight = Font.BOLD;
			updateFont();
			return SwingFont.this;
		}

		@Override
		public IFont italic() {
			weight = Font.ITALIC;
			updateFont();
			return SwingFont.this;
		}

		@Override
		public IFont plain() {
			weight = Font.PLAIN;
			updateFont();
			return SwingFont.this;
		}
	}

	private final class Family implements IFamily {

		@Override
		public IFont arial() {
			return name("Arial");
		}

		@Override
		public IFont timesNewRoman() {
			return name("Times New Roman");
		}

		@Override
		public IFont verdana() {
			return name("Verdana");
		}

		@Override
		public IFont lucinda() {
			return name("Lucinda");
		}

		@Override
		public IFont helvetica() {
			return name("Helvetica");
		}

		@Override
		public IFont courier() {
			return name("Courier");
		}

		@Override
		public IFont georgia() {
			return name("Georgia");
		}

		@Override
		public IFont garamond() {
			return name("Garamond");
		}

		@Override
		public IFont calibri() {
			return name("Calibri");
		}

		@Override
		public IFont name(String font) {
			family = font;
			updateFont();
			return SwingFont.this;
		}
	}

	private SwingTextElement<?, ?> swingTextElement;
	private String family = "Times New Roman";
	private int size = 10;
	private int weight = Font.PLAIN;

	SwingFont(SwingTextElement<?, ?> swingTextElement) {
		this.swingTextElement = swingTextElement;
		family = swingTextElement.getFontFamily();
		size = swingTextElement.getFontSize();
		updateFont();
	}

	@Override
	public IColor color() {
		return new SwingColor() {
			@Override
			protected void setColor(Color color) {
				swingTextElement.setForeground(color);
			}
		};
	}

	@Override
	public IFamily family() {
		return new Family();
	}

	@Override
	public IWeight weight() {
		return new Weight();
	}

	@Override
	public IFont pixel(int i) {
		size = i;
		updateFont();
		return this;
	}

	void updateFont() {
		Font f = new Font(family, weight, size);
		swingTextElement.setFont(f);
	}

	@Override
	public IFont underline(boolean underline) {
		swingTextElement.html.underline = underline;
		swingTextElement.update();
		return this;
	}
}
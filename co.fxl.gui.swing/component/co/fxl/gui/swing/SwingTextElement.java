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

import javax.swing.JComponent;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.template.HTMLText;

public abstract class SwingTextElement<T extends JComponent, R> extends
		SwingElement<T, R> {

	private final class SwingTextElementColor extends SwingColor {
		@Override
		protected void setColor(Color color) {
			container.component.setBackground(color);
		}
	}

	SwingFont font = new SwingFont(this);
	HTMLText html = new HTMLText();

	SwingTextElement(SwingContainer<T> container) {
		super(container);
	}

	public IFont font() {
		return font;
	}

	void setText(String text) {
		html.setText(text);
		update();
	}

	R update() {
		setTextOnComponent(processText());
		font.updateFont();
		@SuppressWarnings("unchecked")
		R ret = (R) this;
		return ret;
	}

	String processText() {
		return html.toString();
	}

	public IColor color() {
		return new SwingTextElementColor();
	}

	String getFontFamily() {
		return container.component.getFont().getFamily();
	}

	int getFontSize() {
		return container.component.getFont().getSize();
	}

	void setFont(Font f) {
		container.component.setFont(f);
	}

	void setForeground(Color color) {
		container.component.setForeground(color);
	}

	abstract void setTextOnComponent(String text);

	@SuppressWarnings("unchecked")
	public R tooltip(String text) {
		container.component.setToolTipText(text);
		return (R) this;
	}
}

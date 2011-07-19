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

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import co.fxl.gui.api.IBordered.IBorder;

import com.sun.java.swing.plaf.windows.WindowsBorders.DashedBorder;

class SwingBorder implements IBorder {

	final class BorderStyle implements IBorderStyle {

		@Override
		public IBorder dotted() {
			borderStyle = DOTTED;
			updateBorder();
			return SwingBorder.this;
		}

		@Override
		public IBorder rounded() {
			borderStyle = ROUNDED;
			updateBorder();
			return SwingBorder.this;
		}

		@Override
		public IBorder solid() {
			borderStyle = SOLID;
			updateBorder();
			return SwingBorder.this;
		}

		@Override
		public IBorder etched() {
			borderStyle = ETCHED;
			updateBorder();
			return SwingBorder.this;
		}

		@Override
		public IBorder bottom() {
			element.setBorder(new TopBorder(SwingBorder.this, false));
			return SwingBorder.this;
		}

		@Override
		public IBorder left() {
			element.setBorder(new LeftBorder(SwingBorder.this, false));
			return SwingBorder.this;
		}

		@Override
		public IBorder top() {
			element.setBorder(new TopBorder(SwingBorder.this, true));
			return SwingBorder.this;
		}

		@Override
		public IBorder noBottom() {
			element.setBorder(new NoBottomBorder(SwingBorder.this, true));
			return SwingBorder.this;
		}

		@Override
		public IBorder shadow() {
			// TODO ...
			return SwingBorder.this;
		}
	}

	private static final int SOLID = 0;
	private static final int DOTTED = 1;
	private static final int ETCHED = 2;
	private static final int ROUNDED = 3;
	private JComponent element;
	private int borderThickness = 1;
	private int borderStyle = SOLID;
	Color borderColor = Color.BLACK;

	public SwingBorder(SwingElement<?, ?> component) {
		this(component.container.component);
	}

	public SwingBorder(JComponent component) {
		this.element = component;
		width(1);
	}

	private void updateBorder() {
		if (element.getBorder() instanceof EmptyBorder)
			throw new MethodNotImplementedException();
		if (borderStyle == SOLID)
			element.setBorder(new LineBorder(borderColor, borderThickness));
		else if (borderStyle == ETCHED)
			element.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		else if (borderStyle == ROUNDED)
			element.setBorder(new LineBorder(borderColor, borderThickness, true));
		else {
			if (borderThickness != 1)
				throw new MethodNotImplementedException();
			element.setBorder(new DashedBorder(borderColor));
		}
	}

	private void updateBorder(Color color) {
		this.borderColor = color;
		updateBorder();
	}

	public IBorder width(int size) {
		if (size == 0) {
			element.setBorder(null);
			return this;
		}
		borderThickness = size;
		updateBorder();
		return this;
	}

	public IColor color() {
		return new SwingColor() {

			protected void setColor(Color color) {
				updateBorder(color);
			}
		};
	}

	@Override
	public IBorderStyle style() {
		return new BorderStyle();
	}

	@Override
	public IBorder title(String title) {
		TitledBorder border = new TitledBorder(title);
		element.setBorder(border);
		return this;
	}

	@Override
	public void remove() {
		element.setBorder(null);
	}
}
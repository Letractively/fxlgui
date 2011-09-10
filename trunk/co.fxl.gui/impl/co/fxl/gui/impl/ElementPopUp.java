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
package co.fxl.gui.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IMouseOverElement;
import co.fxl.gui.api.IMouseOverElement.IMouseOverListener;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;

public class ElementPopUp {

	public interface Decorator {

		void decorate(IVerticalPanel panel);
	}

	public static Heights HEIGHTS = new Heights(0);
	private IPopUp popUp;
	private IElement<?> element;
	private int lines = -1;
	private ColorMemento color;
	private boolean scrollPane = true;

	public ElementPopUp(IElement<?> element) {
		this.element = element;
	}

	public ElementPopUp lines(int lines) {
		this.lines = lines;
		return this;
	}

	public IColor color() {
		return color = new ColorMemento();
	}

	public ElementPopUp scrollPane(boolean scrollPane) {
		this.scrollPane = scrollPane;
		return this;
	}

	public IVerticalPanel create() {
		popUp = element.display().showPopUp().autoHide(true);
		HEIGHTS.decorateBorder(popUp).style().shadow();
		int w = Math.min(320, element.width());
		int h = 0;
		if (lines >= 1)
			h = 2 + 19 * lines;
		if (h > 0)
			popUp.size(w, h);
		else
			popUp.width(w);
		popUp.offset(element.offsetX(), element.offsetY() + element.height());
		IContainer container = popUp.container();
		if (scrollPane) {
			IScrollPane scrollPane = container.scrollPane().width(w);
			scrollPane.color().white();
			if (color != null)
				color.forward(scrollPane.color());
			scrollPane.border().remove();
			container = scrollPane.viewPort();
		}
		// TODO refine, 17 = hack for GWT
		IVerticalPanel panel = container.panel().vertical().spacing(4);
		if (color != null)
			color.forward(panel.color());
		return panel;
	}

	public ElementPopUp clear() {
		if (popUp != null) {
			popUp.visible(false);
			popUp = null;
		}
		return this;
	}

	public ElementPopUp visible(boolean b) {
		popUp.visible(b);
		return this;
	}

	public ElementPopUp onMouseOver(final Decorator decorator) {
		if (element instanceof IMouseOverElement) {
			IMouseOverElement<?> moe = (IMouseOverElement<?>) element;
			moe.addMouseOverListener(new IMouseOverListener() {

				@Override
				public void onMouseOver() {
					IVerticalPanel p = create();
					decorator.decorate(p);
					visible(true);
				}

				@Override
				public void onMouseOut() {
					clear();
				}
			});
		}
		return this;
	}

	public ElementPopUp onRightClick(final Decorator decorator) {
		if (element instanceof IClickable) {
			IClickable<?> c = (IClickable<?>) element;
			c.addClickListener(new IClickListener() {

				@Override
				public void onClick() {
					IVerticalPanel p = create();
					decorator.decorate(p);
					visible(true);
				}
			}).mouseRight();
		}
		return this;
	}
}

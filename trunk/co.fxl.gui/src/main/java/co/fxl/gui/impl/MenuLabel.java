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

import co.fxl.gui.api.IBordered.IBorder.IBorderStyle;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;

public class MenuLabel {

	class OnAction implements IClickListener {

		private IHorizontalPanel panel;

		OnAction() {
			basePanel.clear();
			panel = basePanel.add().panel().horizontal().spacing(4);
			panel.add().label().text(text);
			panel.addClickListener(this);
		}

		@Override
		public void onClick() {
			new OffAction();
		}
	}

	class OffAction implements IClickListener {

		private IHorizontalPanel panel;
		private IHorizontalPanel p1;

		OffAction() {
			basePanel.clear();
			panel = basePanel.add().panel().horizontal().spacing(4);
			panel.add().label().text(text);
			panel.color().white();
			if (atBottom)
				panel.border().style().noBottom();
			else {
				IBorderStyle s = panel.border().style();
				s.left();
				s.top();
				s.bottom();
			}
			IPopUp popUp = panel.display().showPopUp();
			panel.addClickListener(this);
			popUp.addVisibleListener(new IUpdateListener<Boolean>() {
				@Override
				public void onUpdate(Boolean value) {
					if (!value) {
						onClick();
					}
				}
			});
			popUp.autoHide(true);
			popUp.border().remove();
			int offsetX = panel.offsetX();
			int offsetY = panel.offsetY() + panel.height();
			if (!atBottom) {
				offsetX = panel.offsetX() + panel.width();
				offsetY = panel.offsetY();
			}
			popUp.offset(offsetX, offsetY);
			IContainer c;
			if (atBottom) {
				IVerticalPanel popUpPanel = popUp.container().panel()
						.vertical();
				IHorizontalPanel hPanel = popUpPanel.add().panel().horizontal();
				p1 = hPanel.add().panel().horizontal().size(panel.width(), 1);
				p1.color().white();
				IBorderStyle style = p1.border().style();
				style.left();
				style.right();
				p2 = hPanel.add().panel().horizontal();
				p2.color().black();
				vertical = popUpPanel.add().panel().vertical();
				IBorderStyle s = vertical.border().style();
				s.left();
				s.right();
				s.bottom();
			} else {
				IHorizontalPanel popUpPanel = popUp.container().panel()
						.horizontal().align().begin();
				IVerticalPanel hPanel = popUpPanel.add().panel().vertical();
				p1 = hPanel.add().panel().horizontal()
						.size(4, panel.height() - 1);
				p1.color().white();
				IBorderStyle style = p1.border().style();
				style.top();
				p2 = hPanel.add().panel().horizontal();
				style = p2.border().style();
				style.left();
				style.bottom();
				vertical = popUpPanel.add().panel().vertical();
				IBorderStyle s = vertical.border().style();
				s.top();
				s.right();
				s.bottom();
			}
			p1.add().label().text("&#160;");
			p2.add().label().text("&#160;");
			c = vertical.spacing(4).add();
			decorator.decorate(c);
			popUp.visible(true);
			if (atBottom) {
				p2.size(vertical.width() - panel.width(), 1);
			} else {
				p2.size(3, vertical.height() - panel.height() + 1);
			}
		}

		@Override
		public void onClick() {
			new OnAction();
		}
	}

	private IDecorator decorator;
	private boolean atBottom = false;
	private IHorizontalPanel p2;
	private IVerticalPanel vertical;
	private IHorizontalPanel basePanel;
	private String text;

	public MenuLabel(IContainer c) {
		basePanel = c.panel().horizontal();
	}

	public MenuLabel text(String text) {
		this.text = text;
		new OnAction();
		return this;
	}

	public MenuLabel decorator(IDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	public MenuLabel atBottom(boolean atBottom) {
		this.atBottom = atBottom;
		return this;
	}
}

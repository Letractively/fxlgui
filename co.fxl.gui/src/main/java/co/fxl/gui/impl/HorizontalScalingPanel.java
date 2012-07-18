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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;

public class HorizontalScalingPanel implements IClickListener {

	private IHorizontalPanel basic;
	private IHorizontalPanel panel;
	private IHorizontalPanel morePanel;
	private List<IContainer> containers = new LinkedList<IContainer>();
	private List<IContainer> relocated = new LinkedList<IContainer>();
	private int width;

	public HorizontalScalingPanel(IContainer c) {
		basic = c.panel().horizontal();
		panel = basic.add().panel().horizontal();
		morePanel = basic.add().panel().horizontal().visible(false);
		morePanel.addClickListener(this);
		morePanel.addSpace(8).add().label().text("More").addClickListener(this);
		// morePanel.add().image().resource("more.png").addClickListener(this);
	}

	public HorizontalScalingPanel width(int width) {
		this.width = width;
		if (isTooLarge()) {
			morePanel.visible(true);
			for (int i = containers.size() - 1; i >= 0 && isTooLarge(); i--) {
				IContainer c = containers.get(i);
				relocated.add(c);
				c.element().remove();
			}
		}
		return this;
	}

	public boolean isTooLarge() {
		return basic.width() > width;
	}

	public IContainer add() {
		IContainer c = panel.add();
		containers.add(c);
		return c;
	}

	@Override
	public void onClick() {
		IPopUp p = Display.instance().showPopUp().autoHide(true);
		p.border().style().shadow();
		IVerticalPanel pn = p.container().panel().vertical().spacing(10);
		for (IContainer c : relocated) {
			pn.add().element(c.element());
		}
		p.visible(true);
	}

	public void addSpace(int i) {
		basic.addSpace(i);
	}

}

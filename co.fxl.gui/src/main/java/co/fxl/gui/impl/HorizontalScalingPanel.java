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

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;
import co.fxl.gui.api.IVerticalPanel;

public class HorizontalScalingPanel implements IClickListener {

	private static final int SPACING_POPUP = 6;
	private IHorizontalPanel basic;
	private IHorizontalPanel panel;
	private IHorizontalPanel morePanel;
	private List<IContainer> containers = new LinkedList<IContainer>();
	private List<Integer> heights = new LinkedList<Integer>();
	private List<Integer> widths = new LinkedList<Integer>();
	private List<IContainer> relocated = new LinkedList<IContainer>();
	private int width;
	private ILabel text;
	private int hspace;
	private IPopUp popUp;

	public HorizontalScalingPanel(IContainer c) {
		basic = c.panel().horizontal().align().begin().add().panel()
				.horizontal().align().begin();
		panel = basic.add().panel().horizontal().align().begin();
		morePanel = basic.add().panel().horizontal().spacing(SPACING_POPUP)
				.align().begin().visible(false);
		morePanel.addClickListener(this);
		IHorizontalPanel horizontal = morePanel.add().panel().horizontal()
				.align().begin();
		text = horizontal.add().label().text("More");
		horizontal.add().image().resource("less_black.png");
		// .addClickListener(this);
		morePanel.addClickListener(this);
		// horizontal.addClickListener(this);
		// text.addClickListener(this);
	}

	public IFont font() {
		return text.font();
	}

	public HorizontalScalingPanel hspace(int hspace) {
		this.hspace = hspace;
		return this;
	}

	public HorizontalScalingPanel width(int width) {
		this.width = width;
		for (int i = 0; i < containers.size(); i++) {
			heights.add(containers.get(i).element().height());
			widths.add(containers.get(i).element().width());
		}
		if (isTooLarge()) {
			morePanel.visible(true);
			for (int i = containers.size() - 1; i >= 0 && isTooLarge(); i--) {
				IContainer c = containers.get(i);
				relocated.add(0, c);
				c.element().remove();
			}
		}
		return this;
	}

	public boolean isTooLarge() {
		return basic.width() > width;
	}

	public IContainer add() {
		if (!containers.isEmpty() && hspace > 0)
			panel.addSpace(hspace);
		IContainer c = panel.add();
		containers.add(c);
		return c;
	}

	@Override
	public void onClick() {
		if (popUp != null) {
			popUp.visible(false);
		}
		morePanel.color().white();
		morePanel.border().style().bottom().style().left().style().right();
		morePanel.spacing(SPACING_POPUP - 1);
		morePanel.clickable(false);
		popUp = Display.instance().showPopUp().autoHide(true);
		IVerticalPanel base = popUp.container().panel().vertical();
		IVerticalPanel pn = base.add().panel().vertical()
				.spacing(SPACING_POPUP);
		int sumHeight = SPACING_POPUP;
		int maxWidth = morePanel.width();
		for (IContainer c : relocated) {
			pn.add().element(c.element());
			int height = heights.get(containers.indexOf(c));
			sumHeight += SPACING_POPUP + height;
			maxWidth = Math.max(maxWidth, widths.get(containers.indexOf(c)) + 2
					* SPACING_POPUP);
		}
		base.size(maxWidth, sumHeight);
		if (maxWidth > morePanel.width()) {
			popUp.border().remove().style().left().style().top().style()
					.right().style();
			IGridPanel p02 = base.add().panel().grid().spacing(0);
			IAbsolutePanel a0 = p02.cell(0, 0).panel().absolute()
					.size(morePanel.width() - 2, 1);
			addDummyIE(a0);
			IAbsolutePanel a1 = p02.cell(1, 0).panel().absolute()
					.size(maxWidth - morePanel.width() + 2, 1);
			addDummyIE(a1);
			a1.color().black();
		} else {
			popUp.border().remove().color().black();
		}
		popUp.offset(morePanel.offsetX(), morePanel.offsetY() - sumHeight);
		// popUp.size(maxWidth, sumHeight);
		popUp.visible(true);
		popUp.addVisibleListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value) {
					morePanel.color().remove();
					morePanel.border().remove();
					morePanel.spacing(SPACING_POPUP);
					morePanel.clickable(true);
					popUp = null;
				}
			}
		});
	}

	public static void addDummyIE(IPanel<?> leftPartBorder) {
		if (Env.is(Env.IE))
			leftPartBorder.add().image().size(1, 1).resource("empty_1x1.png");
	}

	public void addSpace(int i) {
		basic.addSpace(i);
	}

	public void popUpVisible(boolean b) {
		if (b) {
			if (morePanel.visible())
				onClick();
		} else if (popUp != null) {
			popUp.visible(false);
		}
	}

}

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
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IContainer;
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
		basic = c.panel().horizontal().add().panel().horizontal();
		panel = basic.add().panel().horizontal();
		// horizontal.addClickListener(this);
		// text.addClickListener(this);
	}

	void addMorePanel() {
		morePanel = basic.addSpace(4).add().panel().horizontal()
				.spacing(SPACING_POPUP);
		IHorizontalPanel horizontal = morePanel.add().panel().horizontal();
		text = horizontal.add().label().text("More");
		text.font().pixel(11).weight().bold();
		horizontal.add().image().resource("less_black.png");
		// .addClickListener(this);
		morePanel.addClickListener(this);
	}

	public HorizontalScalingPanel hspace(int hspace) {
		return this;
	}

	public HorizontalScalingPanel width(int width) {
		this.width = width;
		for (int i = 0; i < containers.size(); i++) {
			heights.add(containers.get(i).element().height());
			widths.add(containers.get(i).element().width());
		}
		if (isTooLarge()) {
			addMorePanel();
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
			return;
		}
		morePanel.color().white();
		color(morePanel.border().style().bottom().style().left().style()
				.right().color());
		morePanel.spacing(0).spacing().bottom(SPACING_POPUP - 1)
				.left(SPACING_POPUP - 1).right(SPACING_POPUP - 1)
				.top(SPACING_POPUP);
		morePanel.clickable(false);
		popUp = PopUp.showPopUp().autoHide(true);
		IVerticalPanel base = popUp.container().panel().vertical();
		IVerticalPanel pn = base.add().panel().vertical()
				.spacing(SPACING_POPUP);
		int sumHeight = SPACING_POPUP;
		int maxWidth = morePanel.width();
		int innerWidth = 0;
		for (IContainer c : relocated) {
			pn.add().element(c.element());
			int height = heights.get(containers.indexOf(c));
			sumHeight += SPACING_POPUP + height;
			innerWidth = Math
					.max(widths.get(containers.indexOf(c)), innerWidth);
			maxWidth = Math.max(maxWidth, innerWidth + 2 * SPACING_POPUP);
		}
		for (IContainer c : relocated) {
			c.element().width(innerWidth);
		}
		base.size(maxWidth, sumHeight);
		if (maxWidth > morePanel.width()) {
			color(popUp.border().remove().style().left().style().top().style()
					.right().color());
			IGridPanel p02 = base.add().panel().grid().spacing(0).width(1.0);
			IAbsolutePanel a0 = p02.cell(0, 0).panel().absolute()
					.size(morePanel.width() - 2, 1);
			addDummy(a0);
			IAbsolutePanel a1 = p02.cell(1, 0).panel().absolute()
					.size(maxWidth - morePanel.width() + 2, 1);
			addDummy(a1);
			color(a1.color());
		} else {
			color(popUp.border().remove().color());
		}
		popUp.offset(morePanel.offsetX(), morePanel.offsetY() - sumHeight);
		// popUp.size(maxWidth, sumHeight);
		popUp.visible(true);
		popUp.addVisibleListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				if (!value) {
					Display.instance().invokeLater(new Runnable() {
						@Override
						public void run() {
							morePanel.color().remove();
							morePanel.border().remove();
							morePanel.spacing(SPACING_POPUP).spacing()
									.bottom(0).left(0).right(0).top(0);
							morePanel.clickable(true);
							popUp = null;
						}
					}, 100);
				}
			}
		});
	}

	private void color(IColor color) {
		color.black();
	}

	public static void addDummyIE(IPanel<?> leftPartBorder) {
		if (Env.is(Env.IE))
			addDummy(leftPartBorder);
	}

	public static void addDummy(IPanel<?> leftPartBorder) {
		leftPartBorder.add().image().size(1, 1).resource("empty_1x1.png");
	}

	public void addSpace(int i) {
		basic.addSpace(i);
	}

	public void popUpVisible(boolean b) {
		if (b) {
			if (morePanel != null && morePanel.visible() && popUp == null)
				onClick();
		} else if (popUp != null) {
			popUp.visible(false);
		}
	}

}

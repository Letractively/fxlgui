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
package co.fxl.gui.table.scroll.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IAbsolutePanel;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;

class ScrollTable3CardWidgetTest implements IScrollListener {

	private static final int VISIBLE_HEIGHT = 600;
	private static final int MIN_HEIGHT = 10;
	private static final int ROWS = 1000;
	private static final int MAX_ROWS_PER_PAGE = VISIBLE_HEIGHT / MIN_HEIGHT;
	private ICardPanel cardPanel;
	private IAbsolutePanel card1;
	private IAbsolutePanel card2;
	private IAbsolutePanel card3;
	private boolean card3painted = false;
	private IAbsolutePanel active;
	private int[] heights = new int[ROWS];
	private int scrollHeight;
	private boolean updating;

	void run(IDisplay display) {
		for (int i = 0; i < heights.length; i++)
			heights[i] = MIN_HEIGHT + 5 * (i % 10);
		IScrollPane scrollPane = display.container().panel().vertical().add()
				.scrollPane();
		scrollPane.height(VISIBLE_HEIGHT);
		cardPanel = scrollPane.viewPort().panel().card();
		scrollHeight = (ROWS + (VISIBLE_HEIGHT / MIN_HEIGHT)) * MIN_HEIGHT;
		card1 = cardPanel.add().panel().absolute().height(scrollHeight);
		card2 = cardPanel.add().panel().absolute().height(scrollHeight);
		card3 = cardPanel.add().panel().absolute().height(scrollHeight);
		paintCard3(card3);
		cardPanel.show(card1);
		active = card1;
		scrollPane.addScrollListener(this);
		display.fullscreen().visible(true);
		onScroll(0);
	}

	@Override
	public void onScroll(int maxOffset) {
		if (updating)
			return;
		updating = true;
		IAbsolutePanel card2use = null;
		int rowBase = maxOffset / MIN_HEIGHT;
		if (rowBase >= heights.length - MAX_ROWS_PER_PAGE) {
			card2use = card3;
			if (!card3painted) {
				paintCard3(card2use);
			}
		} else {
			if (card1 != active) {
				card2use = card1;
			} else
				card2use = card2;
			card2use.clear();
			paint(maxOffset, card2use, rowBase);
		}
		cardPanel.show(card2use);
		active = card2use;
		updating = false;
	}

	private void paintCard3(IAbsolutePanel card2use) {
		int height = paint((heights.length - MAX_ROWS_PER_PAGE) * MIN_HEIGHT,
				card2use, heights.length - MAX_ROWS_PER_PAGE);
		card1.height(height);
		card2.height(height);
		card3.height(height);
		card3painted = true;
	}

	private int paint(int maxOffset, IAbsolutePanel card2use, int rowBase) {
		for (int i = 0; i < VISIBLE_HEIGHT / MIN_HEIGHT
				&& i + rowBase < heights.length; i++) {
			int row = i + rowBase;
			int h = heights[row];
			IVerticalPanel p = card2use.add().panel().vertical().size(200, h);
			p.add().label().text(String.valueOf(row));
			if (row % 2 == 0)
				p.color().lightgray();
			else {
				p.color().white();
				p.border();
			}
			card2use.offset(p, 0, maxOffset);
			maxOffset += h;
		}
		return maxOffset;
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new ScrollTable3CardWidgetTest().run(display);
	}
}

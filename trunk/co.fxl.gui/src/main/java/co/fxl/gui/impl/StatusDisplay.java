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

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IScrollPane.IScrollListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.log.impl.Log;

public class StatusDisplay implements IResizeListener, Runnable,
		IScrollListener {

	public interface RefreshListener {

		public void refresh(ICallback<Void> cb);
	}

	// private static final int RESIZE_INTERVALL_MS = 250;
	private static final boolean LOG_ILLEGAL_SIZES = false;
	// public static boolean SINGLE_RESIZE_LISTENER = false;
	private static StatusDisplay instance = new StatusDisplay();
	private IDisplay display = Display.instance();
	private IVerticalPanel panel;
	private IScrollPane scrollPane;
	private IVerticalPanel p0;

	private StatusDisplay() {
		// if (!SINGLE_RESIZE_LISTENER)
		Display.instance().addResizeListener(this);
	}

	public static StatusDisplay instance() {
		return instance;
	}

	// private Long lastResize = null;
	protected IPopUp popUp;
	private IGridCell sidePanelContainer;
	private IGridPanel grid;
	private IScrollPane sidePanel;
	private int scrollOffset;

	// public IResizeConfiguration singleResizeListener(
	// RefreshListener resizeListener) {
	// IResizeConfiguration adp = DisplayResizeAdapter.addResizeListener(
	// wrap(resizeListener), false);
	// return adp;
	// }
	//
	// private IResizeListener wrap(final RefreshListener resizeListener) {
	// // return resizeListener;
	// return new IResizeListener() {
	//
	// @Override
	// public boolean onResize(int width, int height) {
	// lastResize = System.currentTimeMillis();
	// if (popUp == null) {
	// popUp = StatusPanel.showPopUp("Resizing...", 0,
	// StatusPanel.BACKGROUND, StatusPanel.FOREGROUND,
	// false, false);
	// forkResize();
	// }
	// return true;
	// }
	//
	// private void forkResize() {
	// Display.instance().invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// if (System.currentTimeMillis() - lastResize > RESIZE_INTERVALL_MS) {
	// resizeListener
	// .refresh(new CallbackTemplate<Void>() {
	// @Override
	// public void onSuccess(Void result) {
	// popUp.visible(false);
	// popUp = null;
	// }
	// });
	// } else {
	// forkResize();
	// }
	// }
	// }, RESIZE_INTERVALL_MS);
	// }
	// };
	// }

	IResizeConfiguration addResizeListener(IResizeListener resizeListener) {
		return addResizeListener(resizeListener, false);
	}

	// private IResizeConfiguration newDummyResizeConfiguration() {
	// return new IResizeConfiguration() {
	//
	// @Override
	// public IResizeConfiguration singleton() {
	// return this;
	// }
	//
	// @Override
	// public IResizeConfiguration linkLifecycle(IElement<?> element) {
	// return this;
	// }
	//
	// @Override
	// public IResizeConfiguration linkLifecycle(IPopUp dialog) {
	// return this;
	// }
	//
	// @Override
	// public void remove() {
	// }
	// };
	// }

	private IResizeConfiguration addResizeListener(
			IResizeListener resizeListener, boolean fire) {
		// if (SINGLE_RESIZE_LISTENER) {
		// if (fire) {
		// fire(resizeListener);
		// }
		// return newDummyResizeConfiguration();
		// }
		return DisplayResizeAdapter.addResizeListener(resizeListener, fire);
	}

	public void fire(IResizeListener resizeListener) {
		resizeListener.onResize(width(), height());
	}

	// private void autoResize(final IElement<?> e) {
	// autoResize(e, 0);
	// }
	//
	// private void autoResize(final IElement<?> e, final int dec) {
	// autoResize(e, dec, false);
	// }
	//
	// private void autoResize(final IElement<?> e, final int dec, boolean b) {
	// final IResizeListener listener = new IResizeListener() {
	// @Override
	// public void onResize(int width, int height) {
	// int offsetY = offsetY(e, 100);
	// int h = height - offsetY - 10 - dec;
	// if (h > 0)
	// e.height(h);
	// }
	// };
	// // if (!SINGLE_RESIZE_LISTENER)
	// Display.instance().addResizeListener(listener).linkLifecycle(e);
	// Runnable runnable = new Runnable() {
	// @Override
	// public void run() {
	// fire(listener);
	// }
	// };
	// if (b) {
	// Display.instance().invokeLater(runnable);
	// } else
	// runnable.run();
	// }

	public int offsetY(IElement<?> e, int min) {
		return offsetY(e.offsetY(), min);
	}

	public void warning(String warning) {
		final IGridPanel g = p0.add().panel().grid().spacing(6);
		g.color().rgb(192, 0, 0);
		g.cell(0, 0).label().text(warning).autoWrap(true).font().pixel(11)
				.weight().bold().color().white();
		ILabel l = g.cell(1, 0).valign().begin().align().end().label()
				.text("[x]");
		l.font().pixel(11).weight().bold().color().white();
		l.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				g.remove();
				DisplayResizeAdapter.setDecrement("warning", 0, true);
				Constants.put("RelationDecorator.DECREMENT", 1);
			}
		});

		// TODO DisplayResizeAdapter.setDecrement: elements übergeben, nicht
		// absolute größen

		DisplayResizeAdapter.setDecrement("warning", g.height() + 1);
	}

	public IVerticalPanel panel() {
		return p0;
	}

	public int offsetY(int offsetY, int min) {
		if (offsetY == 0) {
			if (LOG_ILLEGAL_SIZES)
				Log.instance().error("Vertical offset is 0");
			return DisplayResizeAdapter.withDecrement(min);
		}
		if (offsetY != min && LOG_ILLEGAL_SIZES) {
			Log.instance().error(
					"Vertical offset is different than anticipated " + offsetY
							+ "!=" + min);
		}
		return offsetY;
	}

	public int width() {
		int w = hasHorizontalScrollbar() ? 1024 : display.width();
		if (sidePanelContainer != null)
			w -= sidePanelContainer.width();
		return w;
	}

	private boolean hasHorizontalScrollbar() {
		return display.width() < 1024;
	}

	public int height() {
		return display.height()
				- (hasHorizontalScrollbar() ? Env.HEIGHT_SCROLLBAR : 0);
	}

	public StatusDisplay reset() {
		scrollPane = display.clear().container().scrollPane().iD("base")
				.horizontal();
		scrollPane.addScrollListener(this);
		grid = scrollPane.viewPort().panel().grid();
		panel = grid.cell(0, 0).panel().vertical().align().begin();
		run();
		stylePanel(panel);
		p0 = panel.add().panel().vertical().align().begin().add().panel()
				.vertical().align().begin();
		return this;
	}

	public void resetPanelDimensions() {
		if (panel != null) {
			run();
			if (Env.is(Env.CHROME)) {
				Display.instance().invokeLater(this);
			}
		}
	}

	@Override
	public void run() {
		panel.size(width(), height());
		scrollPane.size(display.width(), display.height());
		if (sidePanel != null)
			sidePanel.height(display.height());
	}

	@Override
	public void onResize(int width, int height) {
		resetPanelDimensions();
	}

	public void stylePanel(IPanel<?> panel) {
		panel.color().rgb(245, 245, 245);
	}

	public void visible(boolean b) {
		panel.visible(b);
	}

	public void updateHeight() {
		panel.height(height());
		// display.height(display.height());
	}

	public IScrollPane showSidePanel(final int width) {
		sidePanelContainer = grid.cell(1, 0);
		sidePanel = sidePanelContainer.width(width).scrollPane()
				.size(width, display.height());
		sidePanel.border().style().left().color().gray();
		notifyResizeListeners();
		return sidePanel;
	}

	public int scrollOffset() {
		return scrollPane != null ? scrollOffset : 0;
	}

	private void notifyResizeListeners() {
		((DisplayTemplate) Display.instance()).notifyResizeListeners();
	}

	public StatusDisplay hideSidePanel() {
		// sidePanel.remove();
		sidePanelContainer.clear().width(0);
		sidePanelContainer = null;
		sidePanel = null;
		notifyResizeListeners();
		return this;
	}

	@Override
	public void onScroll(int maxOffset) {
		scrollOffset = maxOffset;
	}

}

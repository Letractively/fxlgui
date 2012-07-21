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

import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;

public class StatusDisplay implements IResizeListener, Runnable {

	// private static final int _250 = 250;
	public static boolean SINGLE_RESIZE_LISTENER = Env.is(Env.CHROME);
	private static StatusDisplay instance = new StatusDisplay();
	private IDisplay display = Display.instance();
	private IVerticalPanel panel;
	private IScrollPane scrollPane;
	private IVerticalPanel p0;

	private StatusDisplay() {
		if (!SINGLE_RESIZE_LISTENER)
			Display.instance().addResizeListener(this);
	}

	public static StatusDisplay instance() {
		return instance;
	}

	// private Long lastResize = null;
	// protected IPopUp popUp;

	public IResizeConfiguration singleResizeListener(
			final IResizeListener resizeListener) {
		IResizeConfiguration adp = DisplayResizeAdapter.addResizeListener(
				wrap(resizeListener), false);
		return adp;
	}

	private IResizeListener wrap(final IResizeListener resizeListener) {
		return resizeListener;
		// return new IResizeListener() {
		//
		// @Override
		// public boolean onResize(int width, int height) {
		// lastResize = System.currentTimeMillis();
		// if (popUp == null) {
		// popUp = StatusPanel.showPopUp(
		// StatusPanel.pleaseWait("Recalibrating UI"), 0,
		// StatusPanel.BACKGROUND, StatusPanel.FOREGROUND,
		// false, true);
		// forkResize();
		// }
		// return true;
		// }
		//
		// private void forkResize() {
		// Display.instance().invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// if (System.currentTimeMillis() - lastResize > _250) {
		// popUp.visible(false);
		// popUp = null;
		// resizeListener.onResize(width(), height());
		// } else {
		// forkResize();
		// }
		// }
		// }, _250);
		// }
		// };
	}

	public IResizeConfiguration addResizeListener(IResizeListener resizeListener) {
		return addResizeListener(resizeListener, false);
	}

	private IResizeConfiguration newDummyResizeConfiguration() {
		return new IResizeConfiguration() {

			@Override
			public IResizeConfiguration singleton() {
				return this;
			}

			@Override
			public IResizeConfiguration linkLifecycle(IElement<?> element) {
				return this;
			}

			@Override
			public IResizeConfiguration linkLifecycle(IPopUp dialog) {
				return this;
			}
		};
	}

	public IResizeConfiguration addResizeListener(
			IResizeListener resizeListener, boolean fire) {
		if (SINGLE_RESIZE_LISTENER) {
			if (fire) {
				fire(resizeListener);
			}
			return newDummyResizeConfiguration();
		}
		return DisplayResizeAdapter.addResizeListener(resizeListener, fire);
	}

	public void fire(IResizeListener resizeListener) {
		resizeListener.onResize(width(), height());
	}

	public void autoResize(final IElement<?> e) {
		autoResize(e, 0);
	}

	public void autoResize(final IElement<?> e, final int dec) {
		autoResize(e, dec, false);
	}

	public void autoResize(final IElement<?> e, final int dec, boolean b) {
		final IResizeListener listener = new IResizeListener() {
			@Override
			public boolean onResize(int width, int height) {
				if (!e.visible()) {
					return false;
				}
				int offsetY = offsetY(e, 100);
				int h = height - offsetY - 10 - dec;
				if (h > 0)
					e.height(h);
				return e.visible();
			}
		};
		if (!SINGLE_RESIZE_LISTENER)
			Display.instance().addResizeListener(listener);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				fire(listener);
			}
		};
		if (b) {
			Display.instance().invokeLater(runnable);
		} else
			runnable.run();
	}

	public int offsetY(IElement<?> e, int min) {
		return Math.max(e.offsetY(), DisplayResizeAdapter.withDecrement(min));
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
		return Math.max(offsetY, DisplayResizeAdapter.withDecrement(min));
	}

	public int width() {
		return display.width();
	}

	public int height() {
		return display.height();
	}

	public StatusDisplay reset() {
		scrollPane = display.clear().container().scrollPane().horizontal();
		panel = scrollPane.viewPort().panel().vertical();
		run();
		stylePanel(panel);
		p0 = panel.add().panel().vertical().add().panel().vertical();
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
	}

	@Override
	public boolean onResize(int width, int height) {
		resetPanelDimensions();
		return true;
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

}

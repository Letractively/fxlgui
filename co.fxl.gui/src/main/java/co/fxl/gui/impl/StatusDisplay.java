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
import co.fxl.gui.api.IVerticalPanel;

public class StatusDisplay implements IResizeListener {

	private static StatusDisplay instance = new StatusDisplay();
	private IDisplay display = Display.instance();
	private IVerticalPanel panel;

	private StatusDisplay() {
		Display.instance().addResizeListener(this);
	}

	public static StatusDisplay instance() {
		return instance;
	}

	public IResizeConfiguration addResizeListener(IResizeListener resizeListener) {
		return DisplayResizeAdapter.addResizeListener(resizeListener);
	}

	public IResizeConfiguration addResizeListener(
			IResizeListener resizeListener, boolean b) {
		return DisplayResizeAdapter.addResizeListener(resizeListener, b);
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
		Display.instance().addResizeListener(listener);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				DisplayResizeAdapter.fire(listener);
			}
		};
		if (b) {
			Display.instance().invokeLater(runnable);
		} else
			runnable.run();
	}

	public int offsetY(IElement<?> e, int min) {
		return Math.max(e.offsetY(), min);
	}

	public void warning(String warning) {
		final IGridPanel g = panel.add().panel().grid().spacing(6);
		g.color().rgb(192, 0, 0);
		g.cell(0, 0).label().text(warning).autoWrap(true).font().pixel(11)
				.weight().bold().color().white();
		ILabel l = g.cell(1, 0).valign().begin().align().end().label()
				.text("[x]");
		l.font().pixel(11).weight().bold().color().white();
		l// .image().resource("cancel_white.png").size(16, 16)
		.addClickListener(new IClickListener() {
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
		return panel;
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
		panel = display.clear().container().panel().vertical();
		return this;
	}

	public void resetPanelDimensions() {
		if (panel != null) {
			final int height = StatusDisplay.instance().height();
			panel.width(-1).width(1.0).height(height);
			if (Env.is(Env.CHROME)) {
				final int width = StatusDisplay.instance().width();
				Display.instance().invokeLater(new Runnable() {
					@Override
					public void run() {
						panel.size(width, height);
					}
				});
			}
		}
	}

	@Override
	public boolean onResize(int width, int height) {
		resetPanelDimensions();
		return true;
	}

}

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

import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.log.impl.Log;

public class StatusPanel {

	private static final ColorMemento FOREGROUND = new ColorMemento();
	private static final ColorMemento BACKGROUND = new ColorMemento(249, 237,
			190);
	private static final String LOADING = "Loading ";
	public static final double FADE_IN_MS = 1000;
	private static boolean RESIZE = Constants.get("StatusPanel.RESIZE", true);
	private static boolean BEFORE = Constants.get("StatusPanel.BEFORE", true);
	private IPopUp lastPopUp;
	private String lastStatus;
	private static StatusPanel instance;
	private ColorMemento color = BACKGROUND;
	private ColorMemento fontColor = FOREGROUND;
	private boolean bold;

	public StatusPanel() {
		instance = this;
	}

	public StatusPanel start(String status) {
		if (Log.ENABLED)
			Log.instance().start(LOADING + status);
		lastStatus = status;
		lastPopUp = showLoadingPopUp(LOADING + status, 0, color, fontColor,
				bold);
		return this;
	}

	private void stop(String status) {
		if (Log.ENABLED)
			Log.instance().stop(LOADING + status);
		if (lastPopUp != null && lastStatus.equals(status)) {
			lastPopUp.visible(false);
			lastPopUp = null;
		}
	}

	public void stop() {
		stop(lastStatus);
	}

	public IColor color() {
		return color;
	}

	public IColor fontColor() {
		return fontColor;
	}

	public static IPopUp showPopUp(String info, int y) {
		return showLoadingPopUp(info, y, BACKGROUND, FOREGROUND, false);
	}

	private static IPopUp showLoadingPopUp(String info, int y, ColorMemento m,
			ColorMemento fm, boolean bold) {
		return showPopUp("Please wait - " + info + "...", y, m, fm, bold);
	}

	private static IPopUp showPopUp(String info, int y, ColorMemento m,
			ColorMemento fm, boolean bold) {
		final IPopUp dialog = Display.instance().showPopUp().modal(true)
				.glass(false);
		dialog.border().remove();
		IBorder b = dialog.border();
		b.style().shadow(2);
		b.color().rgb(240, 195, 109);
		IHorizontalPanel spacing = dialog.container().panel().horizontal()
				.spacing(5);
		IFont f = spacing.addSpace(4).add().label().text(info).font().pixel(11);
		if (bold)
			f.weight().bold();
		fm.forward(f.color());
		spacing.addSpace(4);
		m.forward(spacing.color());
		resize(Display.instance().width(), dialog);
		if (RESIZE)
			Display.instance().addResizeListener(new IResizeListener() {
				@Override
				public boolean onResize(int width, int height) {
					resize(width, dialog);
					return dialog.visible();
				}
			}).linkLifecycle(dialog);
		return dialog;
	}

	public static StatusPanel instance() {
		if (instance == null)
			instance = new StatusPanel();
		return instance;
	}

	public static StatusPanel newInstance() {
		return new StatusPanel();
	}

	private static void resize(int width, final IPopUp dialog) {
		if (BEFORE)
			dialog.visible(true);
		int x = (width - dialog.width()) / 2;
		dialog.offset(x, DisplayResizeAdapter.decrement() + 4);
		dialog.visible(true);
	}

	public StatusPanel bold(boolean b) {
		bold = b;
		return this;
	}
}

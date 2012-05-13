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
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDisplay.IResizeListener;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.log.impl.Log;

public class StatusPanel {

	private static final String LOADING = "Loading ";
	public static final double FADE_IN_MS = 1000;
	private IPopUp lastPopUp;
	private String lastStatus;
	private static StatusPanel instance;
	private ColorMemento color = new ColorMemento(255, 240, 170);
	private ColorMemento fontColor = new ColorMemento();
	private boolean bold;

	public StatusPanel() {
		instance = this;
	}

	public StatusPanel start(String status) {
		lastStatus = status;
		if (Log.ENABLED)
			Log.instance().start(LOADING + status);
		lastPopUp = showLoadingPopUp(Display.instance(), LOADING + status,
				true, 0, color, fontColor, bold);
		return this;
	}

	public StatusPanel status(String status) {
		lastStatus = status;
		return this;
	}

	public StatusPanel visible(boolean visible) {
		if (visible)
			lastPopUp = showPopUp(Display.instance(), lastStatus, false, 0,
					color, fontColor, bold);
		else {
			lastPopUp.visible(false);
			lastPopUp = null;
		}
		return this;
	}

	public IColor color() {
		return color;
	}

	public IColor fontColor() {
		return fontColor;
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

	public static IPopUp showPopUp(IDisplay display, String info,
			boolean modal, int y) {
		return showLoadingPopUp(display, info, modal, y, new ColorMemento(255,
				240, 170), new ColorMemento(), false);
	}

	public static IPopUp showPopUp(IDisplay display, String info,
			boolean modal, int y, boolean bold) {
		return showLoadingPopUp(display, info, modal, y, new ColorMemento(255,
				240, 170), new ColorMemento(), bold);
	}

	private static IPopUp showLoadingPopUp(IDisplay display, String info,
			boolean modal, int y, ColorMemento m, ColorMemento fm, boolean bold) {
		return showPopUp(display, "Please wait - " + info + "...", modal, y, m,
				fm, bold);
	}

	private static IPopUp showPopUp(final IDisplay display, String info,
			boolean modal, int y, ColorMemento m, ColorMemento fm, boolean bold) {
		final IPopUp dialog = display.showPopUp().modal(modal).glass(false);
		if (!modal)
			dialog.autoHide(false);
//		if (m.rgb[0] > 220 && m.rgb[1] > 220 && m.rgb[2] > 220)
//			dialog.border().color().gray();
		dialog.border().remove();
		IBorder b = dialog.border();
		b.style().shadow();
		IHorizontalPanel spacing = dialog.container().panel().horizontal()
				.spacing(5);
		IFont f = spacing.addSpace(4).add().label().text(info).font().pixel(11);
		if (bold)
			f.weight().bold();
		fm.forward(f.color());
		spacing.addSpace(4);
		m.forward(spacing.color());
		resize(display.width(), dialog);
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
		int x = (width - dialog.width()) / 2;
		dialog.offset(x, DisplayResizeAdapter.decrement() + 4).visible(true);
	}

	public StatusPanel bold(boolean b) {
		bold = b;
		return this;
	}
}

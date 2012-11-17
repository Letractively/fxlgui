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

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.impl.ServerListener.Type;
import co.fxl.gui.log.impl.Log;

public class StatusPanel {

	static final ColorMemento FOREGROUND = new ColorMemento();
	static final ColorMemento BACKGROUND = new ColorMemento(249, 237, 190);
	private static final String LOADING = "Loading ";
	public static final double FADE_IN_MS = 1000;
	private static boolean RESIZE = !Env.is(Env.SWING);
	private static boolean BEFORE = !Env.is(Env.SWING);
	private IPopUp lastPopUp;
	private static StatusPanel instance;
	ColorMemento color = BACKGROUND;
	ColorMemento fontColor = FOREGROUND;
	private String text;

	private StatusPanel() {
		if (instance != null) {
			instance.hide();
		}
		instance = this;
	}

	public StatusPanel start(String status) {
		ServerListener.notifyCall(Type.LOADING);
		String text = LOADING + status;
		if (Log.ENABLED)
			Log.instance().start(text);
		startAction(text);
		return this;
	}

	StatusPanel startAction(String text) {
		this.text = text;
		lastPopUp = showLoadingPopUp(text, 0, color, fontColor, false);// bold);
		return this;
	}

	void hide() {
		if (lastPopUp != null)
			lastPopUp.visible(false);
		lastPopUp = null;
		instance = null;
	}

	public void stop() {
		ServerListener.notifyReturn();
		if (Log.ENABLED)
			Log.instance().stop(text);
		hide();
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
		return showPopUp(pleaseWait(info), y, m, fm, bold);
	}

	public static String pleaseWait(String info) {
		return "Please wait - " + info + "...";
	}

	static IPopUp showPopUp(String info, int y, ColorMemento m,
			ColorMemento fm, boolean bold) {
		return showPopUp(info, y, m, fm, bold, false);
	}

	static IPopUp showPopUp(String info, int y, ColorMemento m,
			ColorMemento fm, boolean bold, boolean glass) {
		final IPopUp dialog = Display.instance().showPopUp().modal(true)
				.glass(glass);
		dialog.border().remove().style().shadow(2).color().rgb(240, 195, 109);
		IHorizontalPanel spacing = dialog.container().panel().horizontal()
				.spacing(5);
		IFont f = spacing.addSpace(4).add().label().text(info).font().pixel(11);
		if (bold)
			f.weight().bold();
		fm.forward(f.color());
		spacing.addSpace(4);
		m.forward(spacing.color());
		resize(StatusDisplay.instance().width(), dialog);
		if (RESIZE)
			Display.instance().addResizeListener(new IResizeListener() {
				@Override
				public void onResize(int width, int height) {
					resize(StatusDisplay.instance().width(), dialog);
				}
			}).linkLifecycle(dialog);
		return dialog;
	}

	public static StatusPanel instance() {
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

	void text(String textStatusPanel) {
		throw new UnsupportedOperationException();
	}
}

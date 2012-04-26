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
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IPopUp;

public class StatusPanel {

	private IPopUp lastPopUp;
	private String lastStatus;
	private static StatusPanel instance;

	public StatusPanel() {
		instance = this;
	}

	public StatusPanel start(String status) {
		lastStatus = status;
		lastPopUp = showPopUp(Display.instance(), "Loading " + status, true, 0);
		return this;
	}

	private void stop(String status) {
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
		IPopUp dialog = display.showPopUp().modal(true).glass(false);
		dialog.border().remove();
		IBorder b = dialog.border();
		b.style().shadow();
		IHorizontalPanel spacing = dialog.container().panel().horizontal()
				.spacing(5);
		spacing.color().rgb(255, 240, 170);
		spacing.addSpace(4).add().label().text("Please wait - " + info + "...")
				.font().pixel(11);
		spacing.addSpace(4);
		// dialog.center();
		int x = (display.width() - dialog.width()) / 2;
		dialog.offset(x, 4);
		dialog.visible(true);
		x = (display.width() - dialog.width()) / 2;
		dialog.offset(x, 4);
		return dialog;
	}

	public static StatusPanel instance() {
		if (instance == null)
			instance = new StatusPanel();
		return instance;
	}
}

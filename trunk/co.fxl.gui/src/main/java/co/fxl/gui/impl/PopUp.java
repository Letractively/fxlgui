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

import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

public class PopUp {

	private static IDisplay display = Display.instance();
	private static List<IPopUp> visiblePopUps = new LinkedList<IPopUp>();
	private static boolean active = false;

	public static void activate() {
		active = true;
	}

	public static void closeAll() {
		for (IPopUp popUp : new LinkedList<IPopUp>(visiblePopUps))
			popUp.visible(false);
	}

	public static IPopUp showPopUp() {
		final IPopUp popUp = display.showPopUp();
		if (active) {
			visiblePopUps.add(popUp);
			popUp.addVisibleListener(new IUpdateListener<Boolean>() {
				@Override
				public void onUpdate(Boolean value) {
					if (!value) {
						visiblePopUps.remove(popUp);
					}
				}
			});
		}
		return popUp;
	}

	public static IDialog showDialog() {
		return display.showDialog();
	}
}

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

public class ServerListener {

	private static final int TOLERANCE = 50;
	private static final int FADE_OUT = 200;
	private static final boolean SHOW_LOADING_LAZY = false;

	public enum Type {

		SAVING, LOADING
	}

	public static IServerListener instance = null;
	public static Type currentActivity;
	public static int callStack = 0;
	public static StatusPanel currentStatusPanel;
	public static boolean scheduled = false;

	public static void notifyCall(Type type) {
		if (instance != null)
			instance.notifyServerCallStart();
		if (!SHOW_LOADING_LAZY)
			return;
		callStack++;
		if (type.equals(currentActivity))
			return;
		currentActivity = type;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (!scheduled)
					return;
				scheduled = false;
				if (StatusPanel.instance() == null && currentActivity != null) {
					currentStatusPanel = StatusPanel.newInstance().startAction(
							textStatusPanel());
				}
			}
		};
		if (currentStatusPanel != null) {
			currentStatusPanel.hide();
			runnable.run();
			return;
		}
		if (!scheduled) {
			scheduled = true;
			Display.instance().invokeLater(runnable, TOLERANCE);
		}
	}

	public static void notifyReturn() {
		if (instance != null)
			instance.notifyServerCallReturn();
		if (!SHOW_LOADING_LAZY)
			return;
		callStack--;
		if (callStack == 0) {
			scheduled = false;
			if (currentStatusPanel != null) {
				if (currentStatusPanel == StatusPanel.instance())
					Display.instance().invokeLater(new Runnable() {
						@Override
						public void run() {
							if (currentStatusPanel != null)
								currentStatusPanel.hide();
							currentStatusPanel = null;
						}
					}, FADE_OUT);
				else {
					currentStatusPanel = null;
					currentActivity = null;
				}
			}
			currentActivity = null;
		}
	}

	private static String textStatusPanel() {
		return currentActivity.equals(Type.LOADING) ? "Loading" : "Saving";
	}

}

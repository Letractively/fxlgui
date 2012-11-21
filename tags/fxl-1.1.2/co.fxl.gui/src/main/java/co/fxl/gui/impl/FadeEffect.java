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

public class FadeEffect {

	// public static void addFadeEffect(final IElement<?> e, IFocusPanel h) {
	// addFadeEffect(e, h, 300, 4);
	// }

	// public static void addFadeEffect(final IElement<?> e, IFocusPanel h,
	// final int l, final int steps) {
	// e.opacity(0);
	// h.addMouseOverListener(new IMouseOverListener() {
	//
	// private double opacity = 0;
	// private double targetOpacity = 1;
	// private boolean scheduled;
	//
	// @Override
	// public void onMouseOver() {
	// schedule(1);
	// }
	//
	// private void schedule() {
	// if (targetOpacity != opacity) {
	// scheduled = true;
	// Display.instance().invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// opacity += opacity > targetOpacity ? -(1d / steps)
	// : (1d / steps);
	// e.opacity(opacity);
	// schedule();
	// }
	// }, l / steps);
	// } else
	// scheduled = false;
	// }
	//
	// @Override
	// public void onMouseOut() {
	// schedule(0);
	// }
	//
	// private void schedule(int target) {
	// targetOpacity = target;
	// if (!scheduled)
	// schedule();
	// }
	// });
	// }

}

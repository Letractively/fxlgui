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

import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IColored.IColor;

public class YellowFadeIn {

	private static long INC = 100;
	private static long DURATION = 1000;
	private long d = 0;
	private IColor color;
	private boolean abort;

	public YellowFadeIn(IColored colored) {
		color = colored.color();
		schedule();
	}

	private int c(long d, int rgb) {
		double n = d;
		n /= DURATION;
		n *= (255 - rgb);
		n += rgb;
		return (int) n;
	}

	private void schedule() {
		d += INC;
		int r = c(d, StatusPopUp.YELLOW_R);
		int g = c(d, StatusPopUp.YELLOW_G);
		int b = c(d, StatusPopUp.YELLOW_B);
		color.rgb(r, g, b);
		if (d < DURATION && !abort)
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					schedule();
				}
			}, INC);
		else
			color.white();
	}

	public void stop() {
		abort = true;
	}
}

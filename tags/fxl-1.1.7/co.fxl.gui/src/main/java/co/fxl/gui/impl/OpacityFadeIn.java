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

import co.fxl.gui.api.IElement;

public class OpacityFadeIn implements FadeIn {

	private static double INC = 50;
	private static double DURATION = 500;
	private double d = 250;
	private boolean abort;
	private IElement<?> element;

	public OpacityFadeIn(IElement<?> element) {
		this.element = element;
		schedule();
	}

	private void schedule() {
		d += INC;
		element.opacity(d / DURATION);
		if (d < DURATION && !abort)
			Display.instance().invokeLater(new Runnable() {
				@Override
				public void run() {
					schedule();
				}
			}, (long) INC);
		else
			element.opacity(1);
	}

	@Override
	public void stop() {
		abort = true;
	}
}

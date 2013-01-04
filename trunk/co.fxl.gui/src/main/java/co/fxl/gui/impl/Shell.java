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

import co.fxl.gui.api.IDisplay.IResizeConfiguration;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IResizable.IResizeListener;
import co.fxl.gui.api.IScrollPane;

public class Shell {

	private static StatusDisplay d = StatusDisplay.instance();
	private static Shell shell = new Shell();

	public static Shell instance() {
		return shell;
	}

	public int width(IElement<?> e) {
		assert e != null;
		return e.shell().width();
	}

	public int height(IElement<?> e) {
		assert e != null;
		return e.shell().height();
	}

	public int offsetY(IElement<?> e, int i) {
		return d.offsetY(e, i);
	}

	public int offsetY(int offsetY, int i) {
		return d.offsetY(offsetY, i);
	}

	public void fire(IResizeListener listener) {
		d.fire(listener);
	}

	public IResizeConfiguration addResizeListener(
			IResizeListener iResizeListener) {
		return d.addResizeListener(iResizeListener);
	}

	public void hideSidePanel() {
		d.hideSidePanel();
	}

	public StatusDisplay reset() {
		return d.reset();
	}

	public void resetPanelDimensions() {
		d.resetPanelDimensions();
	}

	public int scrollOffset() {
		return d.scrollOffset();
	}

	public IScrollPane showSidePanel(int i) {
		return d.showSidePanel(i);
	}

	public void updateHeight() {
		d.updateHeight();
	}

	public void visible(boolean b) {
		d.visible(b);
	}

	public void warning(String warning) {
		d.warning(warning);
	}

	public int dwidth() {
		return d.width();
	}

	public int dheight() {
		return d.height();
	}

}

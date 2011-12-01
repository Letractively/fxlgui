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
package co.fxl.gui.style.gplus;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.api.IStyle.IWindow;

class GPlusWindow implements IWindow {

	@Override
	public IWindow main(IPanel<?> panel) {
		panel.color().white();
		panel.border().remove();
		return this;
	}

	@Override
	public IWindow header(IPanel<?> panel) {
		panel.color().remove();
		panel.border().color().mix().lightgray().gray();
		panel.border().width(1);
		panel.border().style().bottom();
		return this;
	}

	@Override
	public IWindow footer(IPanel<?> panel) {
		panel.color().remove();
		panel.border().remove();
		return this;
	}

	@Override
	public IWindow title(ILabel label, String title, boolean isSideWidget) {
		label.text(title);
		if (isSideWidget)
			label.font().color().gray();
		else
			label.font().color().black();
		return this;
	}

	@Override
	public IWindow navigationEntry(ILinearPanel<?> panel) {
		panel.spacing(0).border().remove();
		return this;
	}

	@Override
	public IWindow button(IPanel<?> panel, boolean isSideWidget) {
		if (isSideWidget) {
			panel.color().remove();
			panel.border().remove();
		} else {
			panel.color().mix().white().lightgray();
			panel.border().color().lightgray();
		}
		return this;
	}

	@Override
	public ILabel addCommandLabel(ILinearPanel<?> panel, String text,
			boolean isSideWidget) {
		if (isSideWidget)
			return panel.add().label().visible(false);
		else {
			ILabel l = panel.addSpace(4).add().label().text(text);
			l.font().pixel(12);
			return l;
		}
	}

	@Override
	public boolean commandsOnTop() {
		return true;
	}
}
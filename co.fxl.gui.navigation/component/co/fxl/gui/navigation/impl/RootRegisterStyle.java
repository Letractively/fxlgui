/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
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
 */
package co.fxl.gui.navigation.impl;

import co.fxl.gui.api.IPanel;
import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.register.api.IRegister.ITitle;
import co.fxl.gui.register.impl.RegisterWidgetImpl;

class RootRegisterStyle extends RegisterStyle {

	@Override
	void decorateWidget(RegisterWidgetImpl widget) {
		background(widget.headerPanel.color());
		IPanel<?> filler = widget.addFillerPanel();
		background(filler.color());
		widget.separators(false);
	}

	private void background(IColor color) {
		color.rgb(0, 51, 102);
	}

	@Override
	void init(ITitle title) {
		background(title.border().width(2).color());
		title.color().rgb(228, 228, 255);
		title.font().weight().bold().pixel(14).color().black();
	}

	@Override
	public void onBack(ITitle title) {
		background(title.border().width(2).color());
		title.color().rgb(228, 228, 255);
		title.font().color().black();
	}

	@Override
	public void onFront(ITitle title) {
		title.border().width(2).color().white();
		title.color().white();
		title.font().color().black();
	}
}

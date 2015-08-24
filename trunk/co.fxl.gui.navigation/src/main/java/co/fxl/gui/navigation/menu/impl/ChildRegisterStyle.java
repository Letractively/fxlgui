/**
 * Copyright (c) 2010-2015 Dangelmayr IT GmbH. All rights reserved.
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
package co.fxl.gui.navigation.menu.impl;

import co.fxl.gui.api.IColored.IColor;
import co.fxl.gui.register.api.IRegister.ITitle;
import co.fxl.gui.register.impl.RegisterWidgetImpl;
import co.fxl.gui.register.impl.RegisterWidgetImpl.ColorDecorator;

class ChildRegisterStyle extends RegisterStyle {

	@Override
	void decorateWidget(RegisterWidgetImpl widget) {
		widget.separators(false);
		widget.outerSpacing(0);
		// widget.headerPanel.spacing(4);
		widget.background(new ColorDecorator() {
			@Override
			public void decorate(IColor color) {
				color.rgb(240, 240, 240);
			}

		});
	}

	@Override
	void init(ITitle title) {
		onBack(title);
		title.border().width(1).color().lightgray();
		// title.color().white();
	}

	@Override
	public void onBack(ITitle title) {
		title.font().pixel(11).weight().plain().color().blue();
		title.font().underline(true);
	}

	@Override
	public void onFront(ITitle title) {
		title.font().pixel(11).weight().plain().color().black();
		title.font().underline(false);
	}
}

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

import co.fxl.gui.register.api.IRegister.ITitle;
import co.fxl.gui.register.impl.RegisterWidgetImpl;

class RootRegisterStyle extends RegisterStyle {

	// private RegisterWidgetImpl widget;

	@Override
	void decorateWidget(RegisterWidgetImpl widget) {
		// this.widget = widget;
		widget.headerPanel.color().rgb(200, 200, 200);
		// IPanel<?> filler = widget.addFillerPanel();
		// background(filler.color());
		widget.separators(false);
	}

	// private void background(IColor color) {
	// widget.background(color);
	// }

	@Override
	void init(ITitle title) {
		onBack(title);
	}

	@Override
	public void onBack(ITitle title) {
		// title.border().width(2).color().rgb(245, 245, 245);
		title.color().mix().rgb(200, 200, 200);
		title.font().underline(false).weight().plain().color().black();// .color().black();
	}

	@Override
	public void onFront(ITitle title) {
		// background(title.border().width(2).color());
		title.color().rgb(247, 247, 247);
		title.font().underline(false).weight().bold().color().black();// .color().white();
	}
}

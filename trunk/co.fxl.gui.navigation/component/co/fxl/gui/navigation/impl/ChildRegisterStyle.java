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

class ChildRegisterStyle extends RegisterStyle {

	@Override
	void decorateWidget(RegisterWidgetImpl widget) {
		widget.separators(true);
		widget.headerPanel.spacing(2);
	}

	@Override
	void init(ITitle title) {
		title.font().pixel(12);
	}

	@Override
	public void onBack(ITitle title) {
		title.font().color().gray();
		title.font().underline(false);
	}

	@Override
	public void onFront(ITitle title) {
		title.font().color().black();
		title.font().underline(true);
	}
}

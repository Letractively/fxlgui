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

import co.fxl.gui.register.api.IRegister.ITitle;
import co.fxl.gui.register.impl.RegisterWidgetImpl;
import co.fxl.gui.style.impl.Style;

public class RootRegisterStyle extends RegisterStyle {

	@Override
	public void decorateWidget(RegisterWidgetImpl widget) {
		widget.separators(false);
	}

	@Override
	void init(ITitle title) {
		onBack(title);
	}

	@Override
	public void onBack(ITitle title) {
		Style.instance()
				.register()
				.inactive(title.color(), title.border(), title.font(),
						title.isClickable(), title.isEmpty());
	}

	@Override
	public void onFront(ITitle title) {
		Style.instance().register()
				.active(title.color(), title.border(), title.font());
	}
}

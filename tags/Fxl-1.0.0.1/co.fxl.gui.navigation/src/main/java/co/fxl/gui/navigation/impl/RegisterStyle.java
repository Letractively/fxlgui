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

abstract class RegisterStyle {

	private static final RegisterStyle[] STYLES = new RegisterStyle[] {
			new RootRegisterStyle().index(0), new ChildRegisterStyle().index(1) };
	private int index;

	static RegisterStyle root() {
		return style(0);
	}

	private static RegisterStyle style(int depth) {
		if (depth >= STYLES.length)
			depth = STYLES.length - 1;
		return STYLES[depth];
	}

	RegisterStyle index(int index) {
		this.index = index;
		return this;
	}

	RegisterStyle child() {
		return style(index + 1);
	}

	abstract void decorateWidget(RegisterWidgetImpl widget);

	abstract void init(ITitle title);

	abstract void onFront(ITitle title);

	abstract void onBack(ITitle title);
}
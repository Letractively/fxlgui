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
package co.fxl.gui.demo;

import co.fxl.gui.api.IVerticalPanel;

class CustomPanelDemo extends DemoTemplate implements Decorator {

	@Override
	public void decorate(IVerticalPanel parent) {
		parent.color().white();
		IVerticalPanel panel = parent.add().panel().vertical().spacing(10);
		panel.stretch(false);
		panel.add().label().text("Custom/Extension Panels").font().weight()
				.bold().pixel(14);
		panel.add().label().text("To use custom panels, we can use the method");
		panel.add().label()
				.text("IPanel<?> ILayout.plugIn(Class<?> layoutType);").font()
				.family().courier();
		panel.add()
				.label()
				.text("If available, the employed GUI API implementation returns an instance of the input panel type.");
	}
}

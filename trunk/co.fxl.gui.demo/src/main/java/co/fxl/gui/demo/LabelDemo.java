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
package co.fxl.gui.demo;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;

class LabelDemo extends DemoTemplate implements Decorator {

	@Override
	public void decorate(ExampleDecorator decorator, IVerticalPanel panel) {
		ExampleComposite example = new ExampleComposite(decorator, panel);
		IContainer container = example.title("Label");
		container.label().text("Label").font().family().arial().weight().bold()
				.pixel(16).color().red();
		example.codeFragment("panel.add().label().text(\"Label\").font().family().arial().weight().bold().pixel(16).color().red();");
	}
}

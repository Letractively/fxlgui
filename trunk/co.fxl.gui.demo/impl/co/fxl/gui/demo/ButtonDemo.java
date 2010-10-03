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

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;

class ButtonDemo extends DemoTemplate implements Decorator {

	private ExampleComposite example;

	@Override
	public void decorate(ExampleDecorator decorator, final IVerticalPanel panel) {
		example = new ExampleComposite(decorator, panel);
		IContainer container = example.title("Button");
		IButton button = container.button();
		button.text("Button");
		example.codeFragment("panel.add().button().text(\"Button\").addClickListener(new IClickListener() {"
				+ "\n&nbsp;&nbsp;@Override"
				+ "\n&nbsp;&nbsp;public void onClick() {"
				+ "\n&nbsp;&nbsp;&nbsp;&nbsp;output.add().label().text(\"Button clicked!\");"
				+ "\n&nbsp;&nbsp;}" + "\n});");
		button.addClickListener(new IClickListener() {
			@Override
			public void onClick() {
				example.append("Button clicked!");
			}
		});
	}

	@Override
	public void update(IVerticalPanel panel) {
		example.clearOutput();
	}
}

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

import co.fxl.gui.api.ICheckBox;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

class CheckboxDemo extends DemoTemplate implements Decorator {

	private static final String TEXTFIELD_INIT = "Checkbox";
	private ExampleComposite example;
	private ICheckBox checkBox;

	@Override
	public void decorate(ExampleDecorator decorator, final IVerticalPanel panel) {
		example = new ExampleComposite(decorator, panel);
		IContainer container = example.title("Checkbox");
		checkBox = container.checkBox();
		checkBox.text(TEXTFIELD_INIT);
		example.codeFragment("panel.add().checkBox().text(\"Checkbox\").addUpdateListener(new IUpdateListener<Boolean>() {"
				+ "\n&nbsp;&nbsp;@Override"
				+ "\n&nbsp;&nbsp;public void onUpdate(Boolean value) {"
				+ "\n&nbsp;&nbsp;&nbsp;&nbsp;output.text(String.valueOf(value));"
				+ "\n&nbsp;&nbsp;}" + "\n});");
		example.output("false");
		checkBox.addUpdateListener(new IUpdateListener<Boolean>() {
			@Override
			public void onUpdate(Boolean value) {
				example.output(String.valueOf(value));
			}
		});
	}

	@Override
	public void update(IVerticalPanel panel) {
		checkBox.text(TEXTFIELD_INIT);
		example.output("false");
	}
}

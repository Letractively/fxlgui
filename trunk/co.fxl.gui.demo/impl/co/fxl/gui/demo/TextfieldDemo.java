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

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.ITextField;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IUpdateable.IUpdateListener;

class TextfieldDemo extends DemoTemplate implements Decorator {

	private static final String TEXTFIELD_INIT = "Textfield...";
	private ExampleComposite example;
	private ITextField textField;

	@Override
	public void decorate(final IVerticalPanel panel) {
		example = new ExampleComposite(panel);
		IContainer container = example.title("Textfield");
		textField = container.textField();
		textField.text(TEXTFIELD_INIT).width(200).font().family().arial().weight().bold();
		example.codeFragment("panel.add().textField().text(\"Textfield...\").addUpdateListener(new IUpdateListener<String>() {"
				+ "\n&nbsp;&nbsp;@Override"
				+ "\n&nbsp;&nbsp;public void onUpdate(String value) {"
				+ "\n&nbsp;&nbsp;&nbsp;&nbsp;output.text(value);"
				+ "\n&nbsp;&nbsp;}" + "\n});");
		example.output(TEXTFIELD_INIT);
		textField.addUpdateListener(new IUpdateListener<String>() {
			@Override
			public void onUpdate(String value) {
				example.output(value);
			}
		});
	}

	@Override
	public void update(IVerticalPanel panel) {
		textField.text(TEXTFIELD_INIT);
		example.output(TEXTFIELD_INIT);
	}
}

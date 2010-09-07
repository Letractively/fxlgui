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
import co.fxl.gui.api.IRadioButton;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IRadioButton.IGroup;

class RadioButtonDemo extends DemoTemplate implements Decorator {

	private ExampleComposite example;

	@Override
	public void decorate(final IVerticalPanel vpanel) {
		example = new ExampleComposite(vpanel);
		IContainer container = example.title("Radiobutton");
		IVerticalPanel verticalPanel = container.panel().vertical();
		IRadioButton button1 = verticalPanel.add().radioButton().text(
				"Radiobutton 1");
		IRadioButton button2 = verticalPanel.add().radioButton().text(
				"Radiobutton 2");
		IRadioButton button3 = verticalPanel.add().radioButton().text(
				"Radiobutton 3");
		IGroup group = button1.group();
		group.add(button2, button3);
		example.codeFragment("IRadioButton button1 = panel.add().radioButton().text(\"Radiobutton 1\");"
						+ "\nIRadioButton button2 = panel.add().radioButton().text(\"Radiobutton 2\");"
						+ "\nIRadioButton button3 = panel.add().radioButton().text(\"Radiobutton 3\");"
						+ "\nIGroup group = button1.group();"
						+ "\ngroup.add(button2, button3);");
	}
}

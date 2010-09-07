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
import co.fxl.gui.api.IVerticalPanel;

class VerticalPanelDemo extends DemoTemplate implements Decorator {

	@Override
	public void decorate(final IVerticalPanel vpanel) {
		ExampleComposite example = new ExampleComposite(vpanel, true);
		IContainer container = example.title("Vertical Panel");
		IVerticalPanel panel = container.panel().vertical();
		panel.spacing(10).color().lightgray();
		panel.add().label().text("Label");
		panel.add().checkBox().text("Checkbox");
		panel.add().comboBox().addText("Combobox 1")
				.addText("Combobox 2");
		panel.add().radioButton().text("Radio Button");
		StringBuffer b = new StringBuffer();
		b.append("IVerticalPanel verticalPanel = panel.add().panel().vertical();");
		b.append("\nverticalPanel.spacing(10).color().lightgray();");
		b.append("\nverticalPanel.add().label().text(\"Label\");");
		b.append("\nverticalPanel.add().checkBox().text(\"Checkbox\");");
		b.append("\nverticalPanel.add().comboBox().addText(\"Combobox 1\", \"Combobox 2\");");
		b.append("\nverticalPanel.add().radioButton().text(\"Radio Button\");");
		example.codeFragment(b.toString());
	}
}

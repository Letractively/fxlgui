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
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IVerticalPanel;

class GridPanelDemo extends DemoTemplate implements Decorator {

	@Override
	public void decorate(ExampleDecorator decorator, final IVerticalPanel vpanel) {
		vpanel.stretch(false);
		ExampleComposite example = new ExampleComposite(decorator, vpanel, true, false);
		IContainer container = example.title("Grid Panel");
		IGridPanel panel = container.panel().grid();
		panel.spacing(10).color().lightgray();
		panel.cell(0, 0).label().text("Label");
		panel.cell(0, 1).checkBox().text("Checkbox");
		panel.cell(1, 0).comboBox().addText("Combobox 1").addText("Combobox 2");
		panel.cell(1, 1).radioButton().text("Radio Button");
		StringBuffer b = new StringBuffer();
		b.append("IGridPanel gridPanel = panel.add().panel().grid();");
		b.append("\ngridPanel.spacing(10).color().lightgray();");
		b.append("\ngridPanel.cell(0, 0).label().text(\"Label\");");
		b.append("\ngridPanel.cell(0, 1).checkBox().text(\"Checkbox\");");
		b.append("\ngridPanel.cell(1, 0).comboBox().addText(\"Combobox 1\", \"Combobox 2\");");
		b.append("\ngridPanel.cell(1, 1).radioButton().text(\"Radio Button\");");
		example.codeFragment(b.toString());
	}
}

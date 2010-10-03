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
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IColored.IColor;

class DockPanelDemo extends DemoTemplate implements Decorator {

	@Override
	public void decorate(ExampleDecorator decorator, final IVerticalPanel vpanel) {
		ExampleComposite example = new ExampleComposite(decorator, vpanel, true, true);
		IContainer container = example.title("Dock Panel");
		IDockPanel panel = container.panel().dock();
		panel(panel.top(), "Top").green();
		panel(panel.bottom(), "Bottom").yellow();
		panel(panel.left(), "Left").lightgray();
		panel(panel.right(), "Right").red();
		panel(panel.center(), "Center").blue();
		StringBuffer b = new StringBuffer();
		b.append("IDockPanel dockPanel = panel.add().panel().dock();");
		b.append("\npanel(dockPanel.top(), \"Top\").green();");
		b.append("\npanel(dockPanel.bottom(), \"Bottom\").yellow();");
		b.append("\npanel(dockPanel.left(), \"Left\").lightgray();");
		b.append("\npanel(dockPanel.right(), \"Right\").red();");
		b.append("\npanel(dockPanel.center(), \"Center\").blue();");
		b.append("\n\nprivate IColor panel(IContainer c, String text) {");
		b.append("\n&nbsp;&nbsp;IVerticalPanel panel = c.panel().vertical().spacing(10);");
		b.append("\n&nbsp;&nbsp;panel.add().label().text(text);");
		b.append("\n&nbsp;&nbsp;return panel.color();");
		b.append("\n}");
		example.codeFragment(b.toString());
	}

	private IColor panel(IContainer c, String text) {
		IVerticalPanel panel = c.panel().vertical().spacing(10);
		panel.add().label().text(text);
		return panel.color();
	}
}

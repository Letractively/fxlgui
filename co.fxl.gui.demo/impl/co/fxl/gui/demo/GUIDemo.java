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
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IDockPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.api.IMenuWidget;
import co.fxl.gui.navigation.impl.NavigationWidgetImplProvider;
import co.fxl.gui.table.filter.impl.FilterTableWidgetImplProvider;
import co.fxl.gui.table.impl.TableWidgetImplProvider;

public class GUIDemo {

	private ExampleDecorator decorator;
	private IVerticalPanel panel;

	public GUIDemo() {
	}

	public GUIDemo decorator(ExampleDecorator decorator) {
		this.decorator = decorator;
		return this;
	}

	public GUIDemo addHeaderAndContent(IDisplay display) {
		addHeader(display);
		addContent(display);
		return this;
	}

	public IContainer addHeader(IDisplay display) {
		IVerticalPanel headerPanel = panel(display).add().panel().vertical()
				.spacing(10);
		IDockPanel dock = headerPanel.add().panel().dock();
		headerPanel.color().rgb(50, 101, 152);
		dock.center().label().text("GUI-API Demo & Showcase").font().weight()
				.bold().pixel(20).color().white();
		return dock.right();
	}

	private IVerticalPanel panel(IDisplay display) {
		if (panel == null) {
			panel = display.container().panel().vertical();
		}
		return panel;
	}

	public void addContent(IDisplay display) {
		IVerticalPanel panel = panel(display).add().panel().vertical();
		display.register(new NavigationWidgetImplProvider());
		display.register(new TableWidgetImplProvider());
		display.register(new FilterTableWidgetImplProvider());
		display.title("GUI-API Demo");
		panel.color().white();
		panel.stretch(true);
		IMenuWidget navigation = (IMenuWidget) panel.add().widget(
				IMenuWidget.class);
		addElements(navigation);
		addPanels(navigation);
		addWidgets(navigation);
		display.fullscreen();
		display.visible(true);
	}

	private void addElements(IMenuWidget widget) {
		MenuItem item = new MenuItem(widget, decorator, "Elements");
		item.nest("Label", new LabelDemo()).active();
		item.nest("Button", new ButtonDemo());
		item.nest("Checkbox", new CheckboxDemo());
		item.nest("Combobox", new ComboboxDemo());
		item.nest("Radiobutton", new RadioButtonDemo());
		item.nest("Textfield", new TextfieldDemo());
		item.nest("Textarea", new TextareaDemo());
		item.nest("Image", new ImageDemo());
		item.active();
	}

	private void addPanels(IMenuWidget widget) {
		MenuItem item = new MenuItem(widget, decorator, "Panels");
		item.nest("Horizontal", new HorizontalPanelDemo()).active();
		item.nest("Vertical", new VerticalPanelDemo());
		item.nest("Grid", new GridPanelDemo());
		item.nest("Dock", new DockPanelDemo());
		item.nest("Card", new CardPanelDemo());
		item.nest("Custom", new CustomPanelDemo());
	}

	private void addWidgets(IMenuWidget widget) {
		new MenuItem(widget, decorator, "Widgets", new WidgetsDemo());
	}
}

/**
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
 *
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 */
package co.fxl.gui.table.util.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.api.ILazyScrollPane;
import co.fxl.gui.table.util.api.ILazyScrollPane.IDecorator;
import co.fxl.gui.table.util.impl.LazyScrollPanelImplWidgetProvider;

class LazyScrollPaneTest implements IDecorator, IClickListener {

	private ILazyScrollPane widget;
	private IHorizontalPanel[] buttons = new IHorizontalPanel[1000];

	void run(IDisplay display) {
		display.register(new LazyScrollPanelImplWidgetProvider());
		widget = (ILazyScrollPane) display.container().panel().vertical().add()
				.widget(ILazyScrollPane.class);
		widget.size(1000);
		widget.minRowHeight(20);
		widget.height(900);
		widget.decorator(this);
		widget.visible(true);
		display.visible(true);
	}

	@Override
	public void decorate(IContainer c, int firstRow, int lastRow) {
		IVerticalPanel v = c.panel().vertical();
		for (int i = firstRow; i <= lastRow; i++) {
			IHorizontalPanel container = v.add().panel().horizontal();
			container.height(22);
			IClickable<?> clickable = container;
			clickable.addClickListener(this);
			IHorizontalPanel content = container.add().panel().horizontal()
					.spacing(2);
			content.addSpace((i % 3) * 10);
			IImage image = content.add().image();
			image.resource("closed.png");
			image.addClickListener(this);
			IImage icon = content.add().image().resource("export.png");
			icon.addClickListener(this);
			content.addSpace(2);
			String name = "Tree Node " + i;
			boolean isNull = name == null || name.trim().equals("");
			ILabel label = content.add().label()
					.text(isNull ? "unnamed" : name);
			label.font().pixel(12);
			if (isNull)
				label.font().weight().italic().color().gray();
			label.addClickListener(this);
			content.addSpace(10);
			buttons[i] = container;
		}
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new LazyScrollPaneTest().run(display);
	}

	@Override
	public int rowHeight(int rowIndex) {
		return buttons[rowIndex].height();
	}

	@Override
	public void onClick() {
		throw new MethodNotImplementedException();
	}
}

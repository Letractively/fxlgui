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
package co.fxl.gui.tree.test;

import java.lang.reflect.InvocationTargetException;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.impl.NavigationWidgetImplProvider;
import co.fxl.gui.register.impl.RegisterWidgetImplProvider;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;
import co.fxl.gui.tree.model.ModelTreeWidgetProvider;

class TreeWidgetSingleViewTest {

	void run(IDisplay display) {
		display.register(new ModelTreeWidgetProvider());
		display.register(new RegisterWidgetImplProvider());
		display.register(new NavigationWidgetImplProvider());
		IVerticalPanel panel = display.container().panel().vertical();
		panel.color().white();
		@SuppressWarnings("unchecked")
		ITreeWidget<String> widget = (ITreeWidget<String>) panel.add().widget(
				ITreeWidget.class);
		widget.title("Tree");
		widget.setDetailView(new IDecorator<String>() {

			@Override
			public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
					ITree<String> tree) {
				clear(panel);
				panel.spacing(16);
				panel.add().label().text(tree.object());
			}

			@Override
			public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
					Object tree) {
			}

			@Override
			public boolean clear(IVerticalPanel contentPanel) {
				contentPanel.clear();
				return true;
			}

			@Override
			public void resize() {
				throw new MethodNotImplementedException();
			}

		});
		widget.root(new TestTree(null, "Folder", 0));
		display.fullscreen().visible(true);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
		IDisplay display = (IDisplay) clazz.getMethod("instance",
				new Class<?>[0]).invoke(null, new Object[0]);
		new TreeWidgetSingleViewTest().run(display);
	}
}
